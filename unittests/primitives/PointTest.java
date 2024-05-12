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
     */

    @Test
    void subtract() {
        /** Describe **/
        assertEquals(p1.subtract(p2), v1Opposite, "ERROR: (point2 - point1) does not work correctly");

        assertThrows(IllegalArgumentException.class,()->{p1.subtract(p1);},"ERROR: (point - itself) does not throw an exception");

    }

    /**
     * Test method for {@link primitives.Point#add} (primitives.Point)}.
     */

    @Test
    void add()
    {
        assertEquals(p1.add(v1), p2,"ERROR: (point + vector) = other point does not work correctly");
        assertEquals(p1.add(v1Opposite),Point.ZERO,"ERROR: (point + vector) = center of coordinates does not work correctly");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */

    @Test
    void distanceSquared()
    {
        assertEquals(p1.distanceSquared(p1),0,0.0001,"ERROR: point squared distance to itself is not zero");
        assertEquals(p1.distanceSquared(p3),9,0.0001,"ERROR: squared distance between points is wrong");
        assertEquals(p3.distanceSquared(p1),9,0.0001,"ERROR: squared distance between points is wrong");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */

    @Test
    void distance()
    {
        assertEquals(p1.distance(p1),0,0.0001,"ERROR: point distance to itself is not zero");
        assertEquals(p1.distance(p3),3,0.0001,"ERROR: distance between points to itself is wrong");
        assertEquals(p3.distance(p1),3,0.0001,"ERROR: distance between points to itself is wrong");

    }
}