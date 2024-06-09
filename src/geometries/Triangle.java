package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Triangle class represents a triangle in 3D space, defined by its three vertices.
 * It extends the Polygon class.
 */
public class Triangle extends Polygon
{
    /**
     * Constructs a new Triangle object with the specified vertices.
     *
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3)
    {
        super(p1, p2, p3);
    }
    public List<Point> findIntersections(Ray ray)
    {
        List<Point> list= plane.findIntersections(ray);
        if (list == null)
            return null;
        // Check if the ray starts at one of the triangle's vertices
        Point rayP0 = ray.head;
        if (rayP0.equals(this.vertices.get(0)) || rayP0.equals(this.vertices.get(1))
                || rayP0.equals(this.vertices.get(2))) {
            return null;
        }

        // Calculate the normals of the triangle's three edges
        Vector v1 = this.vertices.get(0).subtract(rayP0);
        Vector v2 = this.vertices.get(1).subtract(rayP0);
        Vector v3 = this.vertices.get(2).subtract(rayP0);
        Vector n1, n2, n3;
        try {
            n1 = v1.crossProduct(v2).normalize();
            n2 = v2.crossProduct(v3).normalize();
            n3 = v3.crossProduct(v1).normalize();
        } catch (IllegalArgumentException e) {
            return null;
        }

        // Check if the ray is parallel to any of the triangle's edges
        Vector rayDir = ray.direction;
        double vn1 = rayDir.dotProduct(n1);
        double vn2 = rayDir.dotProduct(n2);
        double vn3 = rayDir.dotProduct(n3);
        if (isZero(vn1) || isZero(vn2) || isZero(vn3)) {
            return null;
        }

        // Check if the ray intersects the triangle
        if ((vn1 > 0 && vn2 > 0 && vn3 > 0) || (vn1 < 0 && vn2 < 0 && vn3 < 0)) {
            return this.plane.findIntersections(ray);
        }
        return null;
    }
    /**
     * Computes the intersections of a given ray with this triangle.
     *
     * @param ray the ray to intersect with this triangle
     * @return a list of intersection points between the ray and the triangle, or
     *         null if there are no intersections
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> planeIntersection = this.plane.findGeoIntersections(ray);
        if (planeIntersection == null)
            return null;

        // Check if the ray starts at one of the triangle's vertices
        Point rayP0 = ray.head;
        Vector rayDir = ray.direction;

        // Calculate the normals of the triangle's three edges
        Vector v1 = this.vertices.get(0).subtract(rayP0);
        Vector v2 = this.vertices.get(1).subtract(rayP0);
        Vector n1 = v1.crossProduct(v2).normalize();
        double vn1 = alignZero(rayDir.dotProduct(n1));
        if (vn1 == 0)
            return null;

        Vector v3 = this.vertices.get(2).subtract(rayP0);
        Vector n2 = v2.crossProduct(v3).normalize();
        double vn2 = alignZero(rayDir.dotProduct(n2));
        if (vn1 * vn2 <= 0)
            return null;

        Vector n3 = v3.crossProduct(v1).normalize();
        double vn3 = rayDir.dotProduct(n3);
        if (vn1 * vn3 <= 0)
            return null;

        planeIntersection.get(0).geometry = this;
        return planeIntersection;
    }
}
