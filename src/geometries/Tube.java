package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        Vector dir = axis.direction;
        Point p0 = axis.head;
        double t = dir.dotProduct(p.subtract(p0));
        if (isZero(t))
            return p.subtract(p0).normalize();
        Point point0 = p0.add(dir.scale(t));
        return p.subtract(point0).normalize();

    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point d, e, rayP0 = ray.head;
        Vector rayDir = ray.direction;
        double dis, ab, bc, ac;

        // initialize the points and vectors of the axis of the cylinder
        Point pointA = rayP0, pointB = axis.head;
        Vector vecA = rayDir, vecB = axis.direction;

        // calculate dot product between ray direction vector and axis vector
        ab = vecA.dotProduct(vecB);

        // check if the vectors are parallel, if so return null
        try {
            vecA.crossProduct(vecB);
        } catch (IllegalArgumentException ex) {
            return null;
        }

        // calculate values for quadratic equation
        double bb = 1;
        double aa = 1;
        try {
            Vector c = pointB.subtract(pointA);
            bc = vecB.dotProduct(c);
            ac = vecA.dotProduct(c);

            double t1 = (-ab * bc + ac * bb) / (aa * bb - ab * ab);
            try {
                d = pointA.add(vecA.scale(t1));
            } catch (IllegalArgumentException ex) {
                d = pointA;
            }

            double t2 = (ab * ac - bc * aa) / (1 - ab * ab);

            try {
                e = pointB.add(vecB.scale(t2));
            } catch (IllegalArgumentException ex) {
                e = pointB;
            }
            dis = d.distance(e);

        } catch (IllegalArgumentException ex) {
            d = rayP0;
            dis = 0;
        }

        // check if the ray misses the cylinder
        double diff = alignZero(dis - radius);
        if (diff > 0.0)
            return null;

        // check if the ray is tangent to the cylinder
        if (diff == 0.0) {
            return null;
        }

        // calculate the width of the intersection at the intersection point
        double width;
        try {
            double sinA = vecA.crossProduct(vecB).length();
            width = radius / sinA;
        } catch (IllegalArgumentException ex) { // it is orthogonal
            width = radius;
        }
        double k = width / radius;

        // calculate the intersection points
        double th = Math.sqrt(radius * radius - dis * dis) * k;

        Point p1 = d.subtract(vecA.scale(th));
        Point p2 = d.add(vecA.scale(th));

        // check which intersection point is in front of the camera
        try {
            if (!(p1.subtract(pointA).dotProduct(vecA) < 0.0) && !(p2.subtract(pointA).dotProduct(vecA) < 0.0)) {
                return List.of(p1, p2);
            }
        } catch (IllegalArgumentException ex) {
        }

        try {
            if (!(p1.subtract(pointA).dotProduct(vecA) < 0.0)) {
                return List.of(p1);
            }
        } catch (IllegalArgumentException ex) {
        }

        try {
            if (!(p2.subtract(pointA).dotProduct(vecA) < 0.0)) {
                return List.of(p2);
            }
        } catch (IllegalArgumentException ex) {
        }

        return null;
    }
}
