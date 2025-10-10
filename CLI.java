import java.util.*; 

public class CLI {
    // Collect all user preferences via CLI, allowing "empty" for any prompt.
    public static Preferences collectPreferences() {
        Scanner sc = new Scanner(System.in);   // read from stdin
        Preferences p = new Preferences();     // mutable prefs container

        System.out.println("Answer or 'skip' to leave blank.\n");

        // Year and price ranges (min-max). Null means no constraint.
        p.setYearRange(askRange(sc, "Year range (e.g. 2015-2025)"));
        p.setPriceRange(askRange(sc, "Price range USD (e.g. 15000-40000)"));

        // Single-value targets/options. Null/empty = no constraint.
        p.setMpgTarget(askDouble(sc, "Target avg MPG (±5)")); // soft target with ±5 window
        p.setPreferredBrands(Preferences.parseList(ask(sc, "Preferred brand(s)")));
        p.setBodyTypes(Preferences.parseList(ask(sc, "Allowed body types (Sedan,SUV,...)")));
        p.setFuelTypes(Preferences.parseList(ask(sc, "Allowed fuel types (Gasoline,Hybrid,EV)")));

        // Booleans use yes/no/skip → true/false/null
        p.setSportyLook(askBoolean(sc, "Prefer sporty look? (yes/no/skip)"));
        p.setMaxZeroToSixty(askDouble(sc, "Max 0–60 mph time (sec)")); // lower is better
        p.setTractionControl(askBoolean(sc, "Require traction control? (yes/no/skip)"));

        return p; // ready for Scorer
    }

    private static String ask(Scanner sc, String msg) {
        System.out.print(msg + ": ");
        String ans = sc.nextLine().trim();
        return ans.equalsIgnoreCase("skip") ? "" : ans;
    }

    // Ask for a numeric range like "min-max". Returns null if skipped/bad input.
    private static double[] askRange(Scanner sc, String msg) {
        String s = ask(sc, msg);
        if (s.isEmpty() || !s.contains("-")) return null; // user skipped or malformed
        try {
            String[] parts = s.split("-");
            // store as [min, max]; caller casts to int where needed
            return new double[] {
                Double.parseDouble(parts[0].trim()),
                Double.parseDouble(parts[1].trim())
            };
        } catch (Exception e) {
            return null; // treat parse failure as skipped
        }
    }

    // Ask for a single double value. Returns null if skipped/bad input.
    private static Double askDouble(Scanner sc, String msg) {
        String s = ask(sc, msg);
        if (s.isEmpty()) return null; // skipped
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return null; // invalid number → ignore
        }
    }

    // Ask for yes/no/skip → true/false/null.
    private static Boolean askBoolean(Scanner sc, String msg) {
        String s = ask(sc, msg);
        if (s.equalsIgnoreCase("yes")) return true;
        if (s.equalsIgnoreCase("no"))  return false;
        return null; // skipped or anything else
    }
}