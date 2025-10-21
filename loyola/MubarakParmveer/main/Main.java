package loyola.MubarakParmveer.main;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("What do you want to do? Enter 1 for grade calculator and 2 for car recommendation (or q to quit)");
        String input = sc.nextLine().trim().toLowerCase();
        if (input.equals("q")) return;

        while (!(input.equals("1")) && !(input.equals("2"))) {
            System.out.println("Please enter 1 or 2 (or q to quit)");
            input = sc.nextLine().trim().toLowerCase();
            if (input.equals("q")) return;
        }

        if (input.equals("1")) {
            runGradeCalculator(sc);
            return;
        }

        if (input.equals("2")) {
            List<Car> inventory = Inventory.build();
            Preferences prefs = CLI.collectPreferences();
            Scorer scorer = new Scorer(inventory);
            List<Scorer.Result> ranked = scorer.scoreAndRank(prefs);

            System.out.println("\n=== Top 3 Matches ===");
            for (int i = 0; i < Math.min(3, ranked.size()); i++) {
                Scorer.Result r = ranked.get(i);
                Car c = r.car;
                System.out.printf("%d) %s %s %d%n", i + 1, c.getBrand(), c.getModel(), c.getYear());
                System.out.printf("   %s | %s | %s | Sporty:%s%n",
                        c.getBodyType(), c.getFuelType(), c.getDrivetrain(), c.isSportyLook());
                System.out.printf("   MPG(avg): %.1f | 0–60: %.1fs | Price: $%,d | Reliability: %d%n",
                        c.getAvgMpg(), c.getZeroToSixty(), c.getPriceUSD(), c.getReliabilityScore());
                System.out.printf("   TOTAL SCORE: %.3f%n", r.b.total);
            }
        }
    }

    private static void runGradeCalculator(Scanner sc) {
        Grade grade = new Grade();
        System.out.println("Please enter the Name, The weight of the grade, and the score. Enter done when finished.");
        while (true) {
            String action = sc.nextLine().trim();
            if (action.equalsIgnoreCase("done")) break;
            if (action.isEmpty()) continue;

            String[] part = action.split("[,\\s]+");
            if (part.length < 3) {
                System.out.println("Format: <Name> <Weight%> <Score>");
                continue;
            }
            String name = part[0];
            try {
                float w = Float.parseFloat(part[1]);
                float s = Float.parseFloat(part[2]);
                grade.addGrade(name, w, s);
            } catch (Exception e) {
                System.out.println("Format: <Name> <Weight%> <Score>");
            }
        }

        System.out.println("What would you like to see. 1 for final grade and 2 for final letter grade.");
        String choise = sc.nextLine().trim();
        if (choise.equals("1")) {
            double g = grade.getFinalGrade();
            System.out.println("The final grade is:" + String.format("%.2f", g));
        } else if (choise.equals("2")) {
            System.out.println("you got a " + grade.getLetterGrade());
        } else {
            System.out.println("Error please enter 1 or 2");
        }
    }
}
