package com.gioviok;

/**
 * The main class.
 */
public class App {

    public static void main(String[] args) {

        // If user passed --no-ansi, we disable ANSI colors.
        if (args.length > 0 && args[0].equals("--no-ansi")) {
            Simulator.ansiSwitchOff();
        }

        Simulator simulator = new Simulator();
        simulator.init()
                .nameBuilding("Bank")
                .addNewFloor("Ground floor", 0, 0)
                .addNewFloor("First floor", 3000, 3000)
                .addNewFloor("Second floor", 123, 6000)
                .addNewFloor("Third floor", 234, 9000)
                .addNewFloor("Fourth floor", 345, 12000)
                .addNewFloor("Vault", 567, -7000)
                .addNewRole("Customer", "Ground floor", "First floor")
                .addNewRole("Employee", "Ground floor", "First floor", "Second floor", "Third floor")
                .addNewRole("Security", "Ground floor", "First floor", "Second floor", "Third floor", "Vault")
                .addNewRole("Manager", "Ground floor", "First floor", "Second floor", "Third floor", "Fourth floor",
                        "Vault")
                .addModelPerson("Dayanand Portolese", 60)
                .addModelPerson("Sayres Saulsbery", 75)
                .addModelPerson("Milo Schop ", 80)
                .addModelPerson("Mario Rossi", 70)
                .addModelPerson("Busbee Jerkins ", 75);
        simulator.guiLoop();
    }
}
