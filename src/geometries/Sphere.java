package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * The Sphere class represents a sphere in 3D space, defined by its center point and radius.
 * It extends the RadialGeometry abstract class.
 */
public class Sphere extends RadialGeometry
{
    // The center point of the sphere
    final private Point center;
    /**
     * Constructs a new Sphere object with the specified center point and radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Point center,double radius)
    {
        super(radius);
        this.center = center;
    }
    /**
     * Returns the normal vector to the sphere at the specified point (not implemented).
     *
     * @param p The point at which to calculate the normal vector.
     * @return Always returns null (not implemented).
     */
    @Override
    public Vector getNormal(Point p)
    {
        return (p.subtract(center));
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // If the beginning point of the ray is on the sphere center, return the point on the sphere's radius
        if (ray.head.equals(center))
            return List.of(ray.getPoint(radius));

        Vector u = center.subtract(ray.head);
        double tM = alignZero(ray.direction.dotProduct(u));
        double d = alignZero(Math.sqrt(u.lengthSquared() - tM * tM));
        double tH = alignZero(Math.sqrt(radius * radius - d * d));
        double t1 = alignZero(tM + tH);
        double t2 = alignZero(tM - tH);

        // If there are no intersections, return null
        if (d > radius)
            return null;

        if (t1 <= 0 && t2 <= 0)
            return null;

        // If there are two intersections, return them as a list
        if (t1 > 0 && t2 > 0)
            return List.of(ray.getPoint(t1), ray.getPoint(t2));

        // If there is one intersection, return it as a list
        if (t1 > 0)
            return List.of(ray.getPoint(t1));
        else
            return List.of(ray.getPoint(t2));
    }
}
