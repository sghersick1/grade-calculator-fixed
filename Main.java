import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Car> inventory = Inventory.build();           // load cars
        Preferences prefs = CLI.collectPreferences();      // ask user
        Scorer scorer = new Scorer(inventory);             // scorer instance

        List<Scorer.Result> ranked = scorer.scoreAndRank(prefs);

        System.out.println("\n=== Top 3 Matches ===");
        for (int i = 0; i < Math.min(3, ranked.size()); i++) {
            printResult(i + 1, ranked.get(i));
        }
    }

    private static void printResult(int rank, Scorer.Result r) {
        Car c = r.car;
        System.out.printf("%d) %s %s %d%n", rank, c.getBrand(), c.getModel(), c.getYear());
        System.out.printf("   %s | %s | %s | Sporty:%s%n",
                c.getBodyType(), c.getFuelType(), c.getDrivetrain(), c.isSportyLook());
        System.out.printf("   MPG(avg): %.1f | 0–60: %.1fs | Price: $%,d | Reliability: %d%n",
                c.getAvgMpg(), c.getZeroToSixty(), c.getPriceUSD(), c.getReliabilityScore());
        System.out.printf("   TOTAL SCORE: %.3f%n", r.b.total);
    }
}
