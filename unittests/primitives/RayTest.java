package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest
{
    Point p0=new Point(1,2,3);
    Point p1=new Point(-2,0,-8);
    Vector dir=new Vector(1,0,4);

    @Test
    void getPoint()
    {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(p0.add(dir.scale(2)),new Point(3,2,11),"ERROR: calculation in the positive distance");
        assertEquals(p0.add(dir.scale(-2)),new Point(-1,2,-5),"ERROR: calculation in the negative distance");
        // =============== Boundary Values Tests =================
        assertEquals(p1.add(dir.scale(2)),Point.ZERO,"ERROR: calculation is zero");
    }
}