package loyola.hersick.main;
import java.util.*;

public class Inventory {
    public static List<Car> build() {
        List<Car> L = new ArrayList<>();
        L.add(new Car("Toyota", "Camry", 2022, "Sedan", false, 7.6, 28, 39, "Gasoline", "FWD", 28000, 87, true));
        L.add(new Car("BMW", "M3", 2021, "Sedan", true, 3.9, 16, 23, "Gasoline", "RWD", 72000, 70, true));
        // ... (rest of ~20 cars)
        L.add(new Car("Ford", "F-150", 2023, "Truck", false, 6.5, 20, 26, "Gasoline", "RWD", 40000, 80, true));
        L.add(new Car("Honda", "Civic", 2020, "Sedan", false, 8.2, 30, 38, "Gasoline", "FWD", 22000, 85, false));
        L.add(new Car("Tesla", "Model 3", 2022, "Sedan", true, 4.1, 130, 120, "EV", "RWD", 45000, 90, true));
        L.add(new Car("Chevrolet", "Tahoe", 2021, "SUV", false, 7.0, 16, 20, "Gasoline", "AWD", 50000, 75, true));
        L.add(new Car("Audi", "A4", 2022, "Sedan", true, 5.6, 24, 31, "Gasoline", "AWD", 40000, 80, true));
        L.add(new Car("Subaru", "Outback", 2023, "Wagon", false, 8.5, 26, 33, "Gasoline", "AWD", 35000, 88, true));
        L.add(new Car("Mercedes-Benz", "C-Class", 2021, "Sedan", true, 5.4, 22, 29, "Gasoline", "RWD", 42000, 78, true));
        L.add(new Car("Nissan", "Altima", 2020, "Sedan", false, 7.9, 28, 39, "Gasoline", "FWD", 24000, 82, false));
        L.add(new Car("Volkswagen", "Golf GTI", 2022, "Hatchback", true, 5.9, 25, 32, "Gasoline", "FWD", 30000, 76, false));
        L.add(new Car("Toyota", "Prius", 2022, "Hatchback", false, 9.8, 58, 53, "Hybrid", "FWD", 27000, 90, true));
        L.add(new Car("Toyota", "GR Supra", 2021, "Coupe", true, 3.9, 25, 31, "Gasoline", "RWD", 52000, 75, true));
        L.add(new Car("Toyota", "Highlander", 2022, "SUV", false, 7.2, 21, 29, "Gasoline", "AWD", 42000, 85, true));
        L.add(new Car("Honda", "Accord", 2021, "Sedan", false, 7.2, 30, 38, "Gasoline", "FWD", 27000, 87, true));
        L.add(new Car("Honda", "CR-V Hybrid", 2022, "SUV", false, 7.5, 40, 35, "Hybrid", "AWD", 34000, 88, true));
        L.add(new Car("Hyundai", "Elantra N", 2022, "Sedan", true, 5.1, 22, 31, "Gasoline", "FWD", 33000, 80, true));
        L.add(new Car("Hyundai", "Tucson", 2021, "SUV", false, 8.8, 26, 33, "Gasoline", "FWD", 26000, 84, true));
        L.add(new Car("Kia", "Telluride", 2022, "SUV", false, 7.1, 20, 26, "Gasoline", "AWD", 42000, 82, true));
        L.add(new Car("Kia", "Niro Hybrid", 2021, "Hatchback", false, 8.6, 51, 46, "Hybrid", "FWD", 26000, 84, true));
        L.add(new Car("Ford", "Mustang GT", 2021, "Coupe", true, 4.3, 15, 24, "Gasoline", "RWD", 42000, 70, true));
        L.add(new Car("Ford", "Escape Hybrid", 2022, "SUV", false, 7.7, 44, 37, "Hybrid", "FWD", 33000, 83, true));
        L.add(new Car("Chevrolet", "Silverado 1500", 2021, "Truck", false, 6.9, 16, 21, "Gasoline", "RWD", 41000, 75, true));
        L.add(new Car("Chevrolet", "Bolt EUV", 2022, "Hatchback", false, 6.7, 0, 0, "EV", "FWD", 32000, 77, true));
        L.add(new Car("Mazda", "CX-5", 2021, "SUV", false, 7.7, 24, 30, "Gasoline", "AWD", 31000, 86, true));
        L.add(new Car("Mazda", "Mazda3", 2022, "Sedan", false, 7.0, 28, 36, "Gasoline", "FWD", 26000, 85, true));
        L.add(new Car("BMW", "X3", 2021, "SUV", false, 6.0, 23, 29, "Gasoline", "AWD", 47000, 78, true));
        L.add(new Car("Audi", "Q5", 2021, "SUV", false, 5.7, 23, 28, "Gasoline", "AWD", 48000, 79, true));
        L.add(new Car("Subaru", "WRX", 2022, "Sedan", true, 5.5, 19, 26, "Gasoline", "AWD", 31000, 80, true));
        L.add(new Car("Jeep", "Wrangler", 2021, "SUV", true, 6.8, 17, 25, "Gasoline", "AWD", 38000, 70, true));
        L.add(new Car("Volvo", "XC60", 2022, "SUV", false, 6.4, 22, 28, "Gasoline", "AWD", 52000, 82, true));
        return L;
    }
}
        