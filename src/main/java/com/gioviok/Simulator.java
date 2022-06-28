package com.gioviok;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * The Simulator class is the main class of the simulator.
 * It includes all the facade methods to the other classes.
 *
 * @see Building
 * @see Elevator
 * @see Floor
 * @see Key
 * @see Person
 */
public class Simulator {
    /**
     * The isDouble method checks if the given string is a double.
     *
     * @param o The object to check.
     * @return True if the object is a double, false otherwise.
     */
    public static boolean isDouble(Object o) {
        try {
            Double.parseDouble(o.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * The internal representation of the building.
     */
    private Building building;

    /**
     * The internal elevator of the building. It is just a reference to the elevator
     * of the building.
     * We store it to avoid extra calls to the building.
     */
    private Elevator elevator;

    /**
     * The internal catalog of the people. It is not the same as the people of the
     * building. It is not the same as the Person catalog.
     * It is used for the prototype design pattern.
     */
    private ArrayList<Person> people = new ArrayList<Person>();
    /**
     * The internal entryPoint timer
     */
    private long entryPoint = 0;

    /**
     * The internal exitPoint timer
     */
    private long exitPoint = 0;

    /**
     * The enum of editor mode.
     * Note: VIEW is deprecated.
     */
    public enum EditorMode {
        /** Adding an item */
        ADD,
        /** Editing an item */
        EDIT,
        /** Deleting an item */
        DELETE,
        /** Viewing an item */
        VIEW
    };

    /*
     * The ANSI codes for colors and styles.
     */
    /** The ANSI code to reset all styles */
    private static String ANSI_RESET = "\u001B[0m";
    /** The ANSI code to write in red */
    private static String ANSI_RED = "\u001B[31m";
    /** The ANSI code to write in green */
    private static String ANSI_GREEN = "\u001B[32m";
    /** The ANSI code to write in yellow */
    private static String ANSI_YELLOW = "\u001B[33m";
    /** The ANSI code to write in blue */
    private static String ANSI_BLUE = "\u001B[34m";
    /** The ANSI code to write in purple */
    private static String ANSI_PURPLE = "\u001B[35m";
    /** The ANSI code to write in cyan */
    private static String ANSI_CYAN = "\u001B[36m";
    /** The ANSI code to write in gray */
    private static String ANSI_GRAY = "\u001B[90m";
    /** The ANSI code to write blinking text */
    private static String ANSI_BLINK = "\u001B[5m";
    // The ANSI codes for background colors.
    /** The ANSI code to write with a green background */
    private static String ANSI_BACKGROUND_GREEN = "\u001B[42m";
    /** The ANSI code to write with a yellow background */
    private static String ANSI_BACKGROUND_YELLOW = "\u001B[43m";
    /** The ANSI code to write with a cyan background */
    private static String ANSI_BACKGROUND_CYAN = "\u001B[46m";
    /** The ANSI code to write with a gray background */
    private static String ANSI_BACKGROUND_GRAY = "\u001B[100m";
    /** The ANSI code to write with a brown background */
    private static String ANSI_BACKGROUND_BROWN = "\u001B[101m";

    /**
     * The ansiSwitchOff is used to switch off the ANSI codes, in case they are not
     * supported by the terminal.
     */
    public static void ansiSwitchOff() {
        Simulator.ANSI_RESET = "";
        Simulator.ANSI_RED = "";
        Simulator.ANSI_GREEN = "";
        Simulator.ANSI_YELLOW = "";
        Simulator.ANSI_BLUE = "";
        Simulator.ANSI_PURPLE = "";
        Simulator.ANSI_CYAN = "";
        Simulator.ANSI_BLINK = "";
        Simulator.ANSI_GRAY = "";
        Simulator.ANSI_BACKGROUND_GREEN = "";
        Simulator.ANSI_BACKGROUND_YELLOW = "";
        Simulator.ANSI_BACKGROUND_CYAN = "";
        Simulator.ANSI_BACKGROUND_GRAY = "";
        Simulator.ANSI_BACKGROUND_BROWN = "";
    }

    /**
     * The clear screen method. We use ANSI escape codes to clear the screen.
     */
    private static void clearScreen() {
        System.out.print(ANSI_RESET);
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * The printEditorHeader function prints the header of the editor.
     *
     * @param mode The mode of the editor.
     */
    private static void printEditorHeader(String mode) {
        // Clear the screen.
        clearScreen();
        // Print the header.
        System.out.println(ANSI_BLUE + "== " + mode + " editor ==");
    }

    /**
     * The startTimer method. We use System.currentTimeMillis() to get the current
     * time and store it in the entryPoint variable.
     *
     * @throws IllegalStateException if the timer is already started.
     */
    private void startTimer() throws IllegalStateException {
        if (entryPoint != 0) {
            throw new IllegalStateException("Timer already started");
        }
        entryPoint = System.currentTimeMillis();
    }

    /**
     * The stopTimer method. We use System.currentTimeMillis() to get the current
     * time and store it in the exitPoint variable.
     * We return the time difference between the entryPoint and the exitPoint.
     * We then reset the entryPoint to 0.
     *
     * @return the time difference between the entryPoint and the exitPoint.
     * @throws IllegalStateException if the timer is not started.
     */
    private long stopTimer() throws IllegalStateException {
        if (entryPoint == 0) {
            throw new IllegalStateException("Timer not started");
        }
        exitPoint = System.currentTimeMillis();
        long time = exitPoint - entryPoint;
        entryPoint = 0;
        return time;
    }

    /**
     * The nameBuilding method is the facade method to change the name of the
     * building.
     *
     * @param name The new name of the building.
     *             return this to allow chaining.
     * @return this, to allow concatenation.
     * @throws IllegalArgumentException if the name is null.
     */
    public Simulator nameBuilding(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        building.setName(name);
        return this;
    }

    /**
     * The getBuilding method is the facade method to get the building.
     *
     * @return The building.
     * @see Building
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * The addNewFloor method is the facade method to add a new floor to the
     * building.
     *
     * @param floor   The new floor name to add.
     * @param keyCode The key code of the floor.
     * @param height  The height of the floor.
     * @return this to allow chaining.
     * @throws IllegalArgumentException if the floor name is null.
     */
    public Simulator addNewFloor(String floor, int keyCode, int height) throws IllegalArgumentException {
        if (floor == null) {
            throw new IllegalArgumentException("Floor name cannot be null");
        }
        building.addFloor(new Floor(floor, keyCode, height));
        return this;
    }

    /**
     * The init method initializes the simulator's building.
     *
     * @param verbose True if we want to print the initialization time, false
     *                otherwise. Defaults to false.
     * @return this to allow chaining.
     */
    public Simulator init(boolean verbose) {
        // Start the timer.
        startTimer();
        // Clear the screen.
        clearScreen();
        // We initialize the simulation.
        building = new Building();
        elevator = building.getElevator();
        // Finished. Print the time if verbose is true.
        if (verbose)
            System.out.println(ANSI_GREEN + "Done!: (" + stopTimer() + " ms)" + ANSI_RESET);
        else {
            // We should still stop the timer, but we don't print the time.
            stopTimer();
        }
        return this;
    }

    /**
     * The init method initializes the simulator's building.
     * Defaults to not verbose.
     * 
     * @return this to allow chaining.
     */
    public Simulator init() {
        return init(false);
    }

    /**
     * The main loop of the simulator's GUI.
     */
    public void guiLoop() {
        // Init scanner.
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // print the menu.
            printHelpMenu();
            // We take the input.
            String input = scanner.nextLine();
            boolean ok = false;
            // Start the timer.
            startTimer();
            // We check the input.
            switch (input) {
                case "0":
                    // Quick view
                    ok = quickView();
                    break;
                case "1":
                    // Open the building editor.
                    ok = buildingEditor();
                    break;
                case "2":
                    // Open the floor editor.
                    ok = floorEditor();
                    break;
                case "3":
                    // Open the elevator editor.
                    ok = elevatorEditor();
                    break;
                case "4":
                    // Open the key editor.
                    ok = keyEditor();
                    break;
                case "5":
                    // Open the person catalog.
                    printPersonCatalog();
                    break;
                case "6":
                    // Open the person editor.
                    ok = personEditor();
                    break;
                case "7":
                    // Print the simulation status.
                    ok = simulationStatus();
                    break;
                case "8":
                    // Run elevator step.
                    try {
                        ok = building.getElevator().run();
                    } catch (Exception e) {
                        System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
                        ok = false;
                        break;
                    }
                    break;
                case "9":
                    // Open the action editor.
                    ok = actionEditor();
                    break;
                case "10":
                    // Quit the simulator.
                    scanner.close();
                    System.exit(0);
                default:
                    // Clear the screen.
                    clearScreen();
                    // Print menu
                    printHelpMenu();
                    // Invalid input.
                    System.out.println(ANSI_RED + "Invalid input." + ANSI_RESET);
                    break;
            }
            // If valid input, we print success.
            if (ok) {
                System.out.println(ANSI_GREEN + "Done! (" + stopTimer() + " ms)" + ANSI_RESET);
            }
            // If invalid input, we still have to stop the timer.
            else {
                stopTimer();
            }
            // Wait for the user to press enter.
            scanner.nextLine();
        }
    }

    /**
     * The quick view method prints the current status of the building.
     *
     * @return true if the operation was successful.
     */
    private boolean quickView() {
        // Open scanner
        Scanner scanner = new Scanner(System.in);
        boolean invalidInput = false;
        do {
            // Print header
            System.out.println(ANSI_BLUE + "== Quick view ==" + ANSI_RESET);
            // Print infos about the building.
            System.out.println(ANSI_BLUE + "Building name:      " + ANSI_PURPLE + building.getName() + ANSI_RESET);
            System.out.println(
                    ANSI_BLUE + "Number of floors:   " + ANSI_PURPLE + building.getFloors().size() + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Elevator status:    " + ANSI_PURPLE
                    + (elevator.getCurrentFloor() != null ? "valid" : "invalid") + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Elevator position:  " + ANSI_PURPLE
                    + (elevator.getCurrentFloor() != null ? elevator.getCurrentFloor().getName() : "invalid")
                    + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Elevator direction: " + ANSI_PURPLE
                    + (elevator.getCurrentMovingDirection() != null ? elevator.getCurrentMovingDirection().toString()
                            : "invalid")
                    + ANSI_RESET);
            System.out.println(
                    ANSI_BLUE + "Number of people:   " + ANSI_PURPLE + building.getPersons().size() + ANSI_RESET);
            System.out
                    .println(ANSI_BLUE + "Number of roles:    " + ANSI_PURPLE + building.getKeys().size() + ANSI_RESET);
            // Ask the user if he wants the graphical view.
            System.out.println((invalidInput ? ANSI_RED + "Invalid input, please try again: " : ANSI_BLUE)
                    + "Do you want to see the graphical view? (y/n)" + ANSI_RESET);
            // Get the input.
            String input = scanner.nextLine();
            // If input is no, we return.
            if (input.equalsIgnoreCase("n")) {
                return true;
            }
            // If input is yes, we print the graphical view.
            else if (input.equalsIgnoreCase("y")) {
                // Clear the screen.
                clearScreen();
                // If ansi was disabled, print a warning
                if (ANSI_BLUE == "") {
                    System.out.println("Warning: ANSI is disabled. The graphical view will not be displayed."); // No
                                                                                                                // point
                                                                                                                // printing
                                                                                                                // ANSI
                                                                                                                // in
                                                                                                                // this
                                                                                                                // message
                } else {
                    // Print the graphical view.
                    printGraphicalView();
                }
                // We return.
                return true;
            }
            // Else, the input is invalid
            else
                invalidInput = true;

        } while (true);
    }

    /**
     * The printGraphicalView method prints the graphical view of the building.
     *
     * Note: It is still a command line view. It is not intended to be anything
     * fancy.
     */
    private void printGraphicalView() {
        // First, we setup some variables.
        // We also account for the X people lenght text, since it may be longer than
        // floor names.
        int largestFloorNameLength = 0;
        // Start printing the floors. We sort them in order of distance, from highest to
        // lowest.
        ArrayList<Floor> floors = new ArrayList<Floor>(building.getFloors());
        Collections.sort(floors, new Comparator<Floor>() {
            @Override
            public int compare(Floor o1, Floor o2) {
                return o2.getDistance() - o1.getDistance();
            }
        });
        ArrayList<Integer> floorPeopleCount = new ArrayList<Integer>();
        for (Floor floor : floors) {
            // Compare the floor name length to the largest one.
            if (floor.getName().length() > largestFloorNameLength) {
                largestFloorNameLength = floor.getName().length();
            }
            // Count people on the floor.
            int count = 0;
            for (Person person : building.getPersons()) {
                if (person.getCurrentFloor() == floor) {
                    count++;
                }
            }
            // The text X people is long log10(count) + 7
            int peopleLength = (int) (Math.log10(count) + 7);
            if (peopleLength > largestFloorNameLength) {
                largestFloorNameLength = peopleLength;
            }
            floorPeopleCount.add(count);
        }

        // Print the building name.
        System.out.println(ANSI_BLUE + building.getName() + ANSI_RESET);
        for (int j = 0; j < floors.size(); j++) {
            Floor floor = floors.get(j);
            for (int i = 0; i < 5 + (j == (floors.size() - 1) ? 1 : 0); i++) { // To print the base
                // Print background.
                System.out.print((floor.getDistance() >= 0 ? ANSI_BACKGROUND_CYAN : ANSI_BACKGROUND_BROWN) + "   ");
                // Switch on the floor line.
                switch (i) {
                    case 0:
                    case 5:
                        // Print a gray line long longestFloorName + 8
                        System.out.print(ANSI_BACKGROUND_GRAY
                                + new String(new char[largestFloorNameLength + 8]).replace("\0", " ") + ANSI_RESET); // Neat
                                                                                                                     // trick
                                                                                                                     // to
                                                                                                                     // print
                                                                                                                     // a
                                                                                                                     // lot
                                                                                                                     // of
                                                                                                                     // spaces.
                        break;
                    case 1:
                    case 4:
                        // Print wall
                        System.out.print(ANSI_BACKGROUND_GRAY + " " + ANSI_RESET);
                        // If elevator is on this floor, print the elevator.
                        System.out.print((elevator.getCurrentFloor() == floor ? ANSI_BACKGROUND_YELLOW : ANSI_RESET)
                                + "   " + ANSI_RESET);
                        // Print long enough space
                        System.out.print(
                                new String(new char[largestFloorNameLength + 3]).replace("\0", " ") + ANSI_RESET);
                        // Print wall
                        System.out.print(
                                (floor == building.getEntranceFloor() ? ANSI_BACKGROUND_GREEN : ANSI_BACKGROUND_GRAY)
                                        + " " + ANSI_RESET);
                        break;
                    case 2:
                        // Print wall
                        System.out.print(ANSI_BACKGROUND_GRAY + " " + ANSI_RESET);
                        // If elevator is on this floor, print the elevator.
                        System.out.print((elevator.getCurrentFloor() == floor ? ANSI_BACKGROUND_YELLOW : ANSI_RESET)
                                + "   " + ANSI_RESET);
                        // Print space
                        System.out.print(" ");
                        // Initialize the string to print.
                        StringBuilder stringToPrint = new StringBuilder();
                        // We calculate how many whitespaces we need to print.
                        int whitespaces = largestFloorNameLength + 2 - floor.getName().length();
                        // We will approximate by defect on the left, by excess on the right.
                        for (int left = 0; left < whitespaces / 2; left++)
                            stringToPrint.append(" ");
                        stringToPrint.append(floor.getName());
                        for (int right = 0; right < whitespaces - whitespaces / 2; right++)
                            stringToPrint.append(" ");
                        // Print the string.
                        System.out.print(stringToPrint.toString());
                        // Print wall
                        System.out.print(
                                (floor == building.getEntranceFloor() ? ANSI_BACKGROUND_GREEN : ANSI_BACKGROUND_GRAY)
                                        + " " + ANSI_RESET);
                        break;
                    case 3:
                        // Print wall
                        System.out.print(ANSI_BACKGROUND_GRAY + " " + ANSI_RESET);
                        // If elevator is on this floor, print the elevator.
                        if (elevator.getCurrentFloor() == floor) {
                            // Init string to print
                            String stringToPrint2 = Integer.toString(elevator.numberOfPeople());
                            // Initialize the string builder.
                            StringBuilder stringBuilder = new StringBuilder();
                            // We calculate how many whitespaces we need to print. // We assume that the
                            // number of people is always less than 1000.
                            int whitespaces2 = 3 - stringToPrint2.length();
                            // We will approximate by defect on the left, by excess on the right.
                            for (int left = 0; left < whitespaces2 / 2; left++)
                                stringBuilder.append(" ");
                            stringBuilder.append(stringToPrint2);
                            for (int right = 0; right < whitespaces2 - whitespaces2 / 2; right++)
                                stringBuilder.append(" ");
                            // Print the string.
                            System.out.print(ANSI_BACKGROUND_YELLOW + stringBuilder.toString() + ANSI_RESET);

                        } else {
                            System.out.print(ANSI_RESET + "   " + ANSI_RESET);
                        }
                        // Print space
                        System.out.print(" ");
                        // Init string to print.
                        String text = floorPeopleCount.get(j)
                                - (elevator.getCurrentFloor() == floor ? elevator.numberOfPeople() : 0) + " people";
                        // Init stringBuilder to print.
                        StringBuilder stringBuilder = new StringBuilder();
                        // We calculate how many whitespaces we need to print.
                        int whitespaces2 = largestFloorNameLength + 2 - text.length();
                        // We will approximate by defect on the left, by excess on the right.
                        for (int left = 0; left < whitespaces2 / 2; left++)
                            stringBuilder.append(" ");
                        stringBuilder.append(text);
                        for (int right = 0; right < whitespaces2 - whitespaces2 / 2; right++)
                            stringBuilder.append(" ");
                        // Print the string.
                        System.out.print(stringBuilder.toString());
                        // Print wall
                        System.out.print(
                                (floor == building.getEntranceFloor() ? ANSI_BACKGROUND_GREEN : ANSI_BACKGROUND_GRAY)
                                        + " " + ANSI_RESET);
                }
                // Print background and newline.
                System.out.println(
                        (floor.getDistance() >= 0 ? ANSI_BACKGROUND_CYAN : ANSI_BACKGROUND_BROWN) + "   " + ANSI_RESET);
            }
        }
    }

    /**
     * The action editor contains the menu used to execute simulation-specific
     * actions.
     *
     * @return True if the action was succesfull, false otherwise.
     */
    private boolean actionEditor() {
        // Init scanner, cursor and action variable
        Scanner scanner = new Scanner(System.in);
        int cursor = 0;
        boolean invalidInput = false;
        // Loop until we get a valid input.
        while (true) {
            // Print the header.
            printEditorHeader("Action editor");
            // Print the menu.
            System.out.println(ANSI_GREEN + "1. Register Person from catalog." + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "2. Execute person action." + ANSI_RESET);
            System.out.println(ANSI_RED + "3. Remove key from person." + ANSI_RESET);
            System.out.println(ANSI_RED + "4. Remove person from building." + ANSI_RESET);
            System.out.println(ANSI_CYAN + "5. Back" + ANSI_RESET);
            System.out
                    .print((invalidInput ? ANSI_RED + "Invalid input. Try again: " : ANSI_BLUE + "Enter your choice: ")
                            + ANSI_RESET);
            // Take the input.
            String input = scanner.nextLine();
            // Input must be 0-5.
            if (!input.matches("[0-5]")) {
                invalidInput = true;
                continue;
            }
            // We set the cursor to the input.
            cursor = Integer.parseInt(input);
            // Reset invalid input
            invalidInput = false;
            // Depending on the cursor, we will display the corresponding menu.
            switch (cursor) {
                case 1:
                    while (true) {
                        // Print the header.
                        printEditorHeader("Action editor");
                        // Here, we print a list of people in the catalogue. Then we ask the user to
                        // choose one, which will be added.
                        int i = 0;
                        for (; i < people.size(); i++) {
                            System.out.println(ANSI_GREEN + (i) + ". " + people.get(i).getName() + ANSI_RESET);
                        }
                        System.out.println(ANSI_RED + (i) + ". Back" + ANSI_RESET);
                        System.out.print(
                                (invalidInput ? ANSI_RED + "Invalid input. Try again: " : ANSI_BLUE + "Select person: ")
                                        + ANSI_RESET);
                        // Take the input.
                        input = scanner.nextLine();
                        // Input must be an integer.
                        if (!input.matches("[0-9]+")) {
                            invalidInput = true;
                            continue;
                        }
                        // Parse the input.
                        int personIndex = Integer.parseInt(input);
                        // Person index must be in the range of the catalogue.
                        if (personIndex == people.size()) {
                            break;
                        }
                        if (personIndex > people.size()) {
                            invalidInput = true;
                            continue;
                        }
                        Key key = null;
                        // Loop to get the key.
                        while (true) {
                            // Print the header.
                            printEditorHeader("Action editor");
                            // Here, we print a list of keys in the catalogue. Then we ask the user to
                            // choose one, which will be added.

                            System.out.println(ANSI_GREEN + "0. No key." + ANSI_RESET);
                            for (int j = 0; j < building.getKeys().size(); j++) {
                                System.out.println(
                                        ANSI_GREEN + (j + 1) + ". " + building.getKeys().get(j).getName() + ANSI_RESET);
                            }
                            System.out.println(ANSI_RED + (building.getKeys().size() + 1) + ". Back" + ANSI_RESET);
                            System.out.print((invalidInput ? ANSI_RED + "Invalid input. Try again: "
                                    : ANSI_BLUE + "Select key to give: ") + ANSI_RESET);
                            // Take the input.
                            input = scanner.nextLine();
                            // Input must be an integer.
                            if (!input.matches("[0-9]+")) {
                                invalidInput = true;
                                continue;
                            }
                            // Parse the input.
                            int keyIndex = Integer.parseInt(input);
                            // Key index must be in the range of the catalogue.
                            if (keyIndex == building.getKeys().size() + 1) {
                                cursor = -1; // To signal not to register the person
                                break;
                            }
                            if (keyIndex != 0) {
                                // Key index must be in the range of the catalogue.
                                if (keyIndex > building.getKeys().size()) {
                                    invalidInput = true;
                                    continue;
                                }
                                key = building.getKeys().get(keyIndex - 1).clone();
                            }
                            break;
                        }
                        // We register the person to the building.
                        try {
                            if (cursor != -1)
                                building.registerPerson(people.get(personIndex).clone(),
                                        (key == null ? null : key.getName()));
                        } catch (IllegalStateException e) {
                            // Print the header.
                            printEditorHeader("Action editor");
                            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
                            // We wait for the user to press enter.
                            scanner.nextLine();
                            cursor = -1; // To return to action editor.
                            break;
                        }
                        break;
                    }
                    cursor = -1; // To return to action editor.
                    break;
                case 2:
                    // Execute person action.
                    while (true) {
                        // Print the header.
                        printEditorHeader("Action editor");
                        // Here, we print a list of people in the building.
                        int i = 0;
                        for (; i < building.getPersons().size(); i++) {
                            System.out.println(
                                    ANSI_GREEN + (i) + ". " + building.getPersons().get(i).getName() + ANSI_RESET);
                        }
                        System.out.println(ANSI_RED + (i) + ". Back" + ANSI_RESET);
                        System.out.print(
                                (invalidInput ? ANSI_RED + "Invalid input. Try again: " : ANSI_BLUE + "Select person: ")
                                        + ANSI_RESET);
                        // Take the input.
                        input = scanner.nextLine();
                        // Input must be an integer.
                        if (!input.matches("[0-9]+")) {
                            invalidInput = true;
                            continue;
                        }
                        // Parse the input.
                        int personIndex = Integer.parseInt(input);
                        // Person index must be in the range of the catalogue.
                        if (personIndex == building.getPersons().size()) {
                            break;
                        }
                        if (personIndex > building.getPersons().size()) {
                            invalidInput = true;
                            continue;
                        }
                        Person person = building.getPersons().get(personIndex);
                        // Loop to get action
                        boolean breakOut = false;
                        while (true) {
                            // Print the header.
                            printEditorHeader("Action editor");
                            // Here, we print a list of actions. Then we ask the user what to do. Note that
                            // some actions are only available for certain people,
                            // depending on the status of the person.
                            // The call elevator ride is only available for people who are in the elevator.
                            // The same goes for the board elevator.
                            boolean isInElevator = building.getElevator().hasPerson(person);
                            System.out.println(ANSI_BLUE + "Person " + ANSI_YELLOW + person.getName() + ANSI_BLUE
                                    + " is " + (isInElevator ? "" : "not ") + "in the elevator." + ANSI_RESET);
                            System.out.println(ANSI_BLUE + "Currently, it is on " + ANSI_PURPLE
                                    + person.getCurrentFloor().getName() + ANSI_BLUE + " floor." + ANSI_RESET);
                            System.out.println(ANSI_BLUE + "What should " + ANSI_YELLOW + person.getName() + ANSI_BLUE
                                    + " do?" + ANSI_RESET);
                            System.out.println(
                                    (isInElevator ? ANSI_GRAY : ANSI_GREEN) + "0. Call elevator ride." + ANSI_RESET
                                            + "\n" +
                                            (isInElevator ? ANSI_GRAY : ANSI_GREEN) + "1. Board elevator." + ANSI_RESET
                                            + "\n" +
                                            (isInElevator ? ANSI_GREEN : ANSI_GRAY) + "2. Leave elevator." + ANSI_RESET
                                            + "\n" +
                                            (isInElevator ? ANSI_GREEN : ANSI_GRAY) + "3. Go to floor." + ANSI_RESET
                                            + "\n");
                            System.out.println(ANSI_RED + (4) + ". Back" + ANSI_RESET);
                            System.out.print((invalidInput ? ANSI_RED + "Invalid input. Try again: "
                                    : ANSI_BLUE + "Select action: ") + ANSI_RESET);
                            // Take the input.
                            input = scanner.nextLine();
                            // Input must be an integer.
                            if (!input.matches("[0-4]")) {
                                invalidInput = true;
                                continue;
                            }
                            // Parse the input.
                            cursor = Integer.parseInt(input);
                            // Switch on the input.
                            switch (cursor) {
                                case 0:
                                    // Call elevator ride.
                                    try {
                                        person.callElevatorRide();
                                    } catch (Exception e) {
                                        // Print the header.
                                        printEditorHeader("Action editor");
                                        System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
                                        // We wait for the user to press enter.
                                        scanner.nextLine();
                                        break;
                                    }
                                    break;
                                case 1:
                                    // Board elevator.
                                    try {
                                        building.getElevator().board(person);
                                    } catch (Exception e) {
                                        // Print the header.
                                        printEditorHeader("Action editor");
                                        System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
                                        // We wait for the user to press enter.
                                        scanner.nextLine();
                                        break;
                                    }
                                    break;
                                case 2:
                                    // Leave elevator.
                                    try {
                                        person.disembark();
                                    } catch (Exception e) {
                                        // Print the header.
                                        printEditorHeader("Action editor");
                                        System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
                                        // We wait for the user to press enter.
                                        scanner.nextLine();
                                        break;
                                    }
                                    break;
                                case 3:
                                    // Go to floor.
                                    // Print the header.
                                    while (true) {
                                        printEditorHeader("Action editor");
                                        // Here, we print a list of floors. Then we ask the user what to do.
                                        int j = 0;
                                        for (; j < building.getFloors().size(); j++) {
                                            System.out.println(ANSI_GREEN + (j) + ". "
                                                    + building.getFloors().get(j).getName() + ANSI_RESET);
                                        }
                                        System.out.println(ANSI_RED + (j) + ". Back" + ANSI_RESET);
                                        System.out.print((invalidInput ? ANSI_RED + "Invalid input. Try again: "
                                                : ANSI_BLUE + "Select floor: ") + ANSI_RESET);
                                        // Take the input.
                                        input = scanner.nextLine();
                                        // Input must be an integer.
                                        if (!input.matches("[0-9]+")) {
                                            invalidInput = true;
                                            continue;
                                        }
                                        // Parse the input.
                                        int floorIndex = Integer.parseInt(input);
                                        // Floor index must be in the range of the building's floors.
                                        if (floorIndex == building.getFloors().size()) {
                                            break;
                                        }
                                        if (floorIndex > building.getFloors().size()) {
                                            invalidInput = true;
                                            continue;
                                        }
                                        try {
                                            person.chooseFloor(building.getFloors().get(floorIndex));
                                        } catch (Exception e) {
                                            // Print the header.
                                            printEditorHeader("Action editor");
                                            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
                                            // We wait for the user to press enter.
                                            scanner.nextLine();
                                            breakOut = true;
                                            break;
                                        }
                                        break;
                                    }
                                    break;
                                case 4:
                                    // Back.
                                    breakOut = true;
                                    break;
                            }
                            if (breakOut)
                                break;
                        }
                    }
                    break;
                case 3:
                    // Remove key from person in the building.
                    while (true) {
                        // Print the header.
                        printEditorHeader("Action editor");
                        // Here, we print a list of people. Then we ask the user what to do.
                        int j = 0;
                        for (; j < building.getPersons().size(); j++) {
                            System.out.println(ANSI_GREEN + (j) + ". " + building.getPersons().get(j).getName()
                                    + ANSI_BLUE + "\t\tKey: " + ANSI_PURPLE +
                                    (building.getPersons().get(j).getKey() == null ? "null"
                                            : building.getPersons().get(j).getKey().getName())
                                    + ANSI_RESET);
                        }
                        System.out.println(ANSI_RED + (j) + ". Back" + ANSI_RESET);
                        System.out.print(
                                (invalidInput ? ANSI_RED + "Invalid input. Try again: " : ANSI_BLUE + "Select person: ")
                                        + ANSI_RESET);
                        // Take the input.
                        input = scanner.nextLine();
                        // Input must be an integer.
                        if (!input.matches("[0-9]+")) {
                            invalidInput = true;
                            continue;
                        }
                        // Parse the input.
                        int personIndex = Integer.parseInt(input);
                        // Person index must be in the range of the building's people.
                        if (personIndex == building.getPersons().size()) {
                            break;
                        }
                        if (personIndex > building.getPersons().size()) {
                            invalidInput = true;
                            continue;
                        }
                        // Get the person.
                        Person person = building.getPersons().get(personIndex);
                        // If the person doesn't already have a key, we print an error.
                        if (person.getKey() == null) {
                            // Print the header.
                            printEditorHeader("Action editor");
                            System.out.println(ANSI_RED + "Error: " + person.getName() + " doesn't have a key already!"
                                    + ANSI_RESET);
                            // We wait for the user to press enter.
                            scanner.nextLine();
                            continue;
                        }
                        do {
                            // Print the header.
                            printEditorHeader("Action editor");
                            // We ask confirmation.
                            System.out.println(ANSI_BLUE + "Are you sure you want to remove the key " + ANSI_YELLOW
                                    + person.getKey() + ANSI_BLUE + " from " + ANSI_PURPLE + person.getName()
                                    + ANSI_BLUE + "?" + ANSI_RESET);
                            System.out.println(ANSI_BLUE + "Y: yes" + ANSI_RESET);
                            System.out.println(ANSI_RED + "N: no" + ANSI_RESET);
                            System.out.print(ANSI_BLUE + ": " + ANSI_RESET);
                            input = scanner.nextLine();
                            if (input.equalsIgnoreCase("Y")) {
                                person.setKey(null);
                                break;
                            }
                            if (input.equals("n") || input.equals("N")) {
                                break;
                            }
                        } while (true);
                        break;
                    }
                    break;
                case 4:
                    // Remove person from the building.
                    while (true) {
                        // Print the header.
                        printEditorHeader("Action editor");
                        // Here, we print a list of people. Then we ask the user what to do.
                        int j = 0;
                        for (; j < building.getPersons().size(); j++) {
                            System.out.println(ANSI_GREEN + (j) + ". " + building.getPersons().get(j).getName()
                                    + ANSI_BLUE + "\t\tKey: " + ANSI_PURPLE +
                                    (building.getPersons().get(j).getKey() == null ? "null"
                                            : building.getPersons().get(j).getKey().getName())
                                    + ANSI_RESET);
                        }
                        System.out.println(ANSI_RED + (j) + ". Back" + ANSI_RESET);
                        System.out.print(
                                (invalidInput ? ANSI_RED + "Invalid input. Try again: " : ANSI_BLUE + "Select person: ")
                                        + ANSI_RESET);
                        // Take the input.
                        input = scanner.nextLine();
                        // Input must be an integer.
                        if (!input.matches("[0-9]+")) {
                            invalidInput = true;
                            continue;
                        }
                        // Parse the input.
                        int personIndex = Integer.parseInt(input);
                        // Person index must be in the range of the building's people.
                        if (personIndex == building.getPersons().size()) {
                            break;
                        }
                        if (personIndex > building.getPersons().size()) {
                            invalidInput = true;
                            continue;
                        }
                        // Get the person.
                        Person person = building.getPersons().get(personIndex);
                        // If the person doesn't already have a key, we print an error.

                        do {
                            // Print the header.
                            printEditorHeader("Action editor");
                            // We ask confirmation.
                            System.out.println(ANSI_BLUE + "Are you sure you want to delete " + ANSI_PURPLE
                                    + person.getKey() + ANSI_BLUE + " from " + ANSI_YELLOW + getBuilding().getName()
                                    + ANSI_BLUE + "?" + ANSI_RESET);
                            System.out.println(ANSI_BLUE + "Y: yes" + ANSI_RESET);
                            System.out.println(ANSI_RED + "N: no" + ANSI_RESET);
                            System.out.print(ANSI_BLUE + ": " + ANSI_RESET);
                            input = scanner.nextLine();
                            if (input.equalsIgnoreCase("Y")) {
                                // If the person was in the elevator, remove it from the elevator.
                                if (building.getElevator().hasPerson(person)) {
                                    building.getElevator().disembark(person);
                                }
                                // Remove the person from the building.
                                building.getPersons().remove(person);
                                break;
                            }
                            if (input.equals("n") || input.equals("N")) {
                                break;
                            }
                        } while (true);
                        break;
                    }
                case 5:
                    // Back.
                    return true;
                default:
                    // Invalid input.
                    invalidInput = true;
                    break;
            }

        }
    }

    /**
     * The simulation status function prints a quick overview of the status of the
     * simulation.
     * This includes infos such as the status of the internal building and the
     * number of registered people.
     * 
     * @return True
     */
    private boolean simulationStatus() {
        // Clear the screen.
        clearScreen();
        // Print the simulation status.
        System.out.println(ANSI_PURPLE + "== Simulation status ==" + ANSI_RESET);
        // Print the building infos.
        System.out.println(ANSI_BLUE + "The building " + ANSI_RESET + ANSI_PURPLE + building.getName()
                + ANSI_RESET + ANSI_BLUE + " has " + ANSI_RESET
                + ((building.numberOfFloors() == 0) ? ANSI_RED : ANSI_GREEN) + building.numberOfFloors()
                + ANSI_RESET + ANSI_BLUE + " floors and " + ANSI_RESET
                + ((building.numberOfPeople() == 0) ? ANSI_RED : ANSI_GREEN) + building.numberOfPeople() + ANSI_RESET
                + ANSI_BLUE + " people inside." + ANSI_RESET);
        // Print the elevator infos.
        System.out.println(ANSI_BLUE + "The elevator is currently on floor " + ANSI_RESET
                + ((elevator.getCurrentFloor() == null) ? ANSI_RED : ANSI_GREEN) + elevator.getCurrentFloor()
                + ANSI_RESET + ANSI_BLUE + ", has " + ANSI_RESET
                + ((elevator.numberOfPeople() == 0) ? ANSI_RED : ANSI_GREEN) + elevator.numberOfPeople() + ANSI_RESET
                + ANSI_BLUE + " people inside,"
                + ANSI_RESET + ANSI_BLUE + " and it is " + ANSI_RESET + ANSI_PURPLE
                + elevator.getCurrentMovingDirection().toString() + ANSI_BLUE + "." + ANSI_RESET);
        // Print key infos.
        System.out.println(ANSI_BLUE + "Currently, there are " + ANSI_RESET + ANSI_GREEN + building.numberOfKeys()
                + ANSI_RESET + ANSI_BLUE + " keys registered to the building." + ANSI_RESET);
        // Print end of simulation status.
        System.out.println(ANSI_PURPLE + "== End of Simulation Status ==" + ANSI_RESET);
        // Return true.
        return true;
    }

    /**
     * The elevator editor is used to edit some parameters of the elevator of the
     * internal building class.
     *
     * @return True if the operation was successful, false otherwise.
     * @throws Error                 if it reaches an impossible state.
     * @throws IllegalStateException if the internal elevator is not yet
     *                               initializited.
     */
    private boolean elevatorEditor() throws Error, IllegalStateException {
        // If elevator is not initialized, we throw an error.
        if (elevator == null) {
            throw new IllegalStateException("Elevator not initialized.");
        }
        // Set up scanner.
        Scanner scanner = new Scanner(System.in);
        // Setup the cursor.
        int cursor = -1;
        // Loop.
        boolean invalidValue = false;
        while (true) {
            printEditorHeader("Elevator");
            // Print the elevator details.
            System.out.println(
                    ANSI_BLUE + "Current Floor: " + ANSI_RESET + ANSI_GREEN + elevator.getCurrentFloor() + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Current Direction: " + ANSI_RESET + ANSI_GREEN
                    + elevator.getCurrentMovingDirection().toString() + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Cabin weight: " + ANSI_RESET
                    + (cursor == 0 ? ANSI_BLINK + ANSI_YELLOW : ANSI_GREEN) + elevator.getWeight() + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Cabin Load Limit: " + ANSI_RESET
                    + (cursor == 1 ? ANSI_BLINK + ANSI_YELLOW : ANSI_GREEN) + +elevator.getMaxLoad() + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Cabin Current Load: " + ANSI_RESET
                    + (elevator.isOverloaded() ? ANSI_RED : ANSI_GREEN) + elevator.getCurrentLoad() + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Currently, there are " + ANSI_RESET
                    + ((elevator.numberOfPeople() == 0) ? ANSI_RED : ANSI_GREEN) + elevator.numberOfPeople()
                    + ANSI_RESET + ANSI_BLUE + " people in the elevator." + ANSI_RESET);
            System.out.println();
            // Print the options if user has yet to choose what to modify.
            if (cursor == -1) {
                System.out.println(ANSI_GREEN + "0. Change cabin weight" + ANSI_RESET);
                System.out.println(ANSI_GREEN + "1. Change cabin load limit" + ANSI_RESET);
                System.out.println(ANSI_RED + "2. Done" + ANSI_RESET);
            } else {
                if (invalidValue)
                    System.out.println(ANSI_RED + "Invalid input. Try again: " + ANSI_RESET);
                else
                    System.out.println(ANSI_BLUE + "Insert value: " + ANSI_RESET);
            }
            invalidValue = false;
            // Get input
            String input = "";
            // If cursor is -1, we ask to choose an option.
            if (cursor == -1) {
                input = scanner.nextLine();
                if (!input.matches("[0-2]")) {
                    invalidValue = true;
                    continue;
                }
                // Set cursor.
                cursor = Integer.parseInt(input);
                if (cursor == 2) {
                    return true;
                }
            }
            // Else, we are asking for a input.
            else {
                // Get input. It must be a number.
                input = scanner.nextLine();
                if (!Simulator.isDouble(input)) {
                    invalidValue = true;
                    continue;
                }
                // Set the appropriate value.
                switch (cursor) {
                    case 0:
                        elevator.setWeight(Double.parseDouble(input));
                        break;
                    case 1:
                        elevator.setMaxLoad(Double.parseDouble(input));
                        break;
                    default:
                        // We should never get here.
                        throw new Error("Invalid cursor value.");
                }
                // Reset cursor.
                cursor = -1;
            }
        }
        // We should never get here.
    }

    /**
     * The floor editor is useful to add, remove or edit floors from and to the
     * internal building.
     *
     * @return True if the operation was successful, false otherwise.
     * @throws Error is it reaches an impossible state.
     */
    private boolean floorEditor() throws Error {
        // Set up scanner.
        Scanner scanner = new Scanner(System.in);
        // Editor loop
        while (true) {
            // Ask user for input.
            String input = "";
            boolean matchesRegex = true;
            do {
                printEditorHeader("Floor");
                // Ask user if he wants to add, edit, delete or view a floor.
                System.out.println(ANSI_BLUE + "What do you want to do?" + ANSI_RESET);
                System.out.println(ANSI_GREEN + "0. Add a floor" + ANSI_RESET);
                System.out.println(ANSI_YELLOW + "1. Edit a floor" + ANSI_RESET);
                System.out.println(ANSI_RED + "2. Remove a floor" + ANSI_RESET);
                System.out.println(ANSI_CYAN + "3. Back" + ANSI_RESET);
                // Get input.
                if (matchesRegex)
                    System.out.print(ANSI_BLUE + "Input: " + ANSI_RESET);
                else
                    System.out.print(ANSI_RED + "Invalid input. Try again: " + ANSI_RESET);
                input = scanner.nextLine();
                matchesRegex = input.matches("[0-3]");
            } while (!matchesRegex);

            // Remember mode
            EditorMode mode;
            switch (input) {
                case "0":
                    mode = EditorMode.ADD;
                    break;
                case "1":
                    mode = EditorMode.EDIT;
                    break;
                case "2":
                    mode = EditorMode.DELETE;
                    break;
                case "3":
                    return true;
                default:
                    System.out.println(ANSI_RED + "Invalid input (" + input + ")." + ANSI_RESET);
                    // If we get here, something went wrong.
                    throw new Error("Invalid program state.");
            }

            Floor floor = null;
            String name = "";
            // If the mode isn't add, we need to ask who's the floor we want to
            // edit/show/delete.
            if (mode != EditorMode.ADD) {
                do {
                    printEditorHeader("Floor");
                    // Print the floor list.
                    int i = 0;
                    for (; i < building.getFloors().size(); i++) {
                        System.out.println(ANSI_BLUE + i + ": " + ANSI_RESET + ANSI_YELLOW
                                + building.getFloors().get(i).getName() + ANSI_RESET);
                    }
                    System.out.println(ANSI_BLUE + i + ": " + ANSI_RESET + ANSI_RED + "Back" + ANSI_RESET);
                    System.out.print(
                            ANSI_BLUE + "Input the number corresponding to the floor to " + mode.toString() + ": "
                                    + ANSI_RESET);
                    // Get input.
                    name = scanner.nextLine();
                } while (name.equals("") || !name.matches("[0-9]+")
                        || Integer.parseInt(name) > building.getFloors().size());
                // Get the person if the input isn't back.
                if (name.equals(Integer.toString(building.getFloors().size()))) {
                    continue;
                }
                floor = building.getFloors().get(Integer.parseInt(name)).clone();
            }

            // If add, we initialize a new person.
            if (mode == EditorMode.ADD) {
                floor = new Floor();
            }

            // Just for safety, we check if the floor is null.
            if (floor == null)
                throw new Error("Invalid program state.");

            // If mode is delete, we need to ask if the user really wants to delete the
            // floor.
            if (mode == EditorMode.DELETE) {
                printEditorHeader("Floor");
                // Print the floor delete confirmation.
                System.out.println(ANSI_BLUE + "Are you sure you want to delete " + ANSI_RESET + ANSI_YELLOW
                        + floor.getName() + ANSI_RESET + ANSI_BLUE + "?" + ANSI_RESET);
                System.out.println(ANSI_GREEN + "Y. Yes" + ANSI_RESET);
                System.out.println(ANSI_RED + "N. No" + ANSI_RESET);
                // Ask user for input.
                String input2 = "";
                do {
                    input2 = scanner.nextLine();
                } while (!input2.matches("[yn]"));

                if (input2.equalsIgnoreCase("y")) {
                    // Delete the floor.
                    building.getFloors().remove(floor);
                    printEditorHeader("Floor");
                    // Print the floor delete confirmation.
                    System.out.println(ANSI_BLUE + "Floor " + ANSI_RESET + ANSI_YELLOW + floor.toString() + ANSI_RESET
                            + ANSI_BLUE + " deleted." + ANSI_RESET);
                    // End of floor editor.
                    System.out.println(ANSI_BLUE + "== End of floor editor ==" + ANSI_RESET);
                }
                continue;
            }
            // Else, if we are in view mode, we just print the floor.
            else if (mode == EditorMode.VIEW) {
                printEditorHeader("Floor");
                // Print the floor name.
                System.out
                        .println(ANSI_BLUE + "Floor name: " + ANSI_RESET + ANSI_YELLOW + floor.getName() + ANSI_RESET);
                // Print the floor height.
                System.out.println(
                        ANSI_BLUE + "Floor height: " + ANSI_RESET + ANSI_YELLOW + floor.getDistance() + ANSI_RESET);
                // Print the floor keycode.
                System.out.println(
                        ANSI_BLUE + "Floor keycode: " + ANSI_RESET + ANSI_YELLOW + floor.getKey() + ANSI_RESET);
                // End of floor editor.
                System.out.println(ANSI_BLUE + "== End of floor editor ==" + ANSI_RESET);
                // Wait for user to press enter.
                scanner.nextLine();
                continue;
            }
            // Else, we are in edit or add mode.
            else {
                // Init cursor.
                int cursor = -1;
                boolean invalidInput = false;
                // Enter command loop.
                boolean exitEditor = false;
                while (!exitEditor) {
                    // Print the floor details.
                    // Depending on cursor, we ask for input in different ways.
                    String input2 = "";
                    printEditorHeader("Floor");
                    // Print the floor name.
                    System.out.println(ANSI_BLUE + "Floor name: "
                            + ((cursor == 0) ? ANSI_YELLOW + ANSI_BLINK : ANSI_GREEN) + floor.getName() + ANSI_RESET);
                    // Print the floor height.
                    System.out.println(
                            ANSI_BLUE + "Floor height: " + ((cursor == 1) ? ANSI_YELLOW + ANSI_BLINK : ANSI_GREEN)
                                    + floor.getDistance() + ANSI_RESET);
                    // Print the floor keycode.
                    System.out.println(ANSI_BLUE + "Floor keycode: "
                            + ((cursor == 2) ? ANSI_YELLOW + ANSI_BLINK : ANSI_GREEN) + floor.getKey() + ANSI_RESET);
                    if (cursor == -1) {
                        System.out.println(ANSI_GREEN + "0. Edit name" + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "1. Edit height" + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "2. Edit keycode" + ANSI_RESET);
                        System.out.println(ANSI_RED + "3. Done" + ANSI_RESET);
                        System.out.println(ANSI_RED + "4. Undo" + ANSI_RESET);
                    } else {
                        if (invalidInput)
                            System.out.print(ANSI_RED + "Invalid input. Try again:" + ANSI_RESET);
                        else
                            System.out.print(ANSI_BLUE + "Input: " + ANSI_RESET);
                    }

                    invalidInput = false;
                    input2 = scanner.nextLine();
                    if (cursor == -1 && !input2.matches("[0-4]")) {
                        invalidInput = true;
                        continue;
                    } else if (cursor == 0 && input2.equals("")) {
                        invalidInput = true;
                        continue;
                    }
                    switch (cursor) {
                        case -1:
                            // This means the user has just chosen an option.
                            switch (Integer.parseInt(input2)) {
                                case 0:
                                    cursor = 0;
                                    break;
                                case 1:
                                    cursor = 1;
                                    break;
                                case 2:
                                    cursor = 2;
                                    break;
                                case 3:
                                    // If mode is add, we add the floor.
                                    if (mode == EditorMode.ADD) {
                                        building.addFloor(floor);
                                    } else {
                                        // Else, we are in edit mode, so we update the floor.
                                        building.getFloors().get(Integer.parseInt(name))
                                                .setName(floor.getName())
                                                .setDistance(floor.getDistance())
                                                .setKey(floor.getKey());
                                    }
                                case 4:
                                    printEditorHeader("Floor");
                                    // Print "Saved!"
                                    System.out
                                            .println(ANSI_GREEN + ((input2 == "2") ? "Saved!" : "Done.") + ANSI_RESET);
                                    // End of person editor.
                                    System.out.println(ANSI_BLUE + "== End of floor editor ==" + ANSI_RESET);
                                    // Return true.
                                    exitEditor = true;
                                    continue;
                            }
                            break;
                        case 0:
                            // This means the user is editing the name.
                            floor.setName(input2);
                            // Reset cursor.
                            cursor = -1;
                            break;
                        case 1:
                            // This means the user is editing the height.
                            try {
                                floor.setDistance(Integer.parseInt(input2));
                            } catch (NumberFormatException e) {
                                invalidInput = true;
                                continue;
                            }
                            // Reset cursor.
                            cursor = -1;
                            break;
                        case 2:
                            // This means the user is editing the keycode.
                            try {
                                floor.setKey(Integer.parseInt(input2));
                            } catch (NumberFormatException e) {
                                invalidInput = true;
                                continue;
                            }
                            // Reset cursor.
                            cursor = -1;
                            break;
                        default:
                            throw new Error("Invalid cursor value (" + cursor + ")");
                    }
                }
            }
        }
        // Since we should always return with one of the above cases, we should never
        // reach this point.
    }

    /**
     * The person Editor contains methods used to add, modify or remove people from
     * and to the Person catalog.
     *
     * @return True if the operation was successful, false otherwise.
     * @throws Error If it reaches an impossible state.
     */
    private boolean personEditor() throws Error {
        // Set up scanner.
        Scanner scanner = new Scanner(System.in);
        // Editor loop
        while (true) {
            // Ask user for input.
            String input = "";
            boolean matchesRegex = true;
            do {
                printEditorHeader("Person");
                // Ask user if he wants to add, edit, delete or view a person.
                System.out.println(ANSI_BLUE + "What do you want to do?" + ANSI_RESET);
                System.out.println(ANSI_GREEN + "0. Add a person" + ANSI_RESET);
                System.out.println(ANSI_YELLOW + "1. Edit a person" + ANSI_RESET);
                System.out.println(ANSI_RED + "2. Delete a person" + ANSI_RESET);
                System.out.println(ANSI_CYAN + "3. Back" + ANSI_RESET);
                // Get input.
                if (matchesRegex)
                    System.out.print(ANSI_BLUE + "Input: " + ANSI_RESET);
                else
                    System.out.print(ANSI_RED + "Invalid input. Try again: " + ANSI_RESET);
                input = scanner.nextLine();
                matchesRegex = input.matches("[0-3]");
            } while (!matchesRegex);

            // Remember mode
            EditorMode mode;
            switch (input) {
                case "0":
                    mode = EditorMode.ADD;
                    break;
                case "1":
                    mode = EditorMode.EDIT;
                    break;
                case "2":
                    mode = EditorMode.DELETE;
                    break;
                case "3":
                    return true;
                default:
                    System.out.println(ANSI_RED + "Invalid input (" + input + ")." + ANSI_RESET);
                    // If we get here, something went wrong.
                    throw new Error("Invalid program state.");
            }

            Person person = null;
            String name = "";
            // If the mode isn't add, we need to ask who's the person we want to
            // edit/show/delete.
            if (mode != EditorMode.ADD) {
                do {
                    printEditorHeader("Person");
                    // Print the person list.
                    int i = 0;
                    for (; i < people.size(); i++) {
                        System.out.println(ANSI_BLUE + i + ": " + ANSI_RESET + ANSI_YELLOW + people.get(i).toString()
                                + ANSI_RESET);
                    }
                    System.out.println(ANSI_BLUE + i + ": " + ANSI_RESET + ANSI_RED + "Back" + ANSI_RESET);
                    System.out.print(ANSI_BLUE + "Input the number corresponding to the person to " + mode.toString()
                            + ": " + ANSI_RESET);
                    // Get input.
                    name = scanner.nextLine();
                } while (name.equals("") || !name.matches("[0-9]+") || Integer.parseInt(name) > people.size());
                // Get the person if the input isn't back.
                if (name.equals(Integer.toString(people.size()))) {
                    continue;
                }
                person = people.get(Integer.parseInt(name)).clone();
            }

            // If add, we initialize a new person.
            if (mode == EditorMode.ADD) {
                person = new Person();
            }

            // Just for safety, we check if the person is null.
            if (person == null)
                throw new Error("Invalid program state.");

            // If mode is delete, we need to ask if the user really wants to delete the
            // person.
            if (mode == EditorMode.DELETE) {
                printEditorHeader("Person");
                // Print the person delete confirmation.
                System.out.println(ANSI_BLUE + "Are you sure you want to delete " + ANSI_RESET + ANSI_YELLOW
                        + person.toString() + ANSI_RESET + ANSI_BLUE + "?" + ANSI_RESET);
                System.out.println(ANSI_GREEN + "Y. Yes" + ANSI_RESET);
                System.out.println(ANSI_RED + "N. No" + ANSI_RESET);
                // Ask user for input.
                String input2 = "";
                do {
                    input2 = scanner.nextLine();
                } while (!input2.matches("[yn]"));

                if (input2.equalsIgnoreCase("y")) {
                    // Delete the person.
                    people.remove(person);
                    printEditorHeader("Person");
                    // Print the person delete confirmation.
                    System.out.println(ANSI_BLUE + "Person " + ANSI_RESET + ANSI_YELLOW + person.toString() + ANSI_RESET
                            + ANSI_BLUE + " deleted." + ANSI_RESET);
                    // End of person editor.
                    System.out.println(ANSI_BLUE + "== End of person editor ==" + ANSI_RESET);
                }
                continue;
            }
            // Else, if we are in view mode, we just print the person.
            else if (mode == EditorMode.VIEW) {
                printEditorHeader("Person");
                // Print the person.
                System.out.println(ANSI_BLUE + "Person: " + ANSI_RESET + ANSI_YELLOW + person.toString() + ANSI_RESET);
                // End of person editor.
                System.out.println(ANSI_BLUE + "== End of person editor ==" + ANSI_RESET);
                // Wait for user to press enter.
                scanner.nextLine();
                continue;
            }
            // Else, we are in edit or add mode.
            else {
                // Init cursor.
                int cursor = -1;
                boolean invalidInput = false;
                // Enter command loop.
                boolean exitEditor = false;
                while (!exitEditor) {
                    // Print the person details.
                    // Depending on cursor, we ask for input in different ways.
                    String input2 = "";
                    printEditorHeader("Person");
                    // Print the person details.
                    System.out.println(ANSI_BLUE + "Name: " + ANSI_RESET
                            + ((cursor == 0) ? ANSI_YELLOW + ANSI_BLINK : ANSI_GREEN) + person.getName() + ANSI_RESET);
                    System.out.println(ANSI_BLUE + "Weight: " + ANSI_RESET
                            + ((cursor == 1) ? ANSI_YELLOW + ANSI_BLINK : ANSI_GREEN) + person.getWeight()
                            + ANSI_RESET);
                    if (cursor == -1) {
                        System.out.println(ANSI_GREEN + "0. Edit name" + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "1. Edit weight" + ANSI_RESET);
                        System.out.println(ANSI_RED + "2. Done" + ANSI_RESET);
                        System.out.println(ANSI_RED + "3. Undo" + ANSI_RESET);
                    } else {
                        if (invalidInput)
                            System.out.print(ANSI_RED + "Invalid input. Try again:" + ANSI_RESET);
                        else
                            System.out.print(ANSI_BLUE + "Input: " + ANSI_RESET);
                    }

                    invalidInput = false;
                    input2 = scanner.nextLine();
                    if (cursor == -1 && !input2.matches("[0-3]")) {
                        invalidInput = true;
                        continue;
                    } else if (cursor == 0 && input2.equals("")) {
                        invalidInput = true;
                        continue;
                    } else if (cursor == 1 && !input2.matches("[0-9]+")) {
                        invalidInput = true;
                        continue;
                    }
                    switch (cursor) {
                        case -1:
                            // This means the user has just chosen an option.
                            switch (Integer.parseInt(input2)) {
                                case 0:
                                    cursor = 0;
                                    break;
                                case 1:
                                    cursor = 1;
                                    break;
                                case 2:
                                    // If mode is add, we add the person.
                                    if (mode == EditorMode.ADD) {
                                        people.add(person);
                                    } else {
                                        // Else, we are in edit mode, so we update the person.
                                        people.get(Integer.parseInt(name)).setName(person.getName());
                                        people.get(Integer.parseInt(name)).setWeight(person.getWeight());
                                    }
                                case 3:
                                    printEditorHeader("Person");
                                    // Print "Saved!"
                                    System.out
                                            .println(ANSI_GREEN + ((input2 == "2") ? "Saved!" : "Done.") + ANSI_RESET);
                                    // End of person editor.
                                    System.out.println(ANSI_BLUE + "== End of person editor ==" + ANSI_RESET);
                                    // Return true.
                                    exitEditor = true;
                                    continue;
                            }
                            break;
                        case 0:
                            // This means the user is editing the name.
                            person.setName(input2);
                            // Reset cursor.
                            cursor = -1;
                            break;
                        case 1:
                            // This means the user is editing the weight.
                            try {
                                person.setWeight(Integer.parseInt(input2));
                            } catch (NumberFormatException e) {
                                continue;
                            }
                            // Reset cursor.
                            cursor = -1;
                            break;
                        default:
                            throw new Error("Invalid cursor value (" + cursor + ")");
                    }
                }
            }
        }
        // Since we should always return with one of the above cases, we should never
        // reach this point.
    }

    /**
     * The building editor is used to modify some variables of the internal
     * building.
     *
     * @return True if the operation was successful, false otherwise.
     * @throws Error                 If it reaches an impossible state.
     * @throws IllegalStateException It the building isn't initialized.
     */
    private boolean buildingEditor() throws Error, IllegalStateException {
        // If elevator is not initialized, we throw an error.
        if (building == null) {
            throw new IllegalStateException("Building not initialized.");
        }
        // Set up scanner.
        Scanner scanner = new Scanner(System.in);
        // Setup the cursor.
        int cursor = -1;
        // Loop.
        boolean invalidValue = false;
        while (true) {
            printEditorHeader("Building");
            // Print the elevator details.
            System.out.println(ANSI_BLUE + "Building name: " + ANSI_RESET
                    + (cursor == 0 ? ANSI_BLINK + ANSI_YELLOW : ANSI_GREEN) + building.getName() + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Entrance floor: " + ANSI_RESET + (cursor == 1 ? ANSI_BLINK : "")
                    + (building.getEntranceFloor() == null ? ANSI_RED + "null"
                            : ANSI_GREEN + building.getEntranceFloor().getName() + ANSI_RESET));
            System.out.println();
            // Print the options if user has yet to choose what to modify.
            if (cursor == -1) {
                System.out.println(ANSI_GREEN + "0. Change building name" + ANSI_RESET);
                System.out.println(ANSI_GREEN + "1. Change entrance floor" + ANSI_RESET);
                System.out.println(ANSI_RED + "2. Done" + ANSI_RESET);
            } else {
                if (invalidValue)
                    System.out.println(ANSI_RED + "Invalid input. Try again (\"undo\" to undo): " + ANSI_RESET);
                else
                    System.out.println(ANSI_BLUE + "Insert value (\"undo\" to undo): " + ANSI_RESET);
            }
            invalidValue = false;
            // Get input
            String input = "";
            // If cursor is -1, we ask to choose an option.
            if (cursor == -1) {
                input = scanner.nextLine();
                if (!input.matches("[0-2]")) {
                    invalidValue = true;
                    continue;
                }
                // Set cursor.
                cursor = Integer.parseInt(input);
                if (cursor == 2) {
                    return true;
                }
            }
            // Else, we are asking for a input.
            else {
                // Get input. It must be a string. If cursor is 1, we need a valid floor name.
                input = scanner.nextLine();
                // If we get undo, we reset the cursor and continue.
                if (input.equals("undo")) {
                    cursor = -1;
                    continue;
                }
                // Set the appropriate value.
                switch (cursor) {
                    case 0:
                        building.setName(input);
                        break;
                    case 1:
                        try {
                            building.setEntranceFloorNamed(input);
                        } catch (IllegalArgumentException e) {
                            invalidValue = true;
                            continue;
                        }
                        ;
                        break;
                    default:
                        // We should never get here.
                        throw new Error("Invalid cursor value.");
                }
                // Reset cursor.
                cursor = -1;
            }
        }
        // We should never get here.
    }

    /**
     * The printPersonCatalog function prints the internal catalog of people.
     */
    private void printPersonCatalog() {
        // Clear the screen.
        clearScreen();
        // Print the person catalog.
        System.out.println(ANSI_BLUE + "== Person catalog ==" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "There "
                + (people.size() == 1 ? "is" : "are") + " "
                + (people.size() == 0 ? ANSI_RED : ANSI_GREEN) + people.size() + ANSI_RESET + ANSI_BLUE + " "
                + (people.size() == 1 ? "person" : "people")
                + " in the catalog." + ANSI_RESET);
        // Print the people.
        for (Person person : people) {
            System.out.println(ANSI_GREEN + person.toString());
        }
        // End
        System.out.println(ANSI_BLUE + "== End of catalog ==" + ANSI_RESET);
    }

    /**
     * The printHelpMenu function prints the helping menu for the simulation class.
     */
    private void printHelpMenu() {
        // Clear the screen.
        clearScreen();
        // Print the help menu.
        System.out.println(ANSI_BLUE + "\n == Help menu ==" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "0. Quick view.           " + ANSI_RESET + ANSI_BLINK + "->" + ANSI_GREEN
                + " Open a quick recap of the simulation's status." + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "1. Building editor.      " + ANSI_RESET + ANSI_BLINK + "->" + ANSI_GREEN
                + " Open the building editor." + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "2. Floor editor.         " + ANSI_RESET + ANSI_BLINK + "->" + ANSI_GREEN
                + " Open the floor editor." + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "3. Elevator editor.      " + ANSI_RESET + ANSI_BLINK + "->" + ANSI_GREEN
                + " Open the elevator editor." + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "4. Key editor.           " + ANSI_RESET + ANSI_BLINK + "->" + ANSI_GREEN
                + " Open the key editor." + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "5. Person catalog.       " + ANSI_RESET + ANSI_BLINK + "->" + ANSI_GREEN
                + " Open the person catalog." + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "6. Person editor.        " + ANSI_RESET + ANSI_BLINK + "->" + ANSI_GREEN
                + " Open the person editor." + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "7. Simulation status.    " + ANSI_RESET + ANSI_BLINK + "->" + ANSI_GREEN
                + " Print the simulation status. This includes people in the bulding and their position." + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "8. Run simulation step.  " + ANSI_RESET + ANSI_BLINK + "->" + ANSI_GREEN
                + " Run simulation step. This includes the elevator moving." + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "9. Run action.           " + ANSI_RESET + ANSI_BLINK + "->" + ANSI_GREEN
                + " Run an action, like adding a person, boarding, unboarding or calling a ride." + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "10. Close the program.   " + ANSI_RESET + ANSI_BLINK + "->" + ANSI_GREEN
                + " Stop the simulation and close the program." + ANSI_RESET);
        System.out.println(ANSI_BLUE + "== End of help menu ==   " + ANSI_RESET);
    }

    /**
     * The keyEditor is used to add, remove or modify keys from or to the internal
     * building.
     *
     * @return True if the action was successful, false otherwise.
     * @throws Error if it reaches an impossible state.
     */
    private boolean keyEditor() throws Error {
        // Set up scanner.
        Scanner scanner = new Scanner(System.in);
        // Editor loop
        while (true) {
            // Ask user for input.
            String input = "";
            boolean matchesRegex = true;
            do {
                printEditorHeader("Key");
                // Ask user if he wants to add, edit, delete or view a key.
                System.out.println(ANSI_BLUE + "What do you want to do?" + ANSI_RESET);
                System.out.println(ANSI_GREEN + "0. Add a key" + ANSI_RESET);
                System.out.println(ANSI_YELLOW + "1. Edit a key" + ANSI_RESET);
                System.out.println(ANSI_RED + "2. Delete a key" + ANSI_RESET);
                System.out.println(ANSI_CYAN + "3. Back" + ANSI_RESET);
                // Get input.
                if (matchesRegex)
                    System.out.print(ANSI_BLUE + "Input: " + ANSI_RESET);
                else
                    System.out.print(ANSI_RED + "Invalid input. Try again: " + ANSI_RESET);
                input = scanner.nextLine();
                matchesRegex = input.matches("[0-3]");
            } while (!matchesRegex);

            // Remember mode
            EditorMode mode;
            switch (input) {
                case "0":
                    mode = EditorMode.ADD;
                    break;
                case "1":
                    mode = EditorMode.EDIT;
                    break;
                case "2":
                    mode = EditorMode.DELETE;
                    break;
                case "3":
                    return true;
                default:
                    System.out.println(ANSI_RED + "Invalid input (" + input + ")." + ANSI_RESET);
                    // If we get here, something went wrong.
                    throw new Error("Invalid program state.");
            }

            Key key = null;
            String name = "";
            // If the mode isn't add, we need to ask who's the key we want to
            // edit/show/delete.
            if (mode != EditorMode.ADD) {
                do {
                    printEditorHeader("Key");
                    // Print the key list.
                    int i = 0;
                    for (; i < building.getKeys().size(); i++) {
                        System.out.println(ANSI_BLUE + i + ": " + ANSI_RESET + ANSI_YELLOW
                                + building.getKeys().get(i).getName() + ANSI_RESET);
                    }
                    System.out.println(ANSI_BLUE + i + ": " + ANSI_RESET + ANSI_RED + "Back" + ANSI_RESET);
                    System.out.print(ANSI_BLUE + "Input the number corresponding to the key to " + mode.toString()
                            + ": " + ANSI_RESET);
                    // Get input.
                    name = scanner.nextLine();
                } while (name.equals("") || !name.matches("[0-9]+")
                        || Integer.parseInt(name) > building.getKeys().size());
                // Get the key if the input isn't back.
                if (name.equals(Integer.toString(building.getKeys().size()))) {
                    continue;
                }
                key = building.getKeys().get(Integer.parseInt(name)).clone();
            }

            // If add, we initialize a new key.
            if (mode == EditorMode.ADD) {
                key = new Key();
            }

            // Just for safety, we check if the key is null.
            if (key == null)
                throw new Error("Invalid program state.");

            // If mode is delete, we need to ask if the user really wants to delete the
            // person.
            if (mode == EditorMode.DELETE) {
                printEditorHeader("Key");
                // Print the key delete confirmation.
                System.out.println(ANSI_BLUE + "Are you sure you want to delete " + ANSI_RESET + ANSI_YELLOW
                        + key.getName() + ANSI_RESET + ANSI_BLUE + "?" + ANSI_RESET);
                System.out.println(ANSI_GREEN + "Y. Yes" + ANSI_RESET);
                System.out.println(ANSI_RED + "N. No" + ANSI_RESET);
                // Ask user for input.
                String input2 = "";
                do {
                    input2 = scanner.nextLine();
                } while (!input2.matches("[yn]"));

                if (input2.equalsIgnoreCase("y")) {
                    // Delete the key.
                    building.removeKey(key.getName());
                    // Clear the screen.
                    clearScreen();
                    // Print the key editor.
                    System.out.println(ANSI_BLUE + "== Key editor ==" + ANSI_RESET);
                    // Print the key delete confirmation.
                    System.out.println(ANSI_BLUE + "Key " + ANSI_RESET + ANSI_YELLOW + key.getName() + ANSI_RESET
                            + ANSI_BLUE + " deleted." + ANSI_RESET);
                    // End of key editor.
                    System.out.println(ANSI_BLUE + "== End of key editor ==" + ANSI_RESET);
                }
                continue;
            }
            // Else, if we are in view mode, we just print the key.
            else if (mode == EditorMode.VIEW) {
                printEditorHeader("Key");
                // Print the key name.
                System.out.println(ANSI_BLUE + "Key: " + ANSI_RESET + ANSI_YELLOW + key.getName() + ANSI_RESET);
                // Print the key codes.
                System.out.print(ANSI_BLUE + "Codes: " + ANSI_RESET);
                for (int i = 0; i < key.getValues().size(); i++) {
                    System.out.print(ANSI_YELLOW + key.getValues().get(i) + ANSI_RESET
                            + (i == key.getValues().size() - 1 ? "" : ", "));
                }
                // Nextline.
                System.out.println();
                // End of key editor.
                System.out.println(ANSI_BLUE + "== End of key editor ==" + ANSI_RESET);
                // Wait for user to press enter.
                scanner.nextLine();
                continue;
            }
            // Else, we are in edit or add mode.
            else {
                // Init cursor.
                int cursor = -1;
                boolean invalidInput = false;
                // Enter command loop.
                boolean exitEditor = false;
                while (!exitEditor) {
                    // Print the person details.
                    // Depending on cursor, we ask for input in different ways.
                    String input2 = "";
                    printEditorHeader("Key");
                    // Print the key name.
                    System.out.println(ANSI_BLUE + "Key: " + ANSI_RESET
                            + (cursor == 0 ? ANSI_YELLOW + ANSI_BLINK : ANSI_GREEN) + key.getName() + ANSI_RESET);
                    // Print the key codes.
                    System.out.print(ANSI_BLUE + "Codes: " + ANSI_RESET);
                    for (int i = 0; i < key.getValues().size(); i++) {
                        System.out.print((cursor == 1 ? ANSI_YELLOW : ANSI_GREEN) + key.getValues().get(i) + ANSI_RESET
                                + (i == key.getValues().size() - 1 ? "" : ", "));
                    }
                    // Nextline.
                    System.out.println();
                    if (cursor == -1) {
                        System.out.println(ANSI_GREEN + "0. Edit name" + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "1. Add/remove code" + ANSI_RESET);
                        System.out.println(ANSI_RED + "2. Done" + ANSI_RESET);
                        System.out.println(ANSI_RED + "3. Undo" + ANSI_RESET);
                    } else {
                        if (invalidInput)
                            System.out.print(ANSI_RED + "Invalid input. Try again:" + ANSI_RESET);
                        else
                            System.out.print(ANSI_BLUE + "Input: " + ANSI_RESET);
                    }

                    invalidInput = false;
                    input2 = scanner.nextLine();
                    if (cursor == -1 && !input2.matches("[0-3]")) {
                        invalidInput = true;
                        continue;
                    } else if (cursor == 0 && input2.equals("")) {
                        invalidInput = true;
                        continue;
                    } else if (cursor == 1 && !input2.matches("[0-9]+")) {
                        invalidInput = true;
                        continue;
                    }
                    switch (cursor) {
                        case -1:
                            // This means the user has just chosen an option.
                            switch (Integer.parseInt(input2)) {
                                case 0:
                                    cursor = 0;
                                    break;
                                case 1:
                                    cursor = 1;
                                    break;
                                case 2:
                                    // If mode is add, we add the key.
                                    if (mode == EditorMode.ADD) {
                                        building.registerKey(key);
                                    } else {
                                        // Else, we are in edit mode, so we update the key.
                                        building.getKeys().get(Integer.parseInt(name)).setName(key.getName())
                                                .setValues(key.getValues());
                                    }
                                case 3:
                                    // Clear the screen.
                                    clearScreen();
                                    // Print the key editor.
                                    System.out.println(ANSI_BLUE + "== Key editor ==" + ANSI_RESET);
                                    // Print "Saved!"
                                    System.out
                                            .println(ANSI_GREEN + ((input2 == "2") ? "Saved!" : "Done.") + ANSI_RESET);
                                    // End of key editor.
                                    System.out.println(ANSI_BLUE + "== End of key editor ==" + ANSI_RESET);
                                    // Return true.
                                    exitEditor = true;
                                    continue;
                            }
                            break;
                        case 0:
                            // This means the user is editing the name.
                            key.setName(input2);
                            // Reset cursor.
                            cursor = -1;
                            break;
                        case 1:
                            // This means the user is editing the key codes.
                            // We "toggle" the code, meaning we add it if it isn't in the list, and remove
                            // it if it is.
                            if (key.getValues().contains(Integer.parseInt(input2))) {
                                key.getValues().remove(Integer.valueOf(Integer.parseInt(input2))); // To remove by
                                // value, not by
                                // index.
                            } else {
                                key.getValues().add(Integer.parseInt(input2));
                            }
                            // Reset cursor.
                            cursor = -1;
                            break;
                        default:
                            throw new Error("Invalid cursor value (" + cursor + ")");
                    }
                }
            }
        }
        // Since we should always return with one of the above cases, we should never
        // reach this point.
    }

    /**
     * The addNewRole function is the facede method to add keys to the building.
     * It makes a key with access to the listed floors.
     *
     * @param keyName The name of the key.
     * @param floors  The floors the key has access to. Take a variable number of
     *                arguments.
     * @throws IllegalArgumentException If the key name is already taken.
     * @throws IllegalArgumentException If the key name is empty.
     * @throws IllegalArgumentException If any of the floors is not in the building.
     * @return this to allow chaining.
     *         Note: If an exception is thrown, the building is not modified, and
     *         the key is
     *         not added, leaving the building in a legal state.
     */
    public Simulator addNewRole(String keyName, String... floors) {
        // Check if the key name is empty.
        if (keyName.equals("")) {
            throw new IllegalArgumentException("Key name cannot be empty.");
        }
        for (Key key : building.getKeys()) {
            // Check if the key name is already taken.
            if (key.getName().equals(keyName)) {
                throw new IllegalArgumentException("Key name already taken.");
            }
        }
        // Create the key.
        Key key = new Key(keyName);
        // Add the floors.
        for (String floor : floors) {
            // Check if the floor is in the building.
            Floor _floor = building.getFloorNamed(floor);
            if (_floor == null) {
                throw new IllegalArgumentException("Floor " + floor + " is not in the building.");
            }
            // Add the floor keyCode
            key.addKeyValue(_floor.getKey());
        }
        // Register the key.
        building.registerKey(key);
        return this;
    }

    /**
     * The addModelPerson function is the facede method to add a person to the
     * catalog.
     *
     * @param name   The name of the person.
     * @param weight The weight of the person.
     * @throws IllegalArgumentException If the name is already taken or empty.
     * @throws IllegalArgumentException If the weight is non positive.
     * @return this to allow chaining.
     */
    public Simulator addModelPerson(String name, int weight) {
        // Check if the name is empty.
        if (name.equals("")) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        for (Person person : people) {
            // Check if the name is already taken.
            if (person.getName().equals(name)) {
                throw new IllegalArgumentException("Name already taken.");
            }
        }
        // Create the person.
        Person person = new Person(name, weight);
        // Register the person.
        people.add(person);
        return this;
    }
}
