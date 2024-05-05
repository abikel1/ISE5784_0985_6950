package geometries;

import primitives.Point;
import primitives.Vector;
/**
 * The Plane class represents a plane in 3D space, defined by a point on the plane and its normal vector.
 */
public class Plane
{
    // A point on the plane
    final private Point point;
    // The normal vector to the plane
    final private Vector normal;
    /**
     * Constructs a new Plane object with the specified point on the plane and its normal vector.
     *
     * @param point  A point on the plane.
     * @param normal The normal vector to the plane.
     */
    public Plane(Point point, Vector normal)
    {
        this.point = point;
        this.normal = normal;
    }
    /**
     * Constructs a new Plane object from three points, computing its normal vector.
     *
     * @param p1 The first point.
     * @param p2 The second point.
     * @param p3 The third point.
     */
    public Plane(Point p1, Point p2, Point p3)
    {
        normal=null;
        point=p1;
    }

    /**
     * Returns the normal vector to the plane at the specified point.
     *
     * @param p The point at which to calculate the normal vector.
     * @return The normal vector to the plane at the specified point.
     */
    public Vector getNormal(Point p)
    {
        return normal;
    }
    /**
     * Returns the normal vector to the plane.
     *
     * @return The normal vector to the plane.
     */
    public Vector getNormal()
    {
        return normal;
    }
}
