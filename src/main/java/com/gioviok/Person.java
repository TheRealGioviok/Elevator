package com.gioviok;

import java.util.ArrayList;

/**
 * The Person class contains the information about a person.
 * It has a name, a weight, a current floor and a key (can be null).
 * The person class is used in a prototype design pattern. The Person
 */
public class Person implements Cloneable {
    /**
     * A catalog of Persons. Used as a part of the prototype pattern.
     */
    private static ArrayList<Person> catalog = new ArrayList<Person>();

    /**
     * The registerPerson method registers a person in the catalog. Name must be
     * unique.
     *
     * @param person The person to register.
     */
    public static void registerPerson(Person person) throws IllegalArgumentException {
        // Check if the person is already registered.
        for (Person p : Person.catalog) {
            if (p.getName().equals(person.getName())) {
                throw new IllegalArgumentException("Person with name " + person.getName() + " already registered.");
            }
        }
        // Add the person to the catalog.
        catalog.add(person);
    }

    /**
     * The getPersonNamed method returns the person with the given name. The person
     * must be cloned to be used.
     *
     * @param name The name of the person.
     * @return The person with the given name, null if it does not exist.
     */
    public static Person getPersonNamed(String name) {
        for (Person person : catalog) {
            if (person.getName().equals(name)) {
                return person;
            }
        }
        return null;
    }

    /**
     * The removePerson method removes a person from the catalog.
     *
     * @param name The name of the person to remove.
     * @return True if the person was removed, false otherwise.
     */
    public static boolean removePerson(String name) {
        for (Person person : catalog) {
            if (person.getName().equals(name)) {
                catalog.remove(person);
                return true;
            }
        }
        return false;
    }

    /**
     * The name of the person.
     */
    private String name;
    /**
     * The weight of the person.
     */
    private int weight;
    /**
     * A reference to what building is the person in.
     */
    private Building currentBuilding;
    /**
     * A reference to the current floor the person is on.
     */
    private Floor currentFloor;
    /**
     * The key of this person. Can be null.
     */
    private Key key;

