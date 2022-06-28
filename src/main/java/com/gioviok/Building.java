package com.gioviok;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The building is a facade class to manage the relationship between a set of
 * floors, A group of persons and A set of keys.
 * The building has an internal catalog of keys. Keys can be assigned to a
 * person, which will become a part of the building.
 * 
 * @see Person
 * @see Elevator
 * @see Floor
 * @see Key
 * @author Gioviok
 */
public class Building {

    /**
     * The name of the building. Defaults to the string "_name_unset".
     */
    private String name = "_name_unset";

    /**
     * The entrance floor of the building. Defaults to null. If null, gets
     * automatically set to the first floor added to the building.
     *
     * @see Floor
     */
    private Floor entranceFloor;

    /**
     * The list of floors in the building. It is kept sorted.
     *
     * @see Floor
     */
    private ArrayList<Floor> floors = new ArrayList<Floor>();

    /**
     * The list of all keys registered to the building.
     *
     * @see Key
     */
    private ArrayList<Key> keys = new ArrayList<Key>();

    /**
     * The list of people registered to the building. It is NOT to be confused with
     * the catalog of persons, which is a static member of Person.
     *
     * @see Person
     */
    private ArrayList<Person> persons = new ArrayList<Person>();

    /**
     * The internal elevator. Must be initialized before use.
     *
     * @see Elevator
     */
    private Elevator elevator;

    /**
     * The getPersons method returns the list of persons registered in the building.
     * 
     * @return The list of persons registered in the building.
     * @see Person
     */
    ArrayList<Person> getPersons() {
        return persons;
    }

    /**
     * The getKeyNamed method returns the key with the given name. The key must be
     * cloned to be used.
     *
     * @param name The name of the key.
     * @return The key with the given name, null if it does not exist.
     * @see Cloneable
     * @see Key
     */
    public Key getKeyNamed(String name) {
        if (name == null)
            return null;
        for (Key key : keys) {
            if (key.getName().equals(name)) {
                return key;
            }
        }
        return null;
    }

    /**
     * The registerKey method registers a key in the catalog. Name must be unique.
     *
     * @param key The key to register.
     * @return this.
     * @throws IllegalArgumentException if the key is null or already registered.
     * @see Key
     */
    public Building registerKey(Key key) throws IllegalArgumentException {
        // Check if the key is null
        if (key == null) {
            throw new IllegalArgumentException("Can't register a null key!");
        }
        // Check if the key is already registered.
        for (Key k : keys) {
            if (k.getName().equals(key.getName())) {
                throw new IllegalArgumentException("Key with name " + key.getName() + " already registered.");
            }
        }
        // Add the key to the catalog.
        keys.add(key);
        return this;
    }

    /**
     * The setupElevator method sets up the elevator. It returns this to allow
     * concatenation.
     *
     * @param floor             The floor of the elevator (must be one of the floors
     *                          of the building).
     * @param direction         The current direction of the elevator.+
     * @param elevatorWeight    The weight of the elevator cabin.
     * @param elevatorMaxWeight The maximum weight the elevator can carry (including
     *                          the cabin weight).
     * @return this.
     * @see Floor
     * @see Elevator
     * @see Elevator.MovingDirection
     */
    public Building setupElevator(Floor floor, Elevator.MovingDirection direction, double elevatorWeight,
            double elevatorMaxWeight) {
        if (this.elevator == null) {
            this.elevator = new Elevator();
        }
        elevator.setup((floor == null) ? entranceFloor : floor,
                direction,
                elevatorWeight,
                elevatorMaxWeight);
        return this;
    }

    /**
     * The removeKey method removes a key from the catalog. It doesn't remove the
     * key from people who have it already assigned.
     *
     * @param name The name of the key to remove.
     * @return True if the key was removed, false otherwise.
     * @see Key
     */
    public boolean removeKey(String name) {
        // Check if the key is already registered.
        Key key = getKeyNamed(name);
        if (key == null) {
            return false;
        }
        // Remove the key from the catalog.
        return keys.remove(key);
    }

