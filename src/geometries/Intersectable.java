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
         * The geometry information of the point
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
    public final List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }


    /**
     * Finds the geographic intersections of the given ray up to an infinite distance.
     *
     * @param ray The ray for which to find intersections.
     * @return A list of geographic points where the ray intersects.
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Finds the geographic intersections of the given ray up to the specified maximum distance.
     *
     * @param ray The ray for which to find intersections.
     * @param maxDistance The maximum distance to consider for intersections.
     * @return A list of geographic points where the ray intersects up to the specified maximum distance.
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        return findGeoIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Helper method to find the geographic intersections of the given ray up to the specified maximum distance.
     * This method is abstract and must be implemented by subclasses.
     *
     * @param ray The ray for which to find intersections.
     * @param maxDistance The maximum distance to consider for intersections.
     * @return A list of geographic points where the ray intersects up to the specified maximum distance.
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance);

}
