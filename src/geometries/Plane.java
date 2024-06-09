package geometries;

import primitives.Point;
import primitives.Ray;
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
    public List<Point> findIntersections(Ray ray) {
        // TODO Auto-generated method stub
        // get ray point and vector
        Point rayPoint = ray.head;
        Vector rayVector = ray.direction;

        // check if the ray is parallel to the plane
        if (isZero(normal.dotProduct(rayVector))) // dotProduct = 0 => parallel
            return null;
        // check if the ray and the plane start at the same point
        if (ray.head.equals(point))
            return null;
        try {

            double t = alignZero((normal.dotProduct(point.subtract(rayPoint))) / (normal.dotProduct(rayVector)));
            // check if the ray starts on the plane
            if (isZero(t))
                return null;
                // check if the ray crosses the plane
            else if (t > 0)
                return List.of(ray.getPoint(t));
                // no intersection between the ray and the plane
            else
                return null;

        } catch (Exception ex) {
            // p.subtract(rayP) is vector zero, which means the ray point is equal to the
            // plane point (ray start on plane)
            return null;
        }
    }
    /**
     * Computes the intersection points of a given {@link Ray} with the plane. If
     * the ray doesn't intersect the plane, the method returns null.
     *
     * @param ray the ray to intersect with the plane
     * @return a list of intersection points if the ray intersects the plane, null
     *         otherwise
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        // Calculate the dot product of the plane's normal vector with the ray's
        // direction vector
        double nv = normal.dotProduct(ray.direction);
        if (isZero(nv)) { // if the dot product is zero, the ray is parallel to the plane and doesn't
            // intersect
            return null;
        }
        try {
            // Calculate the parameter t at which the ray intersects the plane
            Vector pSubtractP0 = point.subtract(ray.head);
            double t = alignZero((normal.dotProduct(pSubtractP0)) / nv);
            return t <= 0 ? null : List.of(new GeoPoint(this, ray.getPoint(t)));
        } catch (Exception ex) { // if an exception occurs during the calculation, return null
            return null;
        }
    }
}