    /**
     * The registerPerson method registers a person to this building with the given
     * key. Key must be one of our keys. Name must be unique.
     *
     * @param person  The person to register.
     * @param keyName The keyName of the key to add (can be null).
     * @param floor   The floor of the person (can be null, in which case the person
     *                is on the entrance floor, if the building has one).
     * @return The person registered.
     * @throws IllegalArgumentException If a person with the same name is already
     *                                  registered or the key is not one of our
     *                                  keys.
     * @throws IllegalStateException    If the building has no floors.
     * @see Person
     * @see Key
     * @see Floor
     */
    public Person registerPerson(Person person, String keyName, Floor floor) throws IllegalArgumentException {
        // Check if the person is already registered.
        for (Person p : persons) {
            if (p.getName().equals(person.getName())) {
                throw new IllegalArgumentException("Person with name " + person.getName() + " already registered.");
            }
        }
        // Check if the key is one of our keys.
        if (keyName != null && getKeyNamed(keyName) == null) {
            throw new IllegalArgumentException("Key " + keyName + " is not one of our keys.");
        }
        // Check if the building has floors.
        if (getFloors().size() == 0) {
            throw new IllegalStateException("Building has no floors.");
        }
        // Check if the floor is one of our floors.
        if (floor != null && !getFloors().contains(floor)) {
            throw new IllegalArgumentException("Floor " + floor.getName() + " is not one of our floors.");
        }
        // Add the person to the list.
        Person p = person.setKey(getKeyNamed(keyName)).setCurrentBuilding(this)
                .setCurrentFloor((floor == null) ? entranceFloor : floor);
        persons.add(p);
        return p;
    }

    /**
     * The null floor call of the registerPerson method.
     *
     * @param person  The person to register.
     * @param keyName The keyName to assign (can be null).
     * @return The person registered.
     * @throws IllegalArgumentException If a person with the same name is already
     *                                  registered or the key is not one of our
     *                                  keys.
     * @throws IllegalStateException    If the building has no floors.
     * @see Person
     * @see Key
     */
    public Person registerPerson(Person person, String keyName) throws IllegalArgumentException {
        return registerPerson(person, keyName, null);
    }

