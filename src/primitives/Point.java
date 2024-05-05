package primitives;
/**
 * This class represents a point in three-dimensional space.
 */
public class Point
{
    // The variable xyz holds the point in three dimensions
   final protected Double3 xyz;
    /**
     * Constructs a new Point object with the given xyz values.
     * @param xyz The coordinates of the point in three dimensions.
     */
    public Point(double x, double y, double z)
    {
        xyz = new Double3(x,y,z);
    }
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    public Vector subtract(Point p)
    {
        return new Vector(xyz.subtract(p.xyz));
    }
    public Point add(Vector v)
    {
        return new Point(xyz.add(v.xyz));
    }
    public double distanceSquared(Point p)
    {
        return  (xyz.d1-p.xyz.d1)*(xyz.d1-p.xyz.d1)+
                (xyz.d2-p.xyz.d2)*(xyz.d2-p.xyz.d2)+
                (xyz.d3-p.xyz.d3)*(xyz.d3-p.xyz.d3);
    }
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
}
