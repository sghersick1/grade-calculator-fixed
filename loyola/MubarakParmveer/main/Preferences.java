package loyola.MubarakParmveer.main;

// HashSet, Set, etc.
import java.util.HashSet;
import java.util.Set;

/**
 * User preferences for car search, as specified via CLI arguments.
 * All fields are nullable, with null meaning "no preference".
 * 
 * This class is used by Main (to parse CLI args) and Scorer (to evaluate cars).
 * 
 * Note: This is a simple data container with no validation logic.
 *       It assumes that Main has already validated the CLI args.
 */
public class Preferences {
    // Range filters (null ⇒ no constraint)
    private Integer minYear, maxYear;        // e.g., 2015..2025
    private Integer minPrice, maxPrice;      // USD range

    // Single-value targets (null ⇒ no constraint)
    private Double mpgTarget;                // target avg MPG with ±5 tolerance

    // Multi-select filters (empty set ⇒ no constraint)
    private Set<String> bodyTypes = new HashSet<>();       // e.g., Sedan,SUV,Coupe
    private Set<String> preferredBrands = new HashSet<>(); // brand boost
    private Set<String> fuelTypes = new HashSet<>();       // Gasoline, Hybrid, EV, Diesel

    // Optional booleans (null ⇒ no preference)
    private Boolean sportyLook;              // true/false/null
    private Double  maxZeroToSixty;          // seconds (lower is better), null ⇒ ignore
    private Boolean tractionControl;         // true/false/null

    // === Setters used by CLI ===

    /** Accepts [min,max] (double[]) from CLI, casts to ints. Null ⇒ leave unset. */
    public void setYearRange(double[] range) {
        if (range != null) {
            minYear = (int) range[0];
            maxYear = (int) range[1];
        }
    }

    /** Accepts [min,max] (double[]) from CLI, casts to ints. Null ⇒ leave unset. */
    public void setPriceRange(double[] range) {
        if (range != null) {
            minPrice = (int) range[0];
            maxPrice = (int) range[1];
        }
    }

    // Simple value/setters. Passing null keeps "no preference" semantics.
    public void setMpgTarget(Double mpg)                 { this.mpgTarget = mpg; }
    public void setPreferredBrands(Set<String> brands)   { this.preferredBrands = brands; }
    public void setBodyTypes(Set<String> bodies)         { this.bodyTypes = bodies; }
    public void setFuelTypes(Set<String> fuels)          { this.fuelTypes = fuels; }
    public void setSportyLook(Boolean b)                 { this.sportyLook = b; }
    public void setMaxZeroToSixty(Double d)              { this.maxZeroToSixty = d; }
    public void setTractionControl(Boolean b)            { this.tractionControl = b; }

    // === Getters used by Scorer ===
    public Integer getMinYear()        { return minYear; }
    public Integer getMaxYear()        { return maxYear; }
    public Integer getMinPrice()       { return minPrice; }
    public Integer getMaxPrice()       { return maxPrice; }
    public Double  getMpgTarget()      { return mpgTarget; }
    public Set<String> getBodyTypes()  { return bodyTypes; }
    public Set<String> getPreferredBrands() { return preferredBrands; }
    public Set<String> getFuelTypes()  { return fuelTypes; }
    public Boolean getSportyLook()     { return sportyLook; }
    public Double  getMaxZeroToSixty() { return maxZeroToSixty; }
    public Boolean getTractionControl(){ return tractionControl; }

    // === Utility helpers ===

    /**
     * Parse comma-separated values into a Set<String>.
     * - Leading/trailing spaces are trimmed.
     * - Empty or null input yields an empty set (interpreted as "no constraint").
     * - Case is preserved; Scorer should decide whether to match case-sensitively.
     */
    public static Set<String> parseList(String input) {
        Set<String> out = new HashSet<>();
        if (input == null) return out; // treat as skipped
        for (String part : input.split(",")) {
            String s = part.trim();
            if (!s.isEmpty()) out.add(s);
        }
        return out;
    }

    // flags for Scorer
    public boolean hasBrandFilter() { return !preferredBrands.isEmpty(); }
    public boolean hasBodyFilter()  { return !bodyTypes.isEmpty(); }
    public boolean hasFuelFilter()  { return !fuelTypes.isEmpty(); }
}