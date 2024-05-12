package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    @Test
    void getNormal()
    {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Sphere sphere=new Sphere(1,new Point(0,0,0));
        // ensure there are no exceptions
        assertDoesNotThrow(() -> sphere.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = sphere.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Sphere's normal is not a unit vector");
    }
}