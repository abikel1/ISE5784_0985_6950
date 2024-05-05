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
    /**
     * Constructs a new Vector object with the given x, y, and z values.
     * Throws an IllegalArgumentException if the vector is zero.
     *
     * @param x The x-coordinate of the vector.
     * @param y The y-coordinate of the vector.
     * @param z The z-coordinate of the vector.
     * @throws IllegalArgumentException If the vector is zero.
     */
    public Vector(double x, double y, double z)
    {
        super(x, y, z);
        if(this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector is zero");
    }
    /**
     * Adds the given vector to this vector and returns the result as a new vector.
     *
     * @param v The vector to add.
     * @return A new vector representing the result of the addition.
     */
    public Vector add(Vector v)
    {
        return new Vector(this.xyz.add(v.xyz));
    }
    /**
     * Scales this vector by the given scalar and returns the result as a new vector.
     *
     * @param num The scalar value to scale the vector by.
     * @return A new vector representing the scaled vector.
     */
    public Vector scale(double num)
    {
        return new Vector(this.xyz.scale(num));
    }
    /**
     * Computes the dot product of this vector with the given vector.
     *
     * @param v The vector to compute the dot product with.
     * @return The dot product of the two vectors.
     */
    public double dotProduct(Vector v)
    {
        return (xyz.d1*v.xyz.d1)+
                (xyz.d2*v.xyz.d2)+
                (xyz.d3*v.xyz.d3);
    }
    /**
     * Computes the cross product of this vector with the given vector.
     *
     * @param v The vector to compute the cross product with.
     * @return A new vector representing the cross product.
     */
    public Vector crossProduct(Vector v)
    {
        return new Vector((xyz.d2*v.xyz.d3)-(xyz.d3*v.xyz.d2),(xyz.d3*v.xyz.d1)-(xyz.d1*v.xyz.d3),(xyz.d1*v.xyz.d2)-(xyz.d2*v.xyz.d1));
    }
    /**
     * Computes the squared length of this vector.
     *
     * @return The squared length of the vector.
     */
    public double lengthSquared()
    {
        return dotProduct(this);
    }
    /**
     * Computes the length of this vector.
     *
     * @return The length of the vector.
     */
    public double length()
    {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Returns a new vector representing this vector normalized to unit length.
     *
     * @return A new normalized vector.
     */
    public Vector normalize()
    {
        return new Vector(xyz.reduce(length()));
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