    /**
     * The Person constructor.
     *
     * @param name            The name to assign. Must be non empty and not null.
     * @param weight          The weight to assign. Must be positive.
     * @param currentBuilding What building to assign (can be null).
     * @param currentFloor    What's the current floor to assign (can be null).
     * @param key             What key to assign (can be null).
     * @throws IllegalArgumentException if the name is null or empty.
     * @throws IllegalArgumentException if the weight is non positive.
     */
    public Person(String name, int weight, Building currentBuilding, Floor currentFloor, Key key)
            throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The name of the person cannot be null or empty.");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("The weight of the person must be positive.");
        }
        this.name = name;
        this.weight = weight;
        this.currentBuilding = currentBuilding;
        this.currentFloor = currentFloor;
        this.key = key;
    }

    /**
     * The Person constructor.
     *
     * @param name   The name to assign. Must be non empty and not null.
     * @param weight The weight to assign. Must be positive.
     * @throws IllegalArgumentException if the name is null or empty.
     * @throws IllegalArgumentException if the weight is non positive.
     */
    public Person(String name, int weight) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The name of the person cannot be null or empty.");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("The weight of the person must be positive.");
        }
        this.name = name;
        this.weight = weight;
    }

    /**
     * The Person constructor.
     */
    public Person() {
    };

    /**
     * Name getter.
     *
     * @return The name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter.
     *
     * @param name The name of the person.
     * @return this, to allow concatenation.
     * @throws IllegalArgumentException If the string is null or empty.
     * @see String
     */
    public Person setName(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
        return this;
    }

    /**
     * Weight getter.
     *
     * @return The weight of the person.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Weight setter.
     *
     * @param weight The weight of the person.
     * @return this.
     * @throws IllegalArgumentException If the weight is non positive.
     */
    public Person setWeight(int weight) throws IllegalArgumentException {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive.");
        }
        this.weight = weight;
        return this;
    }

    /**
     * The callElevatorRide function calls the elevator to the current floor. It
     * ignores the key, since the person is already at the floor.
     *
     * @return True if the call was successful, false otherwise.
     * @throws IllegalStateException If the person's current floor is null.
     */
    public boolean callElevatorRide() throws IllegalStateException {
        if (currentFloor == null) {
            throw new IllegalStateException("The person's current floor is null.");
        }
        return currentBuilding.callElevator(currentFloor);
    }

    /**
     * The chooseFloor function chooses a floor to go to. It checks the key to be
     * valid for the floor.
     *
     * @param floor The floor to go to.
     * @return True if the floor was chosen, exception otherwise
     * @throws IllegalStateException    If the person's current floor or building is
     *                                  null.
     * @throws IllegalArgumentException If the person's key is not valid for the
     *                                  floor.
     */
    public boolean chooseFloor(Floor floor) {
        if (currentFloor == null || currentBuilding == null) {
            throw new IllegalStateException("The person's current floor or building is null.");
        }
        // We request the ride
        return currentBuilding.getElevator().requestRide(floor, key);
    }

    /**
     * Key getter.
     *
     * @return The key of the person.
     * @see Key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Key setter.
     *
     * @param key The key of the person.
     * @return this.
     * @see Key
     */
    public Person setKey(Key key) {
        this.key = key;
        return this;
    }

    /**
     * Current building getter.
     *
     * @return The current building of the person.
     * @see Building
     */
    public Building getCurrentBuilding() {
        return currentBuilding;
    }

    /**
     * Current building setter.
     *
     * @param currentBuilding The current building of the person.
     * @return this.
     * @see Building
     */
    public Person setCurrentBuilding(Building currentBuilding) {
        this.currentBuilding = currentBuilding;
        return this;
    }

    /**
     * Current floor getter.
     *
     * @return The current floor of the person.
     * @throws IllegalStateException If the person is not in a building.
     */
    public Floor getCurrentFloor() throws IllegalStateException {
        if (currentBuilding == null) {
            throw new IllegalStateException("The person is not in a building.");
        }
        return currentFloor;
    }

    /**
     * Current floor setter.
     *
     * @param currentFloor The current floor of the person.
     * @return this.
     * @throws IllegalStateException    If the person is not in a building.
     * @throws IllegalArgumentException If the argument is not a floor of the
     *                                  current building.
     */
    public Person setCurrentFloor(Floor currentFloor) throws IllegalStateException, IllegalArgumentException {
        if (currentBuilding == null) {
            throw new IllegalStateException("The person is not in a building.");
        }
        if (!currentBuilding.getFloors().contains(currentFloor)) {
            throw new IllegalArgumentException("The floor is not a floor of the current building.");
        }
        this.currentFloor = currentFloor;
        return this;
    }

    /**
     * The clone method returns a copy of the person.
     *
     * @return A copy of the person.
     * @see java.lang.Object#clone()
     */
    public Person clone() {
        return new Person(name, weight, currentBuilding, currentFloor, key);
    }

    /**
     * The toString method returns a string representation of the person.
     *
     * @return A string representation of the person.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Person: " + name + " - Weight: " + weight;
    }

    /**
     * The disembark function removes the person from the elevator.
     * 
     * @return true if disembarked, false otherwise (would throw exception before
     *         return).
     * @throws IllegalStateException If the person is not in an elevator.
     * @throws IllegalStateException If the person is not in a building.
     * @see Elevator
     */
    public boolean disembark() throws IllegalStateException {
        if (currentBuilding == null) {
            throw new IllegalStateException("The person is not in a building.");
        }
        if (currentBuilding.getElevator() == null) {
            throw new IllegalStateException("The Elevator is not initialized.");
        }
        return currentBuilding.getElevator().disembark(this);

    }

    /**
     * The board method tries to board the person in the elevator of the building.
     * 
     * @return true if boarded, false otherwise (would throw exception before
     *         return).
     * @throws IllegalStateException If the person is already in an elevator.
     * @throws IllegalStateException If the person is not in a building.
     * @see Elevator
     */
    public boolean board() throws IllegalStateException {
        if (currentBuilding == null) {
            throw new IllegalStateException("The person is not in a building.");
        }
        if (currentBuilding.getElevator() == null) {
            throw new IllegalStateException("The Elevator is not initialized.");
        }
        return currentBuilding.getElevator().board(this);
    }
}
