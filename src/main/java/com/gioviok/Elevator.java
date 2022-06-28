package com.gioviok;

import java.util.ArrayList;

/**
 * The elevator class has a current floor, a moving direction and a list of
 * floors to visit. It has also information about his weight.
 *
 * @author gioviok
 * @version 1.0
 * @since 1.0
 * @see Floor
 * @see Key
 */
public class Elevator {

    /**
     * The current floor of the elevator.
     */
    private Floor currentFloor;

    /**
     * The enum of all possible movement directions an Elevator can assume.
     * 
     * @see Elevator
     * @author Gioviok
     */
    public enum MovingDirection {
        /** The elevator is going up. */
        UP,
        /** The elevator is going down. */
        DOWN,
        /** The elevator is not moving. */
        STATIONARY
    }

    /**
     * The moving direction of the elevator.
     */
    private MovingDirection movingDirection;

    /**
     * The internal list of floors that the elevator was called to.
     */
    private ArrayList<Floor> floorsToVisit = new ArrayList<>();

    /**
     * The weight of the elevator cabin.
     *
     * @see Elevator#isOverloaded()
     */
    private double elevatorWeight;

    /**
     * The maximum weight that the elevator can carry. It includes the weight of the
     * cabin.
     *
     * @see Elevator#isOverloaded()
     */
    private double maxWeight;

    /**
     * The internal list of people currently on the elevator.
     *
     * @see Person
     */
    private ArrayList<Person> persons = new ArrayList<>();

    /**
     * Current floor getter.
     *
     * @return the current floor.
     * @see Floor
     */
    public Floor getCurrentFloor() {
        return currentFloor;
    }

    /**
     * The default constructor for the elevator class. It initializes the elevator
     * weight and maxWeight to 0,
     * its movingDirection as STATIONARY and the current floor as null.
     */
    public Elevator() {
        this.currentFloor = null;
        this.movingDirection = MovingDirection.STATIONARY;
        this.elevatorWeight = 0;
        this.maxWeight = 0;
    }

    /**
     * The constructor of the elevator.
     *
     * @param currentFloor    The current floor of the elevator.
     * @param movingDirection The moving direction of the elevator.
     * @param weight          The weight of the elevator.
     * @param maxWeight       The maximum weight that the elevator can carry.
     */
    public Elevator(Floor currentFloor, MovingDirection movingDirection, double weight, double maxWeight) {
        this.currentFloor = currentFloor;
        this.movingDirection = movingDirection;
        this.elevatorWeight = weight;
        this.maxWeight = maxWeight;
    }

    /**
     * The initializer of the elevator.
     * Used for unit testing.
     *
     * @param currentFloor    The current floor of the elevator.
     * @param movingDirection The moving direction of the elevator.
     * @param weight          The weight of the elevator.
     * @param maxWeight       The maximum weight that the elevator can carry.
     */
    void setup(Floor currentFloor, MovingDirection movingDirection, double weight, double maxWeight) {
        this.currentFloor = currentFloor;
        this.movingDirection = movingDirection;
        this.elevatorWeight = weight;
        this.maxWeight = maxWeight;
    }

    /**
     * The requestRide method requests a ride to the floor.
     *
     * @param floor The floor to go to.
     * @param key   The key to use.
     * @return True if the floor was added to the list of floors to visit, false
     *         otherwise.
     * @throws IllegalArgumentException If the key is not valid.
     * @throws IllegalArgumentException If the floor is null.
     */
    boolean requestRide(Floor floor, Key key) {
        if (floor == null) {
            throw new IllegalArgumentException("The floor cannot be null.");
        }
        if (floor == currentFloor) {
            return false;
        }
        // We check if the floor is accessible with the keys currently in the elevator.
        if (floor.requestAccess(key)) {
            // We check if the floor isn't already in the list of floors to visit.
            if (floorsToVisit.contains(floor)) {
                return false;
            }
            // We add the floor to the list of floors to visit.
            floorsToVisit.add(floor);
            // If stationary, we set the moving direction.
            if (movingDirection == MovingDirection.STATIONARY) {
                movingDirection = (floor.getDistance() > currentFloor.getDistance()) ? MovingDirection.UP
                        : MovingDirection.DOWN;
            }
            // print(2);
            return true;
        }
        throw new IllegalArgumentException("The key is not valid.");
    }

