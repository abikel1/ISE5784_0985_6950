package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Plane class represents a plane in 3D space, defined by a point on the plane and its normal vector.
 */
public class Plane extends Geometry
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
        point=p1;
        Vector u = p2.subtract(point);
        Vector v = p3.subtract(point);
        Vector n = u.crossProduct(v);
        normal = n.normalize();
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
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Point rayP0 = ray.head;
        if (rayP0.equals(point))
            return null;
        double denom = normal.dotProduct(ray.direction);
        if (Util.isZero(denom))
            return null;
        double t = Util.alignZero(normal.dotProduct(point.subtract(rayP0)) / denom);
        return t <= 0 || Util.alignZero(t - maxDistance) >= 0 ? null : List.of(new GeoPoint(this, ray.getPoint(t)));
    }

}
