package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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
        double t = this.axis.direction.dotProduct(p.subtract(this.axis.head));
        if(isZero(t))
        {
            return p.subtract(this.axis.head).normalize();
        }
        // Point o = this.axisRay.getP0().add(this.axisRay.getDir().scale(t));
        Point o = this.axis.getPoint(t);
        return p.subtract(o).normalize();

    }

    /**
     *
     * This method finds the intersections of the given ray with this cylinder.
     *
     * @param ray the ray to intersect with this cylinder.
     * @return A list of intersection points, or null if there are no intersections.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        Vector dir = ray.direction;
        Vector v = axis.direction;
        double dirV = dir.dotProduct(v);

        if (ray.head.equals(axis.head)) { // In case the ray starts on the p0.
            if (isZero(dirV))
                return List.of(new GeoPoint(this, ray.getPoint(radius)));

            if (dir.equals(v.scale(dir.dotProduct(v))))
                return null;

            return List.of(new GeoPoint(this, ray
                    .getPoint(Math.sqrt(radius * radius / dir.subtract(v.scale(dir.dotProduct(v))).lengthSquared()))));
        }

        Vector deltaP = ray.head.subtract(axis.head);
        double dpV = deltaP.dotProduct(v);

        double a = 1 - dirV * dirV;
        double b = 2 * (dir.dotProduct(deltaP) - dirV * dpV);
        double c = deltaP.lengthSquared() - dpV * dpV - radius * radius;

        if (isZero(a)) {
            if (isZero(b)) { // If a constant equation.
                return null;
            }
            return List.of(new GeoPoint(this, ray.getPoint(-c / b))); // if it's linear, there's a solution.
        }

        double discriminant = alignZero(b * b - 4 * a * c);

        if (discriminant < 0) // No real solutions.
            return null;

        double t1 = alignZero(-(b + Math.sqrt(discriminant)) / (2 * a)); // Positive solution.
        double t2 = alignZero(-(b - Math.sqrt(discriminant)) / (2 * a)); // Negative solution.

        if (discriminant <= 0) // No real solutions.
            return null;

        if (t1 > 0 && t2 > 0) {
            List<GeoPoint> _points = new ArrayList<>(2);
            _points.add(new GeoPoint(this, ray.getPoint(t1)));
            _points.add(new GeoPoint(this, ray.getPoint(t2)));
            return _points;
        } else if (t1 > 0) {
            List<GeoPoint> _points = new ArrayList<>(1);
            _points.add(new GeoPoint(this, ray.getPoint(t1)));
            return _points;
        } else if (t2 > 0) {
            List<GeoPoint> _points = new ArrayList<>(1);
            _points.add(new GeoPoint(this, ray.getPoint(t2)));
            return _points;
        }
        return null;
    }
}
