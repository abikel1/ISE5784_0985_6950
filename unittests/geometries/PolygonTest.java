package geometries;

import org.junit.jupiter.api.Test;
import java.util.List;
import geometries.Plane;
import geometries.Polygon;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Polygon class.
 */
class PolygonTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals.
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     * Tests the constructor of the Polygon class.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertex on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "Constructed a polygon with last point equal to the first point");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a polygon with co-located points");

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     * Tests the getNormal method of the Polygon class.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon's normal is not orthogonal to one of the edges");
    }
    /**
     * Test method for {@link geometries.Polygon#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        // reusable vector for the tests
        Vector v1 = new Vector(0, 0, 1);
        // reusable polygon for the tests
        Polygon pol = new Polygon(new Point(0, 0, 1), new Point(2, 0, 1), new Point(2, 2, 1), new Point(0, 2, 1));
        // reusable plane for the tests
        Plane pl = new Plane(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1));
        // reusable ray for the tests
        Ray ray;
        // two type of error:
        // or the plane intersection incorrect
        // or the polygon intersection incorrect
        String errorInIntersectionPlane = "Wrong intersection with plane";
        String errorBadIntersection = "Bad intersection";

        // ============ Equivalence Partitions Tests ==============
        // TC01: The Point inside polygon
        ray = new Ray(new Point(1, 1, 0), v1);
        assertEquals(List.of(new Point(1, 1, 1)), pol.findIntersections(ray), errorBadIntersection);

        // TC02:Point in on edge
        ray = new Ray(new Point(-1, 1, 0), v1);
        assertEquals(List.of(new Point(-1, 1, 1)), pl.findIntersections(ray), errorInIntersectionPlane);
        assertNull(pol.findIntersections(ray), errorBadIntersection);

        // TC03: Point in on vertex
        ray = new Ray(new Point(-1, -1, 0), v1);
        assertEquals(List.of(new Point(-1, -1, 1)), pl.findIntersections(ray), errorInIntersectionPlane);
        assertNull(pol.findIntersections(ray), errorBadIntersection);

        // =============== Boundary Values Tests ==================
        // TC11: In vertex
        ray = new Ray(new Point(0, 2, 0), v1);
        assertEquals(List.of(new Point(0, 2, 1)), pl.findIntersections(ray), errorInIntersectionPlane);
        assertNull(pol.findIntersections(ray), errorBadIntersection);

        // TC12: On edge
        ray = new Ray(new Point(0, 1, 0), v1);
        assertEquals(List.of(new Point(0, 1, 1)), pl.findIntersections(ray), errorInIntersectionPlane);
        assertNull(pol.findIntersections(ray), errorBadIntersection);

        // TC13: On edge continuation
        ray = new Ray(new Point(0, 3, 0), v1);
        assertEquals(List.of(new Point(0, 3, 1)), pl.findIntersections(ray), errorInIntersectionPlane);
        assertNull(pol.findIntersections(ray), errorBadIntersection);

    }
}