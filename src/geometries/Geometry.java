package geometries;

import primitives.Point;
import primitives.Vector;
/**
 * The Geometry interface represents geometric shapes in a 3D space.
 * It provides a method to calculate the normal vector at a given point on the geometry.
 */
public interface Geometry extends Intersectable
{
    /**
     * Calculates the normal vector at the given point on the geometry.
     *
     * @param p The point on the geometry to calculate the normal vector for.
     * @return The normal vector at the given point.
     */
    public Vector getNormal(Point p);
}
