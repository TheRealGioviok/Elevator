package com.gioviok;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.gioviok.Elevator.MovingDirection;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private Simulator simulator;

    @Before
    public void setUp() {
        simulator = new Simulator();
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
                .addNewRole("Manager", "Ground floor", "First floor", "Second floor", "Third floor", "Fourth floor", "Vault")
                .addModelPerson("Dayanand Portolese", 60)
                .addModelPerson("Sayres Saulsbery", 75)
                .addModelPerson("Milo Schop ", 80)
                .addModelPerson("Mario Rossi", 70)
                .addModelPerson("Busbee Jerkins ", 75);
    }

    // Clones test
    /**
     * Test Clone method of Person class.
     */
    @Test
    public void testPersonClone() {
        Person person = new Person("Dayanand Portolese", 60);
        Person personClone = person.clone();
        assertTrue(person.getName().equals(personClone.getName()));
        assertTrue(person.getWeight() == personClone.getWeight());
        assertTrue(person.getKey() == personClone.getKey() || // When key is null
                    person.getKey().equals(personClone.getKey())); // When key is a Key object
    }

    /**
     * Test Clone method of the Key class.
     */
    @Test
    public void testKeyClone() {
        Key key = new Key("TestKey").addKeyValue(1).addKeyValue(5).addKeyValue(8);
        Key keyClone = key.clone();
        assertTrue(key.equals(keyClone));
    }

    /**
     * Test Clone method of the Floor class.
     */
    @Test
    public void testFloorClone() {
        Floor floor = new Floor("TestFloor", 1, 2);
        Floor floorClone = floor.clone();
        assertTrue(floor.equals(floorClone));
    }

    // Test building methods using simulator already initialized
    /**
     * Test getters of building class.
     */
    @Test
    public void testBuildingGetters() {
        Building building = simulator.getBuilding();
        assertTrue(building.getName().equals("Bank"));
        assertTrue(building.getFloors().size() == 6);
        assertTrue(building.getKeys().size() == 4);
        // Get the key named "Manager" through a cycle, and compare it with the key
        // returned by the getKeyNamed method.
        Key managerKey = null;
        for (Key key : building.getKeys()) {
            if (key.getName().equals("Manager")) {
                managerKey = key;
                break;
            }
        }
        // Get the floor named "Vault" through a cycle, and compare it with the floor
        // returned by the getFloorNamed method.
        Floor vaultFloor = null;
        for (Floor floor : building.getFloors()) {
            if (floor.getName().equals("Vault")) {
                vaultFloor = floor;
                break;
            }
        }
        assertTrue(building.getKeyNamed("Manager").equals(managerKey));
        assertTrue(building.getFloorNamed("Vault").equals(vaultFloor));
        assertTrue (building.getPersonNamed("Dayanand Portolese") == null);
        assertTrue (building.getElevator() != null);
        assertTrue (building.numberOfPeople() == building.getPersons().size());
        assertTrue (building.numberOfFloors() == building.getFloors().size());
        assertTrue (building.numberOfKeys() == building.getKeys().size());
        assertTrue (building.getEntranceFloor().getName() == "Ground floor");
    }

    /**
     * Test key constructors, gettersm setters, equals, toString
     */
    @Test
    public void testKey() {
        Key key = new Key(
            "Key Name",
            new ArrayList<Integer>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            )
        );

        assertNotNull(key.getValues());
        assertEquals(key.getName(), "Key Name");
        key.setName("A better key name");
        assertEquals(key.getName(), "A better key name");
        assertTrue(key.equals(new Key("A better key name", new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)))));
        assertTrue(key.equals(key.clone()));
        assertNotEquals(key,null);
        assertNotEquals(key,
            new Object(){
                @Override
                public String toString() {
                    return "I am not a key. I am a dolphin.";
                }
            }
        );
        ArrayList<Integer> values = new ArrayList<Integer>(Arrays.asList(1,2,3));
        key.setValues(values);
        assertTrue(key.getValues().equals(values));
        key.removeKeyValue(1);
        assertTrue(key.getValues().size() == 2);
        assertFalse(key.getValues().equals(values));
        assertFalse(key.getValues().contains(1));
        values.remove(Integer.valueOf(1));
        assertTrue(key.getValues().equals(values));
        assertTrue(key.toString().equals("Key - "+key.getName()));
    }

    /**
     * Test setters. hasKey, requestAccess, equals
     */
    @Test
    public void testFloorSetters() {
        Floor floor = new Floor();
        floor.setName("TestFloor").setDistance(1000).setKey(123);
        assertTrue(floor.getName().equals("TestFloor"));
        assertTrue(floor.getDistance() == 1000);
        assertTrue(floor.getKey() == 123);
        assertTrue(floor.toString().equals(floor.getName()));
        assertTrue(floor.hasKey());
        floor.setKey(0);
        assertFalse(floor.hasKey());
        assertTrue(floor.requestAccess(new Key().addKeyValue(1)));
        floor.setKey(123);
        assertFalse(floor.requestAccess(new Key().addKeyValue(1)));
        assertFalse(floor.requestAccess(null));

        Floor floor2 = floor.clone();
        assertTrue(floor.equals(floor2));
        assertFalse(floor.equals(null));
        assertFalse(floor.equals(new Object(){
            public String toString() {
                return "I am not a floor. I am a dolphin.";
            }
        }));
        floor2.setName("Notequalname");
        assertFalse(floor.equals(floor2));
        floor2.setDistance(1001);
        assertFalse(floor.equals(floor2));
        floor2.setKey(124);
        assertFalse(floor.equals(floor2));
    }
    /**
    * Test removers methods
    */
    @Test
    public void testRemovers(){
        Building building = simulator.getBuilding();
        // Try removing a person that is not in the building
        assertNull(building.removePerson("Dayanand Portolese"));
        // Try remove a key 2 times
        assertTrue(building.removeKey("Manager"));
        assertFalse(building.removeKey("Manager"));
        // Try remove a floor 2 times
        Floor floor = building.getFloorNamed("Vault");
        assertTrue(building.removeFloor(floor));
        assertFalse(building.removeFloor(floor));
        // Try removing a null person
        assertNull(building.removePerson(null));
        // Try removing a person that doesn't exist
        assertNull(building.removePerson("I do not exist"));
        // Try removing a person that does exist
        building.registerPerson(new Person("I exist!",60),null);
        assertNotNull(building.removePerson("I exist!"));
    }


    /**
     * Test setters of building class.
     */
    @Test
    public void testBuildingSetters() {
        Building building = simulator.getBuilding();

        building.setName("TestBuilding");
        assertTrue(building.getName().equals("TestBuilding"));

        building.registerKey(new Key("TestKey").addKeyValue(1));
        assertTrue(building.getKeyNamed("TestKey") != null);

        building.registerPerson(
                new Person("Kenneth Enrico Caselli", 80),
                "Security",
                building.getFloorNamed("Vault"));
        assertTrue(building.getPersonNamed("Kenneth Enrico Caselli") != null);
        // We test the setup method with both a specified floor and a null floor.
        building.setElevator(null);
        building.setupElevator(building.getFloorNamed("First floor"), Elevator.MovingDirection.DOWN, 100, 500);
        assertTrue(building.getElevator() != null);
        building.setElevator(null);
        building.setupElevator(null, Elevator.MovingDirection.DOWN, 100, 500);
        assertTrue(building.getElevator() != null);
        // The current floor should be the entrance floor.
        assertTrue(building.getElevator().getCurrentFloor().getName().equals(building.getEntranceFloor().getName()));
        // Finally we try to reSetup the elevator
        building.setupElevator(building.getFloorNamed("First floor"), Elevator.MovingDirection.DOWN, 100, 500);
        assertTrue(building.getElevator() != null);
        building.setupElevator(null, Elevator.MovingDirection.DOWN, 100, 500);
        assertTrue(building.getElevator() != null);

        // We try to set the entrance to floor named null. We except an exception.
        try {
            building.setEntranceFloorNamed(null);
            fail("Expected an exception");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // We try to set the entrance to floor named "I do not exist". We except an exception.
        try {
            building.setEntranceFloorNamed("I do not exist");
            fail("Expected an exception");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // We try to set the entrance to floor named "First floor". We except no exception.
        assertTrue(building.setEntranceFloorNamed("First floor"));
    }   

    /**
     * Test elevator getters.
     */
    @Test
    public void testElevatorGetters() {
        Elevator elevator = simulator.getBuilding().getElevator();
        assertTrue(elevator.getWeight() == 0);
        assertTrue(elevator.getMaxLoad() == 0);
        assertTrue(elevator.getCurrentFloor().getName() == "Ground floor");
        assertTrue(elevator.getCurrentLoad() == 0);
        assertTrue(elevator.numberOfPeople() == 0);
        // Create mock person and add it to the elevator.
        Person person = new Person() {
            @Override
            public int getWeight() {
                return 80;
            };

            @Override
            public String getName() {
                return "TestPerson";
            };

            @Override
            public Key getKey() {
                return null;
            };

            @Override
            public Floor getCurrentFloor() {
                return simulator.getBuilding().getFloorNamed("Ground floor");
            };

            @Override
            public Building getCurrentBuilding() {
                return simulator.getBuilding();
            };
        };

        elevator.board(person);

        assertTrue(elevator.hasPerson(person));
        assertTrue(elevator.getCurrentLoad() == 80);
        assertTrue(elevator.isOverloaded() == true);
        assertTrue(elevator.numberOfPeople() == 1);
        assertTrue(elevator.disembark(person));
        assertTrue(elevator.getCurrentLoad() == 0);
    }

    /**
     * Test elevator setters.
     */
    @Test
    public void testElevatorSetters() {
        Elevator elevator = simulator.getBuilding().getElevator();
        elevator.setMaxLoad(500);
        assertTrue(elevator.getMaxLoad() == 500);
        elevator.setWeight(100);
        assertTrue(elevator.getCurrentLoad() == 100);
        elevator.setCurrentFloor(simulator.getBuilding().getFloorNamed("Vault"));
        assertTrue(elevator.getCurrentFloor().getName() == "Vault");
    }

    /**
     * Test default case getters and setters
     */
    @Test
    public void testDefaultCaseGettersAndSetters(){
        Building building = simulator.getBuilding();
        Elevator elevator = building.getElevator();
        assertNull(building.getKeyNamed("This Key Does Not Exist"));
    }

    /**
     * Test already added adders
     */
    @Test
    public void testAlreadyAddedAdders(){
        Building building = simulator.getBuilding();
        Elevator elevator = building.getElevator();
        // Person
        Person person = new Person("TestPerson", 80);
        // Register the person
        building.registerPerson(person, "Security", building.getFloorNamed("Vault"));
        // Register it again. We except an exception of type IllegalArgumentException.
        try {
            building.registerPerson(person, "Security", building.getFloorNamed("Vault"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // Register a person with a null floor. We expect it to be registered at the entrance floor.
        building.registerPerson(new Person("TestPerson2", 80), "Security", null);
        assertTrue(building.getPersonNamed("TestPerson2").getCurrentFloor().getName().equals(building.getEntranceFloor().getName()));

        // Key
        Key key = new Key("TestKey").addKeyValue(1);
        // Register the key
        building.registerKey(key);
        // Register it again. We except an exception of type IllegalArgumentException.
        try {
            building.registerKey(key);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }

        // Try adding a person with a key that is not our. We except an exception of type IllegalArgumentException.
        try {
            building.registerPerson(
                    new Person("TestPerson123", 80),
                    "NotRegisteredKey",
                    building.getFloorNamed("Vault"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }

        //Try adding a person to a floor that is not our. We except an exception of type IllegalArgumentException.
        try {
            building.registerPerson(
                    new Person("TestPerson1234", 80),
                    "Security",
                    new Floor("Not our floor",123123,123123));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }

        //Try adding a person while we have no floors. We expect an IllegalStateException.
        Building emptyBuilding = new Building("TestBuilding");
        assertTrue (emptyBuilding.numberOfFloors() == 0);
        try {
            emptyBuilding.registerPerson(
                    new Person("TestPerson", 80),
                    null,
                    building.getFloorNamed("Vault"));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) { /* Success! */ }

        // Try registering a person with the short method
        building.registerPerson(new Person("Much newer person",80), "Security");
        // It should be registered in the entrance floor
        assertTrue(building.getPersonNamed("Much newer person").getCurrentFloor().getName().equals(building.getEntranceFloor().getName()));
        // Try adding a null floor. We except an exception of type IllegalArgumentException.
        try {
            building.addFloor(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // Register a new floor twice
        building.addFloor(new Floor("New Floor",123123,123123));
        try {
            building.addFloor(new Floor("New Floor",123123,123123));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // Register another floor with different name but same height
        try {
            building.addFloor(new Floor("New Floor2",123123,123123));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }

        // We try adding a floor to the empty building. It should be set as entrance
        assertNull(emptyBuilding.getElevator().getCurrentFloor());
        emptyBuilding.addFloor(new Floor("New Floor",123123,123123));
        assertTrue(emptyBuilding.getEntranceFloor().getName().equals("New Floor"));
        // We redo the same thing. It shouldn't be set as entrance.
        emptyBuilding.addFloor(new Floor("New Floor2",123123,123));
        assertFalse(emptyBuilding.getEntranceFloor().getName().equals("New Floor2"));


    }

    /**
     * Test Elevator() constructors
     */
    @Test
    public void testElevatorConstructors(){
        Elevator e1 = new Elevator();
        assertNull(e1.getCurrentFloor());
        assertEquals(e1.getCurrentMovingDirection(), Elevator.MovingDirection.STATIONARY);
        assertTrue(e1.getWeight() == 0); // Since assertTrue(Double, Double) is deprecated (???)
        assertTrue(e1.getMaxLoad() == 0);

        Elevator e2 = new Elevator(null, Elevator.MovingDirection.UP, 50, 300);
        assertNull(e2.getCurrentFloor());
        assertEquals(e2.getCurrentMovingDirection(), Elevator.MovingDirection.UP);
        assertTrue(e2.getWeight() == 50);
        assertTrue(e2.getMaxLoad() == 300);
    }

    /**
     * Test Person() constructors
     */
    @Test
    public void testPersonConstructors(){
        Building building = simulator.getBuilding();
        Person p1 = new Person("TestPerson", 80); // This should go fine
        // Now we try to init with null name, empty name, negative weight, zero weight
        try {
            new Person(null, 80);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        try {
            new Person("", 80);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        try {
            new Person("TestPerson", -80);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        try {
            new Person("TestPerson", 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }

        // Same for the long constructor
        try {
            new Person(null, 80, building, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        try {
            new Person("", 80, building, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        try {
            new Person("TestPerson", -80, building, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        try {
            new Person("TestPerson", 0, building, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
    }

    /**
     * Test Person getters and setters
     */
    @Test
    public void testPersonGettersAndSetters(){
        Building building = simulator.getBuilding();
        Person p1 = new Person("TestPerson", 80);
        assertTrue(p1.getName().equals("TestPerson"));
        assertTrue(p1.getWeight() == 80);
        assertNull(p1.getCurrentBuilding());
        // Try to get current Floor. It should throw IllegalStateException
        try {
            p1.getCurrentFloor();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) { /* Success! */ }
        assertNull(p1.getKey());
        // Try to set a null name. We expect an IllegalArgumentException.
        try {
            p1.setName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // Try to set an empty name. We expect an IllegalArgumentException.
        try {
            p1.setName("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // Try to set a negative weight. We expect an IllegalArgumentException.
        try {
            p1.setWeight(-80);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // Try to set a zero weight. We expect an IllegalArgumentException.
        try {
            p1.setWeight(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // Set the weight to a positive value. It should be fine.
        assertEquals(p1.setWeight(70),p1);
        // Try to set the name to a normal name. It should be fine.
        assertEquals(p1.setName("NewName"),p1);
        // Try setting current floor. It should throw IllegalStateException (since we are not in a building)
        try {
            p1.setCurrentFloor(new Floor("New Floor",123123,123123));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) { /* Success! */ }
        // Try setting current building. It should be fine.
        p1.setCurrentBuilding(building);
        // Try setting current floor to a floor not in the building. We expect an IllegalArgumentException.
        try {
            p1.setCurrentFloor(new Floor("New Floor",123123,123123));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // Try setting current floor to a floor in the building. It should be fine.
        p1.setCurrentFloor(building.getEntranceFloor());
        // Assert that the toString returns "Person: NewName - Weight: 70"
        assertTrue(p1.toString().equals("Person: NewName - Weight: 70"));
    }

    /**
     * Test Person-Elevator interactions
     */
    @Test
    public void testPersonElevatorInteractions(){
        // Create a person.
        Person p1 = new Person("TestPerson", 80);
        // Try to call the elevator. It should throw IllegalStateException (since we are not in a building)
        try {
            p1.callElevatorRide();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) { /* Success! */ }
        // Try to call chooseFloor. It should throw IllegalStateException (since we are not in a building)
        try {
            p1.chooseFloor(new Floor("New Floor",123123,123123));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) { /* Success! */ }
        // Try to disembark. It should throw IllegalStateException (since we are not in a building)
        try {
            p1.disembark();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) { /* Success! */ }
        // Try to board. It should throw IllegalStateException (since we are not in a building)
        try {
            p1.board();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) { /* Success! */ }
        // Trick the person into thinking that it is in a building with no elevator.
        p1.setCurrentBuilding(new Building(){
            @Override
            public Elevator getElevator() {
                return null;
            }
        });
        // Try to board. It should throw IllegalStateException (since the elevator is null)
        try {
            p1.board();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) { /* Success! */ }
        // Try to disembark. It should throw IllegalStateException (since the elevator is null)
        try {
            p1.disembark();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) { /* Success! */ }

        Building building = simulator.getBuilding();
        Elevator elevator = building.getElevator();
        building.registerPerson(p1, "Manager");
        assertTrue(p1.getCurrentBuilding() == building);
        assertTrue(p1.getCurrentFloor() == building.getEntranceFloor());
        // TP elevator to fourth floor
        elevator.setCurrentFloor(building.getFloorNamed("Fourth floor"));
        // Try to call the elevator. It should now be fine
        assertTrue(p1.callElevatorRide());
        // Try to call the elevator again. We expect False
        assertFalse(p1.callElevatorRide());
        // Try to call chooseFloor. It should now be fine
        assertTrue(p1.chooseFloor(building.getFloorNamed("Third floor")));
        // Try to call chooseFloor again. We expect False
        assertFalse(p1.chooseFloor(building.getFloorNamed("Third floor")));
        // Try to call chooseFloor with a floor not in the building. We expect an IllegalArgumentException.
        try {
            p1.chooseFloor(new Floor("New Floor",123123,123123));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // Try to call chooseFloor a null floor. We expect an IllegalArgumentException.
        try {
            p1.chooseFloor(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }

    }

    /**
     * Test Person Catalog methods (registerPerson, getPersonNamed, removePerson)
     */
    @Test
    public void testPersonCatalogMethods(){
        // We create a person
        Person dummy = new Person("Dummy", 80);
        // We register it
        Person.registerPerson(dummy);
        // We check if it is registered
        assertTrue(Person.getPersonNamed("Dummy") == dummy);
        // We try to register it again. We except an exception of type IllegalArgumentException.
        try {
            Person.registerPerson(dummy);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // We remove the person
        assertTrue(Person.removePerson("Dummy"));
        // We check if it is not registered
        assertNull(Person.getPersonNamed("Dummy"));
        // We try to remove it again. We except false.
        assertFalse(Person.removePerson("Dummy"));
    }

    /**
     * Test elevator logic methods
     */
    @Test
    public void testElevatorLogicMethods(){
        Building building = simulator.getBuilding();
        Elevator elevator = building.getElevator();
        // Try calling an elevator to null floor. We expect an IllegalArgumentException.
        try {
            building.callElevator(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }

        // TP elevator to entrance
        elevator.setCurrentFloor(building.getEntranceFloor());
        // We force the elevator to move down
        elevator.setup(elevator.getCurrentFloor(), Elevator.MovingDirection.DOWN, elevator.getWeight(), elevator.getMaxLoad());
        // Try calling an elevator to requestRide the floor he is already on. We expect to return false.
        assertFalse(elevator.requestRide(elevator.getCurrentFloor(), null));
        // Try calling the elevator to requestRide to a floor we don't have the key to. We expect an IllegalArgumentException.
        try {
            elevator.requestRide(building.getFloorNamed("Vault"), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // Try requesting a ride to a null floor. We expect an IllegalArgumentException.
        try {
            elevator.requestRide(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
    }

    /**
     * Test Elevator.requestHere(Floor)
     */
    @Test
    public void testElevatorRequestHere(){
        Building building = simulator.getBuilding();
        Elevator elevator = building.getElevator();
        // Try calling an elevator to null floor. We expect an IllegalArgumentException.
        try {
            elevator.requestHere(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }

        // Try calling elevator to same floor he is. We expect false.
        assertFalse(elevator.requestHere(elevator.getCurrentFloor()));

        //Stop elevator.
        elevator.halt();
        // TP elevator to entrance
        elevator.setCurrentFloor(building.getEntranceFloor());
        // Try calling elevator to first floor
        assertTrue(elevator.requestHere(building.getFloorNamed("First floor")));
        assertEquals(elevator.getCurrentMovingDirection(), Elevator.MovingDirection.UP);
        // Stop elevator.
        elevator.halt();
        // Try calling elevator to vault
        assertTrue(elevator.requestHere(building.getFloorNamed("Vault")));
        assertEquals(elevator.getCurrentMovingDirection(), Elevator.MovingDirection.DOWN);
        // Try calling elevator again to the vault. We expect false
        assertFalse(elevator.requestHere(building.getFloorNamed("Vault")));
    }

    /**
     * Test Elevator.run()
     */
    @Test
    public void testElevatorRun(){
        Building building = simulator.getBuilding();
        Elevator elevator = building.getElevator();
        // Try calling run with no floors to go. We expect true.
        assertTrue(elevator.run());
        // Add two floor to the queue.
        elevator.requestHere(building.getFloorNamed("First floor"));
        elevator.requestHere(building.getFloorNamed("Second floor"));
        // Try calling run with a floor to go. We expect false.
        assertFalse(elevator.run());
        // Try overloading the elevator.
        Person person = new Person("More like a truck",12300);
        building.registerPerson(person, "Security");
        // Reset elevator to entrance.
        elevator.setCurrentFloor(building.getEntranceFloor());
        // board passenger
        elevator.board(person);
        // Try calling run with a floor to go. We expect illegal state exception.
        try {
            elevator.run();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) { /* Success! */ }
    }

    /**
     * Test elevator.board(Person)
     */
    @Test
    public void testElevatorBoard(){
        Building building = simulator.getBuilding();
        Elevator elevator = building.getElevator();
        // Try calling board with null person. We expect an IllegalArgumentException.
        try {
            elevator.board(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // TP elevator to entrance
        elevator.setCurrentFloor(building.getEntranceFloor());
        assertEquals(elevator.getCurrentFloor().getName(),"Ground floor");
        // Create a person on a different floor.
        Person person = new Person("Person",80);
        building.registerPerson(person, "Security",building.getFloorNamed("First floor"));
        // Try calling board with a person on another floor. We expect IllegalArgumentException.
        try {
            elevator.board(person);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // TP the person to the elevator.
        person.setCurrentFloor(building.getEntranceFloor());
        // Try boarding the same person 2 times. We expect IllegalArgumentException.
        assertTrue(elevator.board(person)); // This should be true.
        try {
            elevator.board(person);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ } // This should throw an IllegalArgumentException.
    }

    /**
     * Test Elevator.disembark(Person)
     */
    @Test
    public void testElevatorDisembark(){
        Building building = simulator.getBuilding();
        Elevator elevator = building.getElevator();
        // Try calling disembark with null person. We expect an IllegalArgumentException.
        try {
            elevator.disembark(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
        // Create a person and board it
        assertTrue(elevator.board(building.registerPerson(new Person("Person",60), null)));
        // Try calling disembark with a person not on the elevator. We expect IllegalArgumentException.
        try {
            elevator.disembark(new Person("Person",60));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) { /* Success! */ }
    }

    /**
     * Test elevator logic.
     * It runs a fairly complicated simulation.
     * Initially, 5 people are registered:
     * A) Manager  - starts at the Fourth floor
     * B) Security - starts at the Vault
     * C) Employee - starts at the Ground floor
     * D) Customer - starts at the Ground floor
     * E) No key   - starts at the Ground floor
     * The elevator is initially at the Ground floor. It is not moving and has no requests.
     * A calls the elevator to the Fourth Floor. The elevator is at the Ground floor.
     * The elevator moves to the Fourth Floor.
     * A boards the elevator. The elevator is at the Fourth Floor.
     * A wants to go to the Vault. The elevator is at the Fourth Floor.
     * C calls the elevator to the Ground Floor. The elevator is at the Fourth Floor.
     * The elevator moves to the Ground Floor.
     * C boards the elevator. The elevator is at the Ground Floor.
     * C wants to go to the First Floor. The elevator is at the Ground Floor.
     * The elevator moves to the Vault, since it is still going downwards.
     * The elevator stops at the Vault.
     * A gets off the elevator. The elevator is at the Vault.
     * D calls the elevator to the Ground Floor. The elevator is at the Vault.
     * The elevator moves to the Ground Floor.
     * D boards the elevator. The elevator is at the Ground Floor.
     * D wants to go to the First Floor. The elevator is at the Ground Floor.
     * The elevator moves to the First Floor.
     * D and C get off the elevator. The elevator is at the First Floor.
     * B calls the elevator to the Vault. The elevator is at the First Floor.
     * The elevator moves to the Vault.
     * B gets on the elevator. The elevator is at the Vault.
     * B wants to go to the Second Floor. The elevator is at the Vault.
     * The elevator moves to the Second Floor.
     * End of simulation.
     * 
     * We will check that the final status should be the following:
     * A) Manager  - Vault
     * B) Security - Second floor
     * C) Employee - First floor
     * D) Customer - First floor
     * E) No key   - Ground floor
     * The elevator should be at the Second floor.
     * The elevator should be empty.
     */
    @Test
    public void testElevatorLogicSim() {
        Building building = simulator.getBuilding();
        Elevator elevator = building.getElevator();
        // Register 5 people.
        building.registerPerson(
                new Person("A", 80),
                "Manager",
                building.getFloorNamed("Fourth floor"));
        building.registerPerson(
                new Person("B", 75),
                "Security",
                building.getFloorNamed("Vault"));
        building.registerPerson(
                new Person("C", 70),
                "Employee",
                building.getFloorNamed("Ground floor"));
        building.registerPerson(
                new Person("D", 60),
                "Customer",
                building.getFloorNamed("Ground floor"));
        building.registerPerson(
                new Person("E", 70),
                null,
                building.getFloorNamed("Ground floor"));
        // Assert there are 5 people registered.
        assertTrue(building.numberOfPeople() == 5);
        // Setup elevator.
        elevator.setup(
            building.getFloorNamed("Ground floor"), 
            Elevator.MovingDirection.UP, 
            100, 
            500
        );
        // Assert elevator is at the Ground floor.
        assertTrue(elevator.getCurrentFloor().getName() == "Ground floor");
        // * A calls the elevator to the Fourth Floor. The elevator is at the Ground floor.
        assertTrue(building.getPersonNamed("A").callElevatorRide());
        // * The elevator is still at the Ground floor.
        assertTrue(elevator.getCurrentFloor().getName() == "Ground floor");
        // * The elevator moves to the Fourth Floor. It has no more floors to visit for now.
        assertTrue(elevator.run());
        // * A boards the elevator. The elevator is at the Fourth Floor.
        assertTrue(building.getPersonNamed("A").board());
        assertTrue(elevator.getCurrentFloor().getName() == "Fourth floor");
        // * A wants to go to the Vault. The elevator is at the Fourth Floor.
        assertTrue(elevator.getCurrentFloor().getName() == "Fourth floor");
        assertTrue(elevator.getCurrentLoad() == 80 + elevator.getWeight());
        assertFalse(elevator.isOverloaded());
        assertTrue(building.getPersonNamed("A").chooseFloor(building.getFloorNamed("Vault")));
        // * C calls the elevator to the Ground Floor. The elevator is at the Fourth Floor.
        assertTrue(building.getPersonNamed("C").callElevatorRide());
        // * The elevator is still at the Fourth Floor.
        assertTrue(elevator.getCurrentFloor().getName() == "Fourth floor");
        // * The elevator moves to the Ground Floor. It still has to go to the Vault.
        assertFalse(elevator.run());
        assertTrue(elevator.getCurrentFloor().getName() == "Ground floor");
        // * C boards the elevator. The elevator is at the Ground Floor.
        assertTrue(building.getPersonNamed("C").board());
        assertTrue(elevator.getCurrentFloor().getName() == "Ground floor");
        assertTrue(elevator.numberOfPeople() == 2);
        // * C wants to go to the First Floor. The elevator is at the Ground Floor.
        assertTrue(elevator.getCurrentFloor().getName() == "Ground floor");
        assertTrue(elevator.getCurrentLoad() == 80 + 70 + elevator.getWeight());
        assertFalse(elevator.isOverloaded());
        assertTrue(building.getPersonNamed("C").chooseFloor(building.getFloorNamed("First floor")));
        // * The elevator moves to the Vault, since it is still going downwards.
        assertFalse(elevator.run());
        // * The elevator stops at the Vault.
        assertTrue(elevator.getCurrentFloor().getName() == "Vault");
        // * A gets off the elevator. The elevator is at the Vault.
        assertTrue(building.getPersonNamed("A").disembark());
        assertTrue(elevator.getCurrentFloor().getName() == "Vault");
        // * D calls the elevator to the Ground Floor. The elevator is at the Vault.
        assertTrue(building.getPersonNamed("D").callElevatorRide()); 
        assertTrue(elevator.getCurrentFloor().getName() == "Vault");
        // * The elevator moves to the Ground Floor. It still has to go to the First Floor.
        assertFalse(elevator.run());
        // * D boards the elevator. The elevator is at the Ground Floor.
        assertTrue(building.getPersonNamed("D").board());
        assertTrue(elevator.getCurrentFloor().getName() == "Ground floor");
        // * D wants to go to the First Floor. The elevator is at the Ground Floor.
        assertFalse(building.getPersonNamed("D").chooseFloor(building.getFloorNamed("First floor"))); // Since it was already on the job list
        assertTrue(elevator.getCurrentFloor().getName() == "Ground floor");
        // * The elevator moves to the First Floor. It has no more jobs for now.
        assertTrue(elevator.run());
        assertTrue(elevator.getCurrentFloor().getName() == "First floor");
        // * D and C get off the elevator. The elevator is at the First Floor.
        assertTrue(building.getPersonNamed("D").disembark());
        assertTrue(building.getPersonNamed("C").disembark());
        assertTrue(elevator.getCurrentFloor().getName() == "First floor");
        assertTrue(elevator.numberOfPeople() == 0);
        // * B calls the elevator to the Vault. The elevator is at the First Floor.
        assertTrue(building.getPersonNamed("B").callElevatorRide());
        assertTrue(elevator.getCurrentFloor().getName() == "First floor");
        // * The elevator moves to the Vault. It has no more floors to visit for now.
        assertTrue(elevator.run());
        assertTrue(elevator.getCurrentFloor().getName() == "Vault");
        // * B gets on the elevator. The elevator is at the Vault.
        assertTrue(building.getPersonNamed("B").board());
        assertTrue(elevator.getCurrentFloor().getName() == "Vault");
        // * B wants to go to the Second Floor. The elevator is at the Vault.
        assertTrue(elevator.getCurrentFloor().getName() == "Vault");
        assertTrue(building.getPersonNamed("B").chooseFloor(building.getFloorNamed("Second floor")));
        // * The elevator moves to the Second Floor. It has no more floors to visit for now.
        assertTrue(elevator.run());
        assertTrue(elevator.getCurrentFloor().getName() == "Second floor");
        // * B gets off the elevator. The elevator is at the Second Floor.
        assertTrue(building.getPersonNamed("B").disembark());
        // FINAL CONDITIONS:
        /*
         * We will check that the final status should be the following:
         * A) Manager - Vault
         * B) Security - Second floor
         * C) Employee - First floor
         * D) Customer - First floor
         * E) No key - Ground floor
         * The elevator should be at the Second floor.
         * The elevator should be empty.
         */
        assertTrue(elevator.getCurrentFloor().getName() == "Second floor");
        assertTrue(elevator.numberOfPeople() == 0);
        assertTrue(elevator.getCurrentLoad() == elevator.getWeight());
        assertFalse(elevator.isOverloaded());
        assertTrue(building.getPersonNamed("A").getCurrentFloor() == building.getFloorNamed("Vault"));
        assertTrue(building.getPersonNamed("B").getCurrentFloor() == building.getFloorNamed("Second floor"));
        assertTrue(building.getPersonNamed("C").getCurrentFloor() == building.getFloorNamed("First floor"));
        assertTrue(building.getPersonNamed("D").getCurrentFloor() == building.getFloorNamed("First floor"));
        assertTrue(building.getPersonNamed("E").getCurrentFloor() == building.getFloorNamed("Ground floor"));
    }
}
