package primitives;
/**
 * This class represents a vector in three-dimensional space.
 * Inherits from the Point class.
 */
public class Vector extends Point
{
    /**
     * Constructs a new Vector object with the given xyz values.
     * Throws an IllegalArgumentException if the vector is zero.
     * @param xyz The coordinates of the vector in three dimensions.
     * @throws IllegalArgumentException If the vector is zero.
     */
    public Vector(Double3 xyz)
    {
        super(xyz);
        if(this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector is zero");
    }
    public Vector(double x, double y, double z)
    {
        super(x, y, z);
        if(this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector is zero");
    }
    public Vector add(Vector v)
    {
        return new Vector(this.xyz.add(v.xyz));
    }
    public Vector scale(double num)
    {
        return new Vector(this.xyz.scale(num));
    }
    public double doProduct(Vector v)
    {
        return (xyz.d1*v.xyz.d1)+
                (xyz.d2*v.xyz.d2)+
                (xyz.d3*v.xyz.d3);
    }
    public Vector crossProduct(Vector v)
    {
        return new Vector((xyz.d2*v.xyz.d3)-(xyz.d3*v.xyz.d2),(xyz.d3*v.xyz.d1)-(xyz.d1*v.xyz.d3),(xyz.d1*v.xyz.d2)-(xyz.d2*v.xyz.d1));
    }
    public double lengthSquared()
    {
        return doProduct(this);
    }
    public double lengh()
    {
        return Math.sqrt(lengthSquared());
    }
    public Vector normalize()
    {
        return new Vector(xyz.reduce(lengh()));
    }
    @Override
    public boolean equals(Object obj)
    {
        if(this==obj) return true;
        return (obj instanceof Vector other) && (super.equals(other));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
