package loyola.hersick.main;
public class Car {
    private final String brand;
    private final String model;
    private final int year;
    
    private final String bodyType; // Sedan, SUV, Coupe, Truck, Hatch
    private final boolean sportyLook;
    
    private final double zeroToSixty; // acceleration in seconds
    private final double mpgCity;
    private final double mpgHwy;
    
    private final String fuelType; // Gasoline, Diesel, Hybrid, EV
    private final String drivetrain; // FWD, RWD, AWD
    private final int priceUSD;
    private final int reliabilityScore; // 0-100
    private final boolean tractionControl;
    
    public Car(String brand, String model, int year, String bodyType, boolean sportyLook,
    double zeroToSixty, double mpgCity, double mpgHwy,
    String fuelType, String drivetrain, int priceUSD,
    int reliabilityScore, boolean tractionControl) {
    this.brand = brand;
    this.model = model;
    this.year = year;
    this.bodyType = bodyType;
    this.sportyLook = sportyLook;
    this.zeroToSixty = zeroToSixty;
    this.mpgCity = mpgCity;
    this.mpgHwy = mpgHwy;
    this.fuelType = fuelType;
    this.drivetrain = drivetrain;
    this.priceUSD = priceUSD;
    this.reliabilityScore = reliabilityScore;
    this.tractionControl = tractionControl;
    }
    
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getBodyType() { return bodyType; }
    public boolean isSportyLook() { return sportyLook; }
    public double getZeroToSixty() { return zeroToSixty; }
    public double getMpgCity() { return mpgCity; }
    public double getMpgHwy() { return mpgHwy; }
    public String getFuelType() { return fuelType; }
    public String getDrivetrain() { return drivetrain; }
    public int getPriceUSD() { return priceUSD; }
    public int getReliabilityScore() { return reliabilityScore; }
    public boolean hasTractionControl() { return tractionControl; }
    
    public double getAvgMpg() { return (mpgCity + mpgHwy) / 2.0; }
    
    @Override
    public String toString() {
    return String.format("%s %s %d (%s)", brand, model, year, bodyType);
    }
}