import java.util.*;

class Scorer {

    static class ScoreBreakdown {
        double priceN, mpgN, yearN, sportyN, accelN, fuelN, bodyN, trReliN;
        double total;
        String explanation;
    }

    static class Result {
        final Car car;
        final ScoreBreakdown b;
        Result(Car c, ScoreBreakdown b) { this.car = c; this.b = b; }
    }

    private final List<Car> cars;

    public Scorer(List<Car> cars) { this.cars = cars; }

    public List<Result> scoreAndRank(Preferences p) {
        // --- Precompute mins/maxes for normalization across dataset ---
        double minPrice = Double.MAX_VALUE, maxPrice = -1;
        double minMpg   = Double.MAX_VALUE, maxMpg   = -1;
        double minYear  = Double.MAX_VALUE, maxYear  = -1;
        double min0060  = Double.MAX_VALUE, max0060  = -1;
        double minReli  = Double.MAX_VALUE, maxReli  = -1;

        for (Car c : cars) {
            minPrice = Math.min(minPrice, c.getPriceUSD());
            maxPrice = Math.max(maxPrice, c.getPriceUSD());
            double avgMpg = c.getAvgMpg();
            minMpg = Math.min(minMpg, avgMpg);
            maxMpg = Math.max(maxMpg, avgMpg);
            minYear = Math.min(minYear, c.getYear());
            maxYear = Math.max(maxYear, c.getYear());
            min0060 = Math.min(min0060, c.getZeroToSixty());
            max0060 = Math.max(max0060, c.getZeroToSixty());
            minReli = Math.min(minReli, c.getReliabilityScore());
            maxReli = Math.max(maxReli, c.getReliabilityScore());
        }

        // --- Weights ---
        double wPrice = 0.20, wMpg = 0.20, wYear = 0.15, wSporty = 0.10, wAccel = 0.10,
               wFuel  = 0.10, wBody = 0.10, wTraRel = 0.05;

        List<Result> results = new ArrayList<>();

        for (Car c : cars) {
            ScoreBreakdown b = new ScoreBreakdown();
            StringBuilder exp = new StringBuilder();

            // Notes / soft info
            if (p.hasBrandFilter()) {
                boolean match = p.getPreferredBrands().stream()
                        .anyMatch(br -> br.equalsIgnoreCase(c.getBrand()));
                if (match) exp.append("Brand match; ");
            }
            if (p.getMinYear() != null && c.getYear() < p.getMinYear()) exp.append("older than min year; ");
            if (p.getMaxYear() != null && c.getYear() > p.getMaxYear()) exp.append("newer than max year range; ");
            if (p.getMinPrice() != null && c.getPriceUSD() < p.getMinPrice()) exp.append("below min price; ");
            if (p.getMaxPrice() != null && c.getPriceUSD() > p.getMaxPrice()) exp.append("above max price; ");

            // --- Components (0..1) ---
            // Price (cheaper better); soft penalty if above max
            b.priceN = normalizeInverse(c.getPriceUSD(), minPrice, maxPrice);
            if (p.getMaxPrice() != null && c.getPriceUSD() > p.getMaxPrice()) {
                b.priceN *= penaltyScale(c.getPriceUSD() - p.getMaxPrice(),
                                         Math.max(1, p.getMaxPrice()));
            }

            // MPG with target penalty if below (target - 5)
            double avgMpg = c.getAvgMpg();
            b.mpgN = normalize(avgMpg, minMpg, maxMpg);
            if (p.getMpgTarget() != null && avgMpg < (p.getMpgTarget() - 5.0)) {
                b.mpgN *= penaltyScale((p.getMpgTarget() - 5.0) - avgMpg,
                                       Math.max(1.0, p.getMpgTarget()));
            }

            // Year
            b.yearN = normalize(c.getYear(), minYear, maxYear);

            // Sporty look (neutral = 0.5)
            if (p.getSportyLook() == null) b.sportyN = 0.5;
            else b.sportyN = (p.getSportyLook() == c.isSportyLook()) ? 1.0 : 0.0;

            // 0–60 (lower better) with soft penalty if exceeds max
            b.accelN = normalizeInverse(c.getZeroToSixty(), min0060, max0060);
            if (p.getMaxZeroToSixty() != null && c.getZeroToSixty() > p.getMaxZeroToSixty()) {
                b.accelN *= penaltyScale(c.getZeroToSixty() - p.getMaxZeroToSixty(),
                                         Math.max(1.0, p.getMaxZeroToSixty()));
            }

            // Fuel match (neutral 0.5 if no filter)
            if (p.hasFuelFilter()) {
                boolean fuelMatch = p.getFuelTypes().stream()
                        .anyMatch(f -> f.equalsIgnoreCase(c.getFuelType()));
                b.fuelN = fuelMatch ? 1.0 : 0.0;
            } else b.fuelN = 0.5;

            // Body match (neutral 0.5 if no filter)
            if (p.hasBodyFilter()) {
                boolean bodyMatch = p.getBodyTypes().stream()
                        .anyMatch(bt -> bt.equalsIgnoreCase(c.getBodyType()));
                b.bodyN = bodyMatch ? 1.0 : 0.0;
            } else b.bodyN = 0.5;

            // Traction/Reliability combo
            double reliN = normalize(c.getReliabilityScore(), minReli, maxReli);
            if (p.getTractionControl() == null) {
                b.trReliN = reliN;
            } else if (p.getTractionControl() == Boolean.TRUE) {
                b.trReliN = (c.hasTractionControl() ? 1.0 : 0.0) * 0.7 + reliN * 0.3;
            } else {
                b.trReliN = reliN;
            }

            // Total
            b.total = b.priceN * wPrice + b.mpgN * wMpg + b.yearN * wYear + b.sportyN * wSporty
                    + b.accelN * wAccel + b.fuelN * wFuel + b.bodyN * wBody + b.trReliN * wTraRel;

            b.explanation = exp.toString();
            results.add(new Result(c, b));
        }

        // Sort & tie-breakers
        results.sort((r1, r2) -> {
            int cmp = Double.compare(r2.b.total, r1.b.total);
            if (cmp != 0) return cmp;
            int mpgCmp = Double.compare(r2.car.getAvgMpg(), r1.car.getAvgMpg());
            if (mpgCmp != 0) return mpgCmp;
            int priceCmp = Integer.compare(r1.car.getPriceUSD(), r2.car.getPriceUSD());
            if (priceCmp != 0) return priceCmp;
            return Integer.compare(r2.car.getYear(), r1.car.getYear());
        });

        return results; // ✅ ensure a return on all paths
    }

    // --- helpers ---
    private static double normalize(double x, double min, double max) {
        if (max - min == 0) return 1.0;
        return (x - min) / (max - min);
    }

    private static double normalizeInverse(double x, double min, double max) {
        if (max - min == 0) return 1.0;
        return 1.0 - ((x - min) / (max - min));
    }

    private static double penaltyScale(double diff, double base) {
        return 1.0 / (1.0 + (diff / base));
    }
}
