package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
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
        return (p.subtract(center).normalize());
    }
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Point p0 = ray.head;
        if (center.equals(p0))
            return List.of(new GeoPoint(this, ray.getPoint(radius)));
        Vector u = center.subtract(p0);
        double tm = u.dotProduct(ray.direction);
        double d = Math.sqrt(u.lengthSquared() - tm * tm);
        double dif = Util.alignZero(d - radius);
        if (dif >= 0)
            return null;
        double th = Math.sqrt(radius * radius - d * d);
        double t2 = Util.alignZero(tm + th);
        double t1 = Util.alignZero(tm - th);
        if (t2 <= 0 || Util.alignZero(t1 - maxDistance) >= 0)
            return null;
        if (Util.alignZero(t2 - maxDistance) >= 0)
            return t1 > 0 ? List.of(new GeoPoint(this, ray.getPoint(t1))) : null;
        return t1 > 0 //
                ? List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2))) //
                : List.of(new GeoPoint(this, ray.getPoint(t2)));
    }

}
