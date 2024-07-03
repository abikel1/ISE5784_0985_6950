package primitives;

import java.util.List;

import static primitives.Util.isZero;
import geometries.Intersectable.GeoPoint;

/**
 * This class represents a ray in three-dimensional space.
 */
public class Ray
{
    // The starting point of the ray
    final public Point head;
    // The direction of the ray
    final public Vector direction;
    private static final double DELTA = 0.1;

    /**
     * Constructs a new Ray object with the given starting point and direction.
     * @param head The starting point of the ray.
     * @param direction The direction of the ray.
     */
    public Ray(Point head, Vector direction)
    {
        this.head = head;
        this.direction = direction.normalize();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "Ray:" + head.toString()+" "+direction.toString();
    }
    /**
     *
     * @param t
     * @return If t is 0 the function returns head otherwise returns a new point which is head + vector direction * scalar
     */
    public Point getPoint(double t)
    {
        try {
            if (isZero(t))
                return head;
            return head.add(direction.scale(t));
        } catch (Exception e) {
            return head;
        }
    }
    /**
     * In the points list - find the point with minimal distance from the ray head
     * point and return it
     *
     * @param points list of intersection point
     * @return the closet point to ray point (p0)
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }
    /**
     * this function return the close {@link GeoPoint} point to the beginning of the
     * ray
     *
     * @param points of GeoPoint points
     * @return the closest GeoPoint point to the beginning of the ray
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> points) {
        if (points == null || points.isEmpty())
            return null;
        GeoPoint closest = null;
        double minDistance = Double.MAX_VALUE;
        for (GeoPoint p : points) {
            double distance = p.point.distance(head);
            if (distance < minDistance) {
                closest = p;
                minDistance = distance;
            }
        }
        return closest;
    }
    /**
     * Constructs a ray with a given starting point, direction, and surface normal.
     *
     * @param p The starting point of the ray.
     * @param dir The direction of the ray (must be already normalized)
     * @param normal The surface normal at the starting point.
     */
    public Ray(Point p, Vector dir, Vector normal) {
        Vector delta = normal.scale(normal.dotProduct(dir) > 0 ? DELTA : -DELTA);
        this.head = p.add(delta);
        this.direction = dir;
    }
}
