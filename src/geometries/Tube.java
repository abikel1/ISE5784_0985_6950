package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
/**
 * The Tube class represents an infinite tube in 3D space, defined by its axis and radius.
 * It extends the RadialGeometry abstract class.
 */
public class Tube extends RadialGeometry
{
    // The axis of the tube
    final protected Ray axis;
    /**
     * Constructs a new Tube object with the specified axis and radius.
     *
     * @param axis   The axis of the tube.
     * @param radius The radius of the tube.
     */
    public Tube(Ray axis, double radius)
    {
        super(radius);
        this.axis = axis;
    }
    /**
     * Returns the normal vector to the tube at the specified point (not implemented).
     *
     * @param p The point at which to calculate the normal vector.
     * @return Always returns null (not implemented).
     */
    @Override
    public Vector getNormal(Point p)
    {
        return null;
    }
}
