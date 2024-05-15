package geometries;

import primitives.Point;
import primitives.Vector;
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
}
