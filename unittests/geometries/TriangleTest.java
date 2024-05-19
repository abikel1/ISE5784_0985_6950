package geometries;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.Triangle;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for the Triangle class.
 */
class TriangleTest {

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     * Tests the getNormal method of the Triangle class.
     */
    @Test
    void testGetNormal()
    {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test for a normal vector on the triangle's surface
        Point[] pts = { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0) };
        Triangle triangle = new Triangle(pts[0], pts[1], pts[2]);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> triangle.getNormal(pts[0]), "");
        // generate the test result
        Vector result = triangle.getNormal(pts[0]);
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Triangle's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 2; ++i) {
            assertTrue(isZero(result.dotProduct(pts[i].subtract(pts[i == 0 ? 2 : i - 1]))),
                    "Triangle's normal is not orthogonal to one of the edges");
        }
    }
    /**
     * Test method for
     * {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Triangle triangle = new Triangle(new Point(1, 0, 0), new Point(1, 1, 0), new Point(1, 0, 1));

        // ============ Equivalence Partitions Tests ==============
        // TC01: a simple case of a ray that intersects the triangle
        assertEquals(triangle.findIntersections(new Ray(new Point(0.25, 0.25, 0.25), new Vector(2, 0.5, 0.5))),
                List.of(new Point(1,0.4375,0.4375)), "TC01: ERROR: have to be only 1 intersection");

        // TC02: a simple case of a ray that does not intersect the triangle against the
        // edge
        assertNull(triangle.findIntersections(new Ray(new Point(2, 0, 2), new Vector(-2, 0, 2))),
                "TC02: ERROR: have to be no intersection");

        // TC03: a simple case of a ray that does not intersect the triangle against the
        // vertex
        assertNull(triangle.findIntersections(new Ray(new Point(1, -0.5, -0.5), new Vector(-1, -0.5, -0.5))),
                "TC03: ERROR: have to be no intersection");

        // =============== Boundary Values Tests ==================
        // TC11: a ray that intersects the triangle on the vertex
        assertNull(triangle.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 0, 0))),
                "TC11: ERROR: have to be no intersection on the vertex");

        // TC12: a ray that intersects the triangle on the edge
        assertNull(triangle.findIntersections(new Ray(new Point(-1, 0.5, 0), new Vector(1, 0.5, 0))),
                "TC12: ERROR: have to be no intersection on the edge");

        // TC13: a ray on the continuance of the edge
        assertNull(triangle.findIntersections(new Ray(new Point(-1, 0, 2), new Vector(1, 0, 2))),
                "TC13: ERROR: have to be no intersection on the edge");

    }
}