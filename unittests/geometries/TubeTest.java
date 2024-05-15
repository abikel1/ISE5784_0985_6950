package geometries;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for the Tube class.
 */
class TubeTest {

    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     * Tests the getNormal method of the Tube class.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test for a normal vector on the tube's surface
        Tube tube = new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 2);
        assertEquals(tube.getNormal(new Point(0, 2, 5)), new Vector(0, 1, 0),
                "ERROR: The normal is not correct");

        // ========== Boundary Values Tests ==================
        // TC11: Normal at a point making a right angle with the ray source point
        tube = new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1);
        assertEquals(tube.getNormal(new Point(1, 0, 0)), new Vector(1, 0, 0),
                "ERROR: A right angle with the ray source point");
    }
}