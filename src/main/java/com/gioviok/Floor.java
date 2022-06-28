package com.gioviok;

/**
 * The Floor class contains the information about a floor.
 * It has a name, a key (if it is accessible by only a key), and a distance from
 * the ground.
 * Once instantiated, the floor is only accessible by the elevator.
 *
 * @author gioviok
 * @version 1.0
 * @since 1.0
 * @see Elevator
 * @see Key
 */
public class Floor implements Comparable<Floor>, Cloneable {
    /**
     * The name of the floor.
     */
    private String name;
    /**
     * The keyCode of the floor. A value of 0 means no key.
     */
    private int key;
    /**
     * The distance from the ground. Negative indicates underground levels. It is
     * used to sort floors.
     */
    private int distance;

    /**
     * The clone method of the floor.
     *
     * @return A copy of the floor.
     */
    @Override
    public Floor clone() {
        return new Floor(name, key, distance);
    }

    /**
     * The empty constructor of the floor.
     */
    public Floor() {
    };

    /**
     * The constructor of the floor.
     *
     * @param name     The name of the floor.
     * @param key      The key of the floor. (null if it does not have a key)
     * @param distance The distance from the ground (in millimeters).
     * @see Key
     * @see Elevator
     */
    public Floor(String name, int key, int distance) {
        this.name = name;
        this.key = key;
        this.distance = distance;
    }

    /**
     * The equals method of the floor.
     *
     * @return True if the floor is equal to the other floor. False otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Floor)) {
            return false;
        }
        Floor otherFloor = (Floor) other;
        return name.equals(otherFloor.name) && key == otherFloor.key && distance == otherFloor.distance;
    }

    /**
     * The getName method returns the name of the floor.
     *
     * @return The name of the floor.
     */
    public String getName() {
        return name;
    }

    /**
     * The getKey method returns the key of the floor.
     *
     * @return The key of the floor. (null if it does not have a key)
     * @see Key
     */
    public int getKey() {
        return key;
    }

    /**
     * The getDistance method returns the distance from the ground of the floor.
     *
     * @return The distance from the ground of the floor.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * The hasKey method returns true if the floor has a key, false otherwise.
     *
     * @return True if the floor has a key, false otherwise.
     * @see Key
     */
    public boolean hasKey() {
        return key != 0;
    }

    /**
     * The requestAccess method requests access to the floor.
     *
     * @param key The key to use.
     * @return True if the key can give access to the floor, false otherwise.
     * @see Key
     */
    public boolean requestAccess(Key key) {
        // We check if the elevator can go to the floor.
        if (!hasKey())
            return true;
        if (key == null)
            return false;
        else {
            if (key.checkAccess(this.key))
                return true;
        }
        return false;
    }

    /**
     * The toString method returns a the name of the floor. We declare it in order
     * to mantain compatibility.
     *
     * @return The name of the floor.
     */
    public String toString() {
        return name;
    }

    /**
     * The compareTo method is used to compare two floors.
     * We define a floor f to be greater than a floor x
     * if f.distance > x.distance
     *
     * @param o The floor to compare
     * @return the difference between our distance and the compared distance.
     */
    @Override
    public int compareTo(Floor o) {
        return this.distance - o.distance;
    }

    /**
     * Name setter. Returns this to allow concatenation.
     *
     * @param name The new name to set
     * @return this
     */
    public Floor setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Distance setter.
     *
     * @param distance The new distance to set.
     * @return this
     */
    public Floor setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    /**
     * Key setter.
     *
     * @param key The new Key to set.
     * @return this
     * @see Key
     */
    public Floor setKey(int key) {
        this.key = key;
        return this;
    }
}
