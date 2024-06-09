package geometries;

import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import primitives.Point;
import primitives.Ray;

public class Geometries extends Intersectable {
    private List<Intersectable> geometries=new LinkedList<Intersectable>();
    Geometries(){}
    public Geometries(Intersectable... geometries)
    {
        this();
        add(geometries);
    }
    public void add(Intersectable... geometries)
    {
        Collections.addAll(this.geometries, geometries);
    }

    @Override
    public List<Point> findIntersections(Ray ray)
    {
        List<Point> intersections = null;

        for (Intersectable geo : geometries) { // run on list of geometries
            List<Point> otherIntersections = geo.findIntersections(ray); // find intersections of each geometry
            if (otherIntersections != null && !otherIntersections.isEmpty()) {
                if (intersections == null)
                    intersections = new LinkedList<>();// create a new LinkedList
                intersections.addAll(otherIntersections);// insert all intersections
            }
        }
        return intersections; // return the list of intersections
    }
    /**
     * Finds all the intersections of the given ray with the geometries in the list.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points, or null if there are no intersections.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> res = null;
        for (Intersectable geometry : this.geometries) {
            List<GeoPoint> resi = geometry.findGeoIntersections(ray);
            if (resi != null) {
                if (res == null) {
                    res = new LinkedList<GeoPoint>();
                }
                res.addAll(resi);
            }
        }
        return res;
    }
}