    /**
     * The getPersonNamed method returns the person with the given name. It returns
     * null if the person does not exist.
     *
     * @param name The name of the person.
     * @return The person with the given name, null if it does not exist.
     */
    public Person getPersonNamed(String name) {
        for (Person p : persons) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * The removePerson method removes a person from the building.
     *
     * @param name The name of the person to remove.
     * @return The removed person, null if it does not exist.
     * @see Person
     */
    public Person removePerson(String name) {
        // Check if the person is already registered.
        Person person = getPersonNamed(name);
        if (person == null) {
            return null;
        }
        // Remove the person from the list.
        persons.remove(person);
        return person;
    }

    /**
     * The addFloor method adds a floor to the building. If the building has no
     * entrance floor, the new floor is set to be the entrance floor.
     *
     * @param floor The floor to add.
     * @return this
     * @throws IllegalArgumentException If there is already a floor with the same
     *                                  name or height, or if the floor is null.
     * @throws Error                    If inconsistency during the checks get
     *                                  found.
     *                                  Note: One should not try to catch the Error
     *                                  exception. It means that
     *                                  something is wrong with the whole execution.
     * @see Floor
     * @see Error
     */
    public Building addFloor(Floor floor) throws IllegalArgumentException, Error {
        // Check if the floor is null.
        if (floor == null) {
            throw new IllegalArgumentException("Floor is null.");
        }
        // Check if the floor is already registered.
        for (Floor f : floors) {
            if (f.getName().equals(floor.getName())) {
                throw new IllegalArgumentException("Floor with name " + floor.getName() + " already registered.");
            }
            if (f.getDistance() == floor.getDistance()) {
                throw new IllegalArgumentException("Floor with height " + floor.getDistance() + " already registered.");
            }
        }
        // We do a binary search to find the insertion index (floors are always sorted
        // by distance to the ground level).
        int index = Collections.binarySearch(floors, floor);
        // We expect the index to be negative since we are inserting a new floor.
        index = -index - 1; // We get the insertion index.
        // Add the floor to the list.
        floors.add(index, floor);
        // If the building has no entrance floor, the new floor is the entrance floor.
        if (entranceFloor == null) {
            entranceFloor = floor;
            // If the elevator is on null floor, it is on the entrance floor.
            if (elevator.getCurrentFloor() == null) {
                elevator.setCurrentFloor(entranceFloor);
            }
        }
        return this;
    }

    /**
     * The removeFloor method removes a floor from the building.
     *
     * @param floor The floor to remove.
     * @return True if the floor was removed, false otherwise.
     */
    public boolean removeFloor(Floor floor) {
        return floors.remove(floor);
    }

    /**
     * The constructor of the building.
     *
     * @param name The name of the building.
     */
    public Building(String name) {
        this.name = name;
        this.elevator = new Elevator();
    }

    /**
     * The empty constructor of the building.
     */
    public Building() {
        this.elevator = new Elevator();
    };

    /**
     * The getName method returns the name of the building.
     *
     * @return The name of the building.
     */
    public String getName() {
        return name;
    }

    /**
     * The setName method sets the name of the building.
     *
     * @param name The name of the building.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The getFloors method returns the list of floors.
     *
     * @return The list of floors.
     */
    public ArrayList<Floor> getFloors() {
        return floors;
    }

    /**
     * The getElevator method returns the elevator.
     *
     * @return The elevator.
     */
    public Elevator getElevator() {
        return elevator;
    }

    /**
     * The setElevator method sets the elevator.
     *
     * @param elevator The elevator.
     * @see Elevator
     */
    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    /**
     * The callElevator method calls the elevator to a given floor by adding the
     * floor to the list of floors to visit.
     *
     * @param floor The floor to visit.
     * @return true if the floor was added to the list of floors to visit, false
     *         otherwise.
     * @throws IllegalArgumentException If the floor is null.
     */
    public boolean callElevator(Floor floor) throws IllegalArgumentException {
        // Check if the floor is null.
        if (floor == null) {
            throw new IllegalArgumentException("Floor is null.");
        }
        // Add the floor to the list of floors to visit without checking the key.
        return elevator.requestHere(floor);
    }

    /**
     * The getFloorNamed method returns the floor with the given name.
     *
     * @param name The name of the floor.
     * @return The floor with the given name, null if it does not exist.
     * @see Floor
     */
    public Floor getFloorNamed(String name) {
        for (Floor floor : floors) {
            if (floor.getName().equals(name)) {
                return floor;
            }
        }
        return null;
    }

    /**
     * The setEntranceFloorNamed method sets the entrance floor to the floor with
     * the given name.
     *
     * @param name The name of the floor.
     * @return true if the floor was set as the entrance floor, false otherwise.
     * @throws IllegalArgumentException If the name given is null or if the floor
     *                                  with the given name does not exist.
     * @see Floor
     */
    public boolean setEntranceFloorNamed(String name) throws IllegalArgumentException {
        // Check if the name is null.
        if (name == null) {
            throw new IllegalArgumentException("Name is null.");
        }
        // Check if the floor exists.
        Floor floor = getFloorNamed(name);
        if (floor == null) {
            throw new IllegalArgumentException("Floor with name " + name + " does not exist.");
        }
        // Set the entrance floor.
        entranceFloor = floor;
        return true;
    }

    /**
     * The numberOfFloors method returns the number of floors in the building.
     *
     * @return The number of floors in the building.
     */
    public int numberOfFloors() {
        return floors.size();
    }

    /**
     * The numberOfPeople method returns the number of people in the building.
     *
     * @return The number of people in the building.
     */
    public int numberOfPeople() {
        return persons.size();
    }

    /**
     * The getEntranceFloor method returns the entrance floor.
     *
     * @return The entrance floor.
     */
    public Floor getEntranceFloor() {
        return entranceFloor;
    }

    /**
     * The numberOfKeys method returns the number of registered keys of the
     * building.
     *
     * @return The number of registered keys.
     */
    public int numberOfKeys() {
        return keys.size();
    }

    /**
     * The getKeys method returns the catalog of keys.
     *
     * @return The catalog of keys.
     */
    ArrayList<Key> getKeys() {
        return keys;
    }

}
