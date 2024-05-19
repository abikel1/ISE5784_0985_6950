package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    void getPoint()
    {
        // ============ Equivalence Partitions Tests ==============
        // TC01: positive distance
        assertEquals(new Point(6.0, 2.0, 3.0), new Ray(new Point(1, 2, 3), new Vector(1, 1, 1)).getPoint(5), "ERROR: positive distance wrong");
        // TC01: negative distance
        assertEquals(new Point(-1, 0, 1), new Ray(new Point(1, 2, 3), new Vector(1, 1, 1)).getPoint(-2), "ERROR: negative distance wrong");
    }
}