    /**
     * The requestHere method requests the elevator to stop at the current floor. It
     * won't check for the key.
     *
     * @param floor The floor to stop at.
     * @return True if the floor was added to the list of floors to visit, false
     *         otherwise.
     * @throws IllegalArgumentException If the floor is null.
     */
    boolean requestHere(Floor floor) {
        if (floor == null) {
            throw new IllegalArgumentException("The floor cannot be null.");
        }
        if (floor == currentFloor) {
            return false;
        }
        // We add the floor to the list of floors to visit.
        if (floorsToVisit.contains(floor)) {
            return false;
        }
        floorsToVisit.add(floor);
        // If stationary, we set the moving direction.
        if (movingDirection == MovingDirection.STATIONARY) {
            movingDirection = (floor.getDistance() > currentFloor.getDistance()) ? MovingDirection.UP
                    : MovingDirection.DOWN;
        }
        // print(2);
        return true;
    }

    /**
     * The halt method stops the elevator. It also unboards every person at the
     * current floor.
     *
     * Note: this method may produce undesired behaviour when unboarding
     * unauthorized persons to the current floor.
     */
    public void halt() {
        movingDirection = MovingDirection.STATIONARY;
        // We clear the list of floors to visit. We do this since we don't know if a
        // floor accessible with the keys is present in the list.
        // It could in fact happen that the person using the key will be forced to exit
        // the elevator, and we don't want to make non authorized people have access to
        // that floor, at least when the
        // authorized person is not in the elevator.
        clearRequests();
    }

    /**
     * The isOverloaded method checks if the elevator is overloaded.
     *
     * @return True if the elevator is overloaded, false otherwise.
     */
    public boolean isOverloaded() {
        return getCurrentLoad() > maxWeight;
    }

    /**
     * The clearRequests method clears the list of floors to visit.
     */
    private void clearRequests() {
        floorsToVisit.clear();
    }

    /**
     * The run method runs the elevator.
     *
     * @return True if the elevator has no more floors to visit, false otherwise.
     */
    public boolean run() throws IllegalStateException {

        // If the elevator is overloaded, we throw an exception.
        if (isOverloaded()) {
            // We halt the elevator.
            halt();
            throw new IllegalStateException("The elevator is overloaded. (Weight: " + getCurrentLoad()
                    + "kg, Max weight: " + maxWeight + "kg)");
        }
        // We get the next floor.
        Floor nextFloor = nextFloor();
        // We check if the next floor is null.
        if (nextFloor == null) {
            // We halt the elevator.
            halt();
            return true;
        }

        // For now, we just move to the next floor.
        currentFloor = nextFloor;
        // We move every person on the elevator.
        for (Person person : persons) {
            person.setCurrentFloor(currentFloor);
        }
        // We remove the floor from the list of floors to visit.
        floorsToVisit.remove(nextFloor);

        // print(2);

        if (floorsToVisit.isEmpty()) {
            // We halt the elevator.
            halt();
            return true;
        }
        return false;
    }

    /**
     * The nextFloor method returns the next floor to visit.
     *
     * @return The next floor to visit. Null if there is no next floor.
     * @see Floor
     * @see Key
     */
    private Floor nextFloor() {
        // We check if there is a next floor.
        if (floorsToVisit.size() > 0) {
            // We look for the closest floor to the current floor, in the current moving
            // direction.
            Floor closestFloor = null;
            int closestFloorDistance = Integer.MAX_VALUE;
            for (Floor floor : floorsToVisit) {
                int distance;
                // Are we going up or down?
                if (movingDirection == MovingDirection.UP) {
                    distance = floor.getDistance() - currentFloor.getDistance();
                } else {
                    distance = currentFloor.getDistance() - floor.getDistance();
                }
                // If distance is negative or 0, the floor is in the wrong direction or the same
                // floor (shouldn't happen).
                if (distance <= 0)
                    continue;
                // If the distance is smaller than the closest floor distance, we update the
                // closest floor.
                if (distance < closestFloorDistance) {
                    closestFloor = floor;
                    closestFloorDistance = distance;
                }
            }
            // If we didn't find a floor, we can inverse the direction.
            if (closestFloor == null) {
                movingDirection = (movingDirection == MovingDirection.UP) ? MovingDirection.DOWN : MovingDirection.UP;
                return nextFloor();
            }
            return closestFloor;
        }
        // We return null.
        movingDirection = MovingDirection.STATIONARY;
        return null;
    }

