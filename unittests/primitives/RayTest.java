package primitives;

import org.junit.jupiter.api.Test;

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
}