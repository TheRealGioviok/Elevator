package com.gioviok;

import java.util.ArrayList;

/**
 * The Key class contains the information about a key.
 * It has a name and a list of values.
 * The key class is an interface, meaning that it can be used by any class that
 * implements it.
 * The elevator manager can then create specific keys that can give access to
 * specific floors.
 * The key class implements the prototype design pattern.
 *
 * @author gioviok
 * @version 1.0
 * @since 1.0
 * @see Elevator
 * @see Floor
 */
public class Key implements Cloneable {
    /**
     * The name of this key.
     */
    private String name = "";

    /**
     * The list of keyCodes of this key.
     */
    private ArrayList<Integer> values = new ArrayList<Integer>();

    /**
     * The constructor of the key.
     *
     * @param name   The name of the key.
     * @param values The list of values that the key can give access to.
     */
    public Key(String name, ArrayList<Integer> values) {
        this.name = name;
        this.values = new ArrayList<>(values);
    }

    /**
     * The empty constructor of the key.
     */
    public Key() {
    };

    /**
     * The name constructor of the key. It creates a key with the given name and no
     * values.
     *
     * @param name The name to give to the key
     */
    public Key(String name) {
        this.name = name;
    }

    /**
     * The getter of the key's name.
     *
     * @return The key name.
     */
    public String getName() {
        return name;
    }

    /**
     * The getter of the keyCodes of the key.
     *
     * @return An ArrayList of integers, with the keyCodes as elements.
     *         Note: It returns a clone.
     */
    public ArrayList<Integer> getValues() {
        return new ArrayList<Integer>(values);
    }

    /**
     * The setter of the key's name.
     *
     * @param name The new name.
     * @return this, to allow concatenation.
     */
    public Key setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * The setter of the keyCode values
     *
     * @param values An ArrayList of the new keyCodes.
     * @return this, to allow concatenation.
     *         Note: It will take a copy of the argument.
     */
    public Key setValues(ArrayList<Integer> values) {
        // Clone the values.
        this.values = new ArrayList<>(values);
        return this;
    }

    /**
     * The equals method of the key.
     *
     * @param other The object to compare.
     * @return True if the key is equal to the other key. False otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Key)) {
            return false;
        }
        Key otherKey = (Key) other;
        return this.name.equals(otherKey.name) && this.values.equals(otherKey.values);
    }

    /**
     * The addKeyValue method adds a value to the list of values.
     *
     * @param value The value to add to the list of values.
     * @return self The key with the added value.
     */
    public Key addKeyValue(int value) {
        values.add(value);
        return this;
    }

    /**
     * The removeKeyValue method removes a value from the list of values.
     *
     * @param value The value to remove from the list of values.
     * @return True if the value was removed, false otherwise (if the value was not
     *         in the list).
     * @see Key
     */
    public boolean removeKeyValue(int value) {
        // We must force the use of the ArrayList<>.remove(Object) method, so we must
        // convert the value to an object.
        return values.remove(Integer.valueOf(value));
    }

    /**
     * The checkAccess method checks if the key contains the argument keycode.
     *
     * @param key The keycode to check.
     * @return True if the key has all the privileges of the argument key, false
     *         otherwise.
     */
    public boolean checkAccess(int key) {
        return values.contains(key);
    }

    /**
     * The clone method creates a copy of the key.
     *
     * @return A copy of the key.
     */
    public Key clone() {
        Key key = new Key(name);
        key.values = (ArrayList<Integer>) values.clone();
        return key;
    }

    /**
     * The toString method returns a string representation of the key.
     *
     * @return A string representation of the key.
     */
    public String toString() {
        return "Key - " + name;
    }

}