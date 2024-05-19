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

    private final Point p1 = new Point(1, 2, 3);
    private final Point p2 = new Point(2, 4, 6);
    private final Point p3 = new Point(2, 4, 5);

    private final Vector v1 = new Vector(1, 2, 3);
    private final Vector v1Opposite = new Vector(-1, -2, -3);
    private final Vector v2 = new Vector(-2, -4, -6);
    private final Vector v3 = new Vector(0, 3, -2);
    private final Vector v4 = new Vector(1, 2, 2);
    private final double DELTA = 0.000001;

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
        assertThrows(IllegalArgumentException.class, () -> {
                    p1.subtract(p1);
                },
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
        assertEquals(p1.distanceSquared(p1), 0, DELTA, "ERROR: point squared distance to itself is not zero");
        // TC02: Correct squared distance calculation
        assertEquals(p1.distanceSquared(p3), 9, DELTA, "ERROR: squared distance between points is wrong");
        assertEquals(p3.distanceSquared(p1), 9, DELTA, "ERROR: squared distance between points is wrong");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     * Tests the distance calculation between two points.
     */
    @Test
    void distance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Distance to itself should be zero
        assertEquals(p1.distance(p1), 0, DELTA, "ERROR: point distance to itself is not zero");
        // TC02: Correct distance calculation
        assertEquals(p1.distance(p3), 3, DELTA, "ERROR: distance between points is wrong");
        assertEquals(p3.distance(p1), 3, DELTA, "ERROR: distance between points is wrong");
    }
}