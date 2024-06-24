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
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        LinkedList<GeoPoint> intersections = null;
        for (Intersectable shape : geometries) {
            List<GeoPoint> shapeIntersections = shape.findGeoIntersectionsHelper(ray, maxDistance);
            if (shapeIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>();
                intersections.addAll(shapeIntersections);
            }
        }
        return intersections;
    }

}
