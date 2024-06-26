package geometries;
import java.util.List;
import java.util.Objects;

import primitives.Point;
import primitives.Ray;
public abstract class Intersectable
{
    /**
     * Represents a geographic point with associated geometry information.
     */
    public static class GeoPoint {
        /**
         * The geometry information of the point.
         */
        public Geometry geometry;

        /**
         * The actual point coordinates.
         */
        public Point point;

        /**
         * Constructs a GeoPoint object with the specified geometry and point
         * coordinates.
         *
         * @param geometry The geometry information of the point.
         * @param point    The actual point coordinates.
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        /**
         * Checks if this GeoPoint is equal to another object.
         *
         * @param o The object to compare.
         * @return {@code true} if the objects are equal, {@code false} otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            GeoPoint geoPoint = (GeoPoint) o;
            return Objects.equals(geometry, geoPoint.geometry) && point.equals(geoPoint.point);
        }

        /**
         * Returns a string representation of the GeoPoint object.
         *
         * @return A string representation of the object.
         */
        @Override
        public String toString() {
            return "GeoPoint{" + "geometry=" + geometry + ", point=" + point + '}';
        }
    }

    /**
     * Returns all the intersections of ray with the geometry shape.
     *
     * @param ray {@link Ray} pointing toward the object.
     * @return List of intersection {@link Point}s.
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }

    /**
     * Finds the intersection points of the ray with the surface of the object.
     *
     * @param ray The ray to intersect with the GeoPoint.
     * @return A list of GeoPoints that are the intersections of the ray with the
     *         object.
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersectionsHelper(ray);
    }

    /**
     * Finds the intersection points of the ray with the surface of the object. This
     * method should be implemented by the subclasses to provide their own
     * intersection logic.
     *
     * @param ray The ray to intersect with the object.
     * @return A list of GeoPoints that are the intersections of the ray with the
     *         object.
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);
}
