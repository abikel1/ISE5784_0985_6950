package geometries;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import primitives.Point;
import primitives.Ray;

public class Geometries implements Intersectable
{
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
            if (!otherIntersections.isEmpty()) {
                if (intersections.isEmpty()) // if no intersections were inserted yet
                    intersections = new LinkedList<>(); // create a new LinkedList
                intersections.addAll(otherIntersections); // insert all intersections
            }
        }
        return intersections; // return the list of intersections
    }
}
