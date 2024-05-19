package geometries;
import java.util.List;
import java.util.Objects;
import primitives.Point;
import primitives.Ray;
public interface Intersectable
{
    /**
     * This function returns all the intersection points of the geometry
     *
     * @param ray that may has intersection with the geometry
     * @return list of intersections
     */
    List<Point> findIntersections(Ray ray);
}
