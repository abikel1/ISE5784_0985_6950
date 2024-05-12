/**
 * Unit tests for primitives.Vector class
 * @author Avital Shenker
 * @author Ayala Bikel
 */
package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class VectorTest {

    Vector v1         = new Vector(1, 2, 3);
    Vector v1Opposite = new Vector(-1, -2, -3);
    Vector v2         = new Vector(-2, -4, -6);
    Vector v3         = new Vector(0, 3, -2);
    Vector v4         = new Vector(1, 2, 2);
    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void add()
    {
        assertThrows(IllegalArgumentException.class,()->v1.add(v1Opposite),"ERROR: Vector + -itself does not throw an exception");
        assertEquals(v1.add(v2),v1Opposite,"ERROR: Vector + Vector does not work correctly");
    }
    /**
     * Test method for {@link primitives.Vector#scale(primitives.Vector)}.
     */
    @Test
    void scale()
    {
    }
    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void dotProduct()
    {
        assertEquals(v1.dotProduct(v3),0,"ERROR: dotProduct() for orthogonal vectors is not zero");
        assertEquals(v1.dotProduct(v2),-28,"ERROR: dotProduct() wrong value");
    }
    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void crossProduct()
    {
        assertThrows(IllegalArgumentException.class,()->v1.crossProduct(v2),"ERROR: crossProduct() for parallel vectors does not throw an exception");
        Vector vr = v1.crossProduct(v3);
        assertEquals(v1.length() * v3.length(),vr.length(),0.0001,"ERROR: crossProduct() wrong result length");
        assertTrue(isZero(vr.dotProduct(v1)),"ERROR: crossProduct() result is not orthogonal to its operands");
        assertTrue(isZero(vr.dotProduct(v3)),"ERROR: crossProduct() result is not orthogonal to its operands");
    }
    /**
     * Test method for {@link primitives.Vector#lengthSquared(primitives.Vector)}.
     */
    @Test
    void lengthSquared()
    {
        assertEquals(v4.lengthSquared(),9,"ERROR: lengthSquared() wrong value");
    }
    /**
     * Test method for {@link primitives.Vector#length(primitives.Vector)}.
     */
    @Test
    void length()
    {
        assertEquals(v4.length(),3,"ERROR: length() wrong value");
    }
    /**
     * Test method for {@link primitives.Vector#normalize(primitives.Vector)}.
     */
    @Test
    void normalize()
    {
        Vector v = new Vector(1, 2, 3);
        Vector u = v.normalize();
        assertEquals(u.length(),1,0.0001,"ERROR: the normalized vector is not a unit vector");
        assertThrows(IllegalArgumentException.class,()->v.crossProduct(u),"ERROR: the normalized vector is not parallel to the original one");
        assertTrue(v.dotProduct(u) > 0,"ERROR: the normalized vector is opposite to the original one");
    }
}