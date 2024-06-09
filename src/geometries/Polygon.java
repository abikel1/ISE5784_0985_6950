package geometries;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.isZero;
import static primitives.Util.compareSign;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;


/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 * @author Dan
 */
public class Polygon extends Geometry {
    /** List of polygon's vertices */
    protected final List<Point> vertices;
    /** Associated plane in which the polygon lays */
    protected final Plane       plane;
    /** The size of the polygon - the amount of the vertices in the polygon */
    private final int size;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     * @param  vertices                 list of vertices according to their order by
     *                                  edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size          = vertices.length;

        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane         = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return; // no need for more tests for a Triangle

        Vector  n        = plane.getNormal();
        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector  edge1    = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector  edge2    = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with the normal. If all the rest consequent edges will generate the same sign
        // - the polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    @Override
    public Vector getNormal(Point point) { return plane.getNormal(); }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // Check if the ray's start point is a vertex of the polygon, return null if so
        Point rayP0 = ray.head;
        for (int i = 0; i < vertices.size(); i++) {
            if (rayP0.equals(this.vertices.get(i)))
                return null;
        }

        // Calculate vectors from ray's start point to each vertex of the polygon
        Vector[] vectorsToP0 = new Vector[this.vertices.size()];
        for (int i = 0; i < vertices.size(); ++i) {
            vectorsToP0[i] = this.vertices.get(i).subtract(rayP0);
        }

        // Calculate normal vectors for each edge of the polygon
        Vector[] normalVectors = new Vector[this.vertices.size()];
        try {
            for (int i = 0; i < vertices.size(); ++i) {
                normalVectors[i] = vectorsToP0[i].crossProduct(vectorsToP0[(i + 1) % vectorsToP0.length]).normalize();
            }
        } catch (IllegalArgumentException e) {
            return null;
        }

        // Calculate dot products between the ray's direction and each normal vector
        Vector rayDir = ray.direction;
        double[] dotProdCal = new double[this.vertices.size()];
        for (int i = 0; i < vertices.size(); ++i) {
            dotProdCal[i] = rayDir.dotProduct(normalVectors[i]);
            if (isZero(dotProdCal[i]))
                return null;
        }

        // Check if all dot products are either positive or negative
        boolean allPositive = true, allNegative = true;
        for (int i = 0; i < vertices.size(); ++i) {
            if (dotProdCal[i] < 0 && allPositive)
                allPositive = false;
            if (dotProdCal[i] > 0 && allNegative)
                allNegative = false;
        }

        // If all dot products have the same sign, find intersections with the polygon's
        // plane
        if (allNegative || allPositive) {
            return this.plane.findIntersections(ray);
        }
        return null;
    }
    /**
     * Computes the intersection points between a given ray and the polygon.
     *
     * @param ray the ray to find intersections with the polygon
     * @return a list of intersection points between the ray and the polygon, or
     *         null if there are no intersections
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {

        int len = vertices.size();
        Point p0 = ray.head;
        Vector v = ray.direction;
        List<Vector> vectors = new ArrayList<>(len);

        // all the vectors
        for (Point vertex : vertices) {
            vectors.add(vertex.subtract(p0));
        }

        int sign = 0;
        for (int i = 0; i < len; i++) {
            // calculate the normal using the formula in the course slides
            Vector n = vectors.get(i).crossProduct(vectors.get((i + 1) % len)).normalize();
            double dotProd = v.dotProduct(n);

            if (i == 0)
                sign = dotProd > 0 ? 1 : -1;

            if (!compareSign(sign, dotProd) || isZero(dotProd))
                return null;
        }
        return plane.findGeoIntersectionsHelper(ray);
    }
}