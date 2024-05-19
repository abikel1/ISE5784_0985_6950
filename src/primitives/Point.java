package primitives;
/**
 * This class represents a point in three-dimensional space.
 */
public class Point
{
    // The variable xyz holds the point in three dimensions
   final protected Double3 xyz;
    // Static constant representing the zero point
   public static final Point ZERO = new Point(0, 0, 0);
    /**
     * Constructs a new Point object with the given xyz values.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @param z The z-coordinate of the point.
     */
    public Point(double x, double y, double z)
    {
        xyz = new Double3(x,y,z);
    }
    /**
     * Constructs a new Point object with the given Double3 object.
     *
     * @param xyz The coordinates of the point in three dimensions.
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }
    /**
     * Computes the vector from this point to the given point.
     *
     * @param p The point to subtract from this point.
     * @return The vector from this point to the given point.
     */
    public Vector subtract(Point p)
    {
        return new Vector(xyz.subtract(p.xyz));
    }
    /**
     * Computes the point obtained by adding the given vector to this point.
     *
     * @param v The vector to add to this point.
     * @return The point obtained by adding the vector to this point.
     */
    public Point add(Vector v)
    {
        return new Point(xyz.add(v.xyz));
    }
    /**
     * Computes the squared distance between this point and the given point.
     *
     * @param p The point to compute the distance to.
     * @return The squared distance between this point and the given point.
     */
    public double distanceSquared(Point p)
    {
        return  (xyz.d1-p.xyz.d1)*(xyz.d1-p.xyz.d1)+
                (xyz.d2-p.xyz.d2)*(xyz.d2-p.xyz.d2)+
                (xyz.d3-p.xyz.d3)*(xyz.d3-p.xyz.d3);
    }
    /**
     * Computes the distance between this point and the given point.
     *
     * @param p The point to compute the distance to.
     * @return The distance between this point and the given point.
     */
    public double distance(Point p)
    {
        return Math.sqrt(distanceSquared(p));
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return xyz.toString();
    }
    /**
     * get the x value of the point
     *
     * @return x coordinate value
     */
    public double getX() {
        return xyz.d1;
    }

    /**
     * get the y value of the point
     *
     * @return y coordinate value
     */
    public double getY() {
        return xyz.d2;
    }
}
