package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * The SimpleRayTracer class is a concrete implementation of the SimpleRayTracer
 * abstract class. This class is responsible for tracing rays in the scene and
 * calculating their color. The class implements the traceRay method, which
 * returns the color of the closest point of intersection with an object in the
 * scene. The class also implements the calcColor method, which calculates the
 * color at a given point in the scene.
 *
 * @author Ayala and Avital
*
*/
public class SimpleRayTracer extends RayTracerBase{
    /**
     * Constructs a RayTracerBasic object with the given scene.
     *
     * @param scene the scene to be rendered.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * an inheritance function from base this function returns the color of the
     * closest point to the ray.
     *
     * @param ray - the ray between camera and view plane
     * @return the color - color of closest point
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);// find intersection point
        if (intersections == null) // if there are no intersection points return color of background
            return scene.background;
        Point closestPoint = ray.findClosestPoint(intersections);// find closest point between ray
        return calcColor(closestPoint); // return the color of closestPoint
    }
    /**
     * calculate the scene color
     * @param point
     * @return the scene's ambient light
     */
    private Color calcColor(Point point) {
        return scene.background;
    }
}
