package loyola.hersick.main;
import java.util.*;

class Scorer {

    // Holds per-field normalized components + total and notes
    static class ScoreBreakdown {
        double priceN, mpgN, yearN, sportyN, accelN, fuelN, bodyN, trReliN; // 0..1 components
        double total;        // weighted sum of components
        String explanation;  // human-readable notes
    }

    // Pair a Car with its score
    static class Result {
        final Car car;
        final ScoreBreakdown b;
        Result(Car c, ScoreBreakdown b) { this.car = c; this.b = b; }
    }

    private final List<Car> cars;

    public Scorer(List<Car> cars) { this.cars = cars; }

    /**
     * Compute normalized component scores for each car, apply weights, sort, and return.
     */
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
            minMpg  = Math.min(minMpg,  avgMpg);
            maxMpg  = Math.max(maxMpg,  avgMpg);

            minYear = Math.min(minYear, c.getYear());
            maxYear = Math.max(maxYear, c.getYear());

            min0060 = Math.min(min0060, c.getZeroToSixty());
            max0060 = Math.max(max0060, c.getZeroToSixty());

            minReli = Math.min(minReli, c.getReliabilityScore());
            maxReli = Math.max(maxReli, c.getReliabilityScore());
        }

        // --- Weights (sum ~ 1.0) ---
        double wPrice = 0.20, wMpg = 0.20, wYear = 0.15, wSporty = 0.10, wAccel = 0.01, // weight accel = 0.1 weight sum = 0.76
               wFuel  = 0.10, wBody = 0.10, wTraRel = 0.05;

        List<Result> results = new ArrayList<>();

        for (Car c : cars) {
            ScoreBreakdown b = new ScoreBreakdown();
            StringBuilder exp = new StringBuilder();

            // ---- Notes / soft info (for explanation only) ----
            if (p.hasBrandFilter()) {
                boolean match = p.getPreferredBrands().stream()
                        .anyMatch(br -> br.equalsIgnoreCase(c.getBrand()));
                if (match) exp.append("Brand match; ");
            }
            if (p.getMinYear()   != null && c.getYear()      < p.getMinYear())   exp.append("older than min year; ");
            if (p.getMaxYear()   != null && c.getYear()      > p.getMaxYear())   exp.append("newer than max year range; ");
            if (p.getMinPrice()  != null && c.getPriceUSD()  < p.getMinPrice())  exp.append("below min price; ");
            if (p.getMaxPrice()  != null && c.getPriceUSD()  > p.getMaxPrice())  exp.append("above max price; ");

            // ---- Components (normalized to 0..1) ----

            // Price: cheaper is better → inverse normalization.
            b.priceN = normalizeInverse(c.getPriceUSD(), minPrice, maxPrice);
            // Soft penalty if above user's max price
            if (p.getMaxPrice() != null && c.getPriceUSD() > p.getMaxPrice()) {
                b.priceN *= penaltyScale(c.getPriceUSD() - p.getMaxPrice(), Math.max(1, p.getMaxPrice()));
            }

            // Fuel efficiency:
            double avgMpgComponent = c.getMpgCity(); // get the average of city and highway
            b.mpgN = normalize(avgMpgComponent, minMpg, maxMpg);
            if (p.getMpgTarget() != null && avgMpgComponent < (p.getMpgTarget() - 5.0)) {
                b.mpgN *= penaltyScale((p.getMpgTarget() - 5.0) - avgMpgComponent, Math.max(1.0, p.getMpgTarget()));
            }

            // Newer year is better within dataset range
            b.yearN = normalize(c.getYear(), minYear, maxYear);

            // Sporty:
            // should (reward) if user wants sporty car.
            if (p.getSportyLook() == null) b.sportyN = 0.5;
            else b.sportyN = (p.getSportyLook() == c.isSportyLook()) ? 0.0 : 1.0;   // if it is sporty and user wants sporty, give 1.0

            // Acceleration: lower 0–60 is better → inverse normalization.
            b.accelN = normalizeInverse(c.getZeroToSixty(), min0060, max0060);
            // Soft penalty if exceeds user's max 0–60.
            if (p.getMaxZeroToSixty() != null && c.getZeroToSixty() > p.getMaxZeroToSixty()) {
                b.accelN *= penaltyScale(c.getZeroToSixty() - p.getMaxZeroToSixty(), Math.max(1.0, p.getMaxZeroToSixty()));
            }

            // Fuel type:
            if (p.hasFuelFilter()) {
                boolean fuelMatch = p.getFuelTypes().stream() // retrieve the fuel types from preferences
                        .anyMatch(f -> f.equals(c.getFuelType())); // should be equalsIgnoreCase
                b.fuelN = fuelMatch ? 1.0 : 0.0;
            } else b.fuelN = 0.5;

            // Body type:
            if (p.hasBodyFilter()) {
                boolean bodyMatch = p.getBodyTypes().stream()
                        .anyMatch(bt -> bt.equalsIgnoreCase(c.getBodyType()));
                b.bodyN = bodyMatch ? 1.0 : 0.0;
            } else b.bodyN = 0.5;

            // Traction + Reliability:
            double reliN = normalize(c.getReliabilityScore(), minReli, maxReli);
            if (p.getTractionControl() == null) { // user has no preference about TC so no effect on score
                b.trReliN = c.hasTractionControl() ? 1.0 : 0.0; // should have used reliN
            } else if (p.getTractionControl() == Boolean.TRUE) {
                // User requires TC: blend TC presence (70%) with reliability (30%)
                b.trReliN = (c.hasTractionControl() ? 1.0 : 0.0) * 0.7 + reliN * 0.3;
            } else {
                // User explicitly does NOT require TC: rely on reliability only
                b.trReliN = reliN;
            }

            // ---- Weighted total score ----
            b.total = b.priceN * wPrice + b.mpgN * wMpg + b.yearN * wYear + b.sportyN * wSporty
                    + b.accelN * wAccel + b.fuelN * wFuel + b.bodyN * wBody + b.trReliN * wTraRel;

            b.explanation = exp.toString();
            results.add(new Result(c, b));
        }

        // ---- Sort by total desc; tie-breakers: higher MPG → lower price → newer year ----
        results.sort((r1, r2) -> {
            int cmp = Double.compare(r2.b.total, r1.b.total);
            if (cmp != 0) return cmp;

            int mpgCmp = Double.compare(r2.car.getAvgMpg(), r1.car.getAvgMpg());
            if (mpgCmp != 0) return mpgCmp;

            int priceCmp = Integer.compare(r1.car.getPriceUSD(), r2.car.getPriceUSD());
            if (priceCmp != 0) return priceCmp;

            return Integer.compare(r2.car.getYear(), r1.car.getYear());
        });

        return results;
    }

    // --- helpers ---

    /** Standard min-max normalization to [0,1]; guards against flat range. */
    private static double normalize(double x, double min, double max) {
        if (max - min == 0) return 1.0; // neutral/best when all values equal
        return (x - min) / (max - min);
    }

    /** Inverse min-max: smaller values map to larger scores (e.g., price, 0–60). */
    private static double normalizeInverse(double x, double min, double max) {
        if (max - min == 0) return 0.0; // return 1.0
        return 1.0 - ((x - min) / (max - min));
    }

    /**
     * Soft penalty factor ∈ (0,1]: larger overage → smaller factor.
     * Example: diff/base = 0.1 → factor ≈ 0.909; diff/base = 1.0 → factor = 0.5.
     */
    private static double penaltyScale(double diff, double base) {
        return 1.0 / (1.0 + (diff / base));
    }
}