/**
 * Unit tests for primitives.Point class
 *
 * @author Avital Shenker
 * @author Ayala Bikel
 */
package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    Point p1 = new Point(1, 2, 3);
    Point p2 = new Point(2, 4, 6);
    Point p3 = new Point(2, 4, 5);

    Vector v1 = new Vector(1, 2, 3);
    Vector v1Opposite = new Vector(-1, -2, -3);
    Vector v2 = new Vector(-2, -4, -6);
    Vector v3 = new Vector(0, 3, -2);
    Vector v4 = new Vector(1, 2, 2);

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     * Tests the subtraction of one point from another.
     */
    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple subtraction test
        assertEquals(p1.subtract(p2), v1Opposite, "ERROR: (point2 - point1) does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Subtracting a point from itself should throw an exception
        assertThrows(IllegalArgumentException.class, () -> { p1.subtract(p1); },
                "ERROR: (point - itself) does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     * Tests the addition of a vector to a point.
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple addition test
        assertEquals(p1.add(v1), p2, "ERROR: (point + vector) = other point does not work correctly");
        // TC02: Addition resulting in the origin point
        assertEquals(p1.add(v1Opposite), Point.ZERO, "ERROR: (point + vector) = center of coordinates does not work correctly");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     * Tests the squared distance calculation between two points.
     */
    @Test
    void distanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Distance squared to itself should be zero
        assertEquals(p1.distanceSquared(p1), 0, 0.0001, "ERROR: point squared distance to itself is not zero");
        // TC02: Correct squared distance calculation
        assertEquals(p1.distanceSquared(p3), 9, 0.0001, "ERROR: squared distance between points is wrong");
        assertEquals(p3.distanceSquared(p1), 9, 0.0001, "ERROR: squared distance between points is wrong");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     * Tests the distance calculation between two points.
     */
    @Test
    void distance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Distance to itself should be zero
        assertEquals(p1.distance(p1), 0, 0.0001, "ERROR: point distance to itself is not zero");
        // TC02: Correct distance calculation
        assertEquals(p1.distance(p3), 3, 0.0001, "ERROR: distance between points is wrong");
        assertEquals(p3.distance(p1), 3, 0.0001, "ERROR: distance between points is wrong");
    }
}