    /**
     * The getCurrentLoad method returns the current load of the elevator.
     * It calculates it by adding the weight of the elevator and the weight of every
     * person in the elevator.
     *
     * @return The current load of the elevator.
     * @see Person
     */
    public double getCurrentLoad() {
        double currentLoad = elevatorWeight;
        for (Person person : persons) {
            currentLoad += person.getWeight();
        }
        return currentLoad;
    }

    /**
     * The getCurrentMovingDirection method returns the current moving direction of
     * the elevator.
     *
     * @return The current moving direction of the elevator.
     */
    public MovingDirection getCurrentMovingDirection() {
        return movingDirection;
    }

    /**
     * The board method adds a person to the elevator.
     *
     * @param person The person to add.
     * @return True if the person was added, false otherwise.
     * @throws IllegalArgumentException If the person is not on the same floor as
     *                                  the elevator.
     * @throws IllegalArgumentException If the person is already in the elevator.
     * @throws IllegalArgumentException If the person is null.
     * @see Person
     */
    public boolean board(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("The person cannot be null.");
        }
        // We check if the person is on the same floor as the elevator.
        if (person.getCurrentFloor() != currentFloor) {
            throw new IllegalArgumentException(
                    "The person is not on the same floor as the elevator: Person is on floor "
                            + person.getCurrentFloor().getName() + ", elevator is on floor " + currentFloor.getName());
        }
        // We check if the person is not already in the elevator.
        if (persons.contains(person)) {
            throw new IllegalArgumentException("The person is already in the elevator.");
        }
        // We add the person to the elevator.
        return persons.add(person);
    }

    /**
     * The disembark method removes a person from the elevator.
     *
     * @param person The person to remove.
     * @return True if the person was removed, false otherwise.
     * @throws IllegalArgumentException If the person is not in the elevator.
     * @throws IllegalArgumentException If the person is null.
     * @see Person
     */
    public boolean disembark(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("The person cannot be null.");
        }
        // We check if the person is in the elevator.
        if (!persons.contains(person)) {
            throw new IllegalArgumentException("The person is not in the elevator.");
        }
        // We remove the person from the elevator.
        return persons.remove(person);
    }

    /**
     * The getWeight method returns the weight of the elevator. It doesn't include
     * the weight of the people.
     *
     * @return The weight of the elevator.
     */
    public double getWeight() {
        return elevatorWeight;
    }

    /**
     * The getMaxLoad method returns the max load of the elevator.
     *
     * @return The max load of the elevator.
     */
    public double getMaxLoad() {
        return maxWeight;
    }

    /**
     * The numberOfPeople method returns the number of people in the elevator.
     *
     * @return The number of people in the elevator.
     * @see Person
     */
    public int numberOfPeople() {
        return persons.size();
    }

    /**
     * The setWeight method sets the weight of the elevator's cabin.
     *
     * @param weight The weight to set.
     */
    public void setWeight(double weight) {
        elevatorWeight = weight;
    }

    /**
     * The setMaxLoad method sets the max load of the elevator.
     *
     * @param maxLoad The max load to set.
     */
    public void setMaxLoad(double maxLoad) {
        maxWeight = maxLoad;
    }

    /**
     * The setCurrentFloor method sets the current floor of the elevator.
     *
     * @param floor The floor to set as the current floor
     *              Note: This method is to be used for initialization or testing.
     *              For all other purposes,
     *              the run method should be the only one to modify the current
     *              floor.
     */
    void setCurrentFloor(Floor floor) {
        currentFloor = floor;
    }

    /**
     * The hasPerson method returns true if the elevator has a certain person, false
     * otherwise.
     *
     * @param person the person to check
     * @return True if the elevator has the person, false otherwise.
     */
    boolean hasPerson(Person person) {
        return persons.contains(person);
    }
}
