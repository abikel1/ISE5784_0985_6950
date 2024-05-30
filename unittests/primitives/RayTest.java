package primitives;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTest
{

    @Test
    void getPoint()
    {
        Ray ray=new Ray(new Point(1,0,0),new Vector(1,0,0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for positive distance t>0
        assertEquals(new Point(3,0,0), ray.getPoint(2), "ERROR: getPoint doesn't work for a positive distance");
        // TC02:Test for negative distance t<0
        assertEquals(new Point(-1,0,0), ray.getPoint(-2), "ERROR: getPoint doesn't work for a negative distance");
        // =============== Boundary Values Tests ==================
        // TC11:Test for 0 distance. t=0
        assertEquals(new Point(1,0,0), ray.getPoint(0),
                "ERROR: getPoint doesn't work for no distance, the head of the ray is the expected output");
    }
    @Test
    void testFindClosestPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: The close point to the beginning of the ray in the middle of the list
        Ray ray = new Ray(new Point(2, 0, 0), new Vector(2, 0, 0));
        List<Point> points = new LinkedList<>();
        points.add(new Point(1, 0, 0));
        points.add(new Point(2.1, 0, 0));
        points.add(new Point(3, 0, 0));
        assertEquals(new Point(2.1, 0, 0),
                ray.findClosestPoint(points),"Error: TC01, Should return the point in the middle of the list!");

        // ============ Boundary Values Tests ==============
        // TC11: The close point to the beginning of the ray in the first of the list
        ray = new Ray(new Point(0.9, 0, 0), new Vector(2, 0, 0));
        assertEquals(new Point(1, 0, 0),
                ray.findClosestPoint(points), "Error: TC11, Should return the first point in the list!");

        // TC12: The close point to the beginning of the ray in the last of the list
        ray = new Ray(new Point(3.1, 0, 0), new Vector(2, 0, 0));
        assertEquals(new Point(3, 0, 0),
                ray.findClosestPoint(points),"Error: TC12, Should return the last point in the list!");

        // TC13: The list are empty
        points = new LinkedList<>();
        assertNull(ray.findClosestPoint(points), "Error: TC13, Should return null!");
    }
}