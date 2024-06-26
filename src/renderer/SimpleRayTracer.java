package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;
import geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;

public class SimpleRayTracer extends RayTracerBase{

    /**
     * Constructs with one pa
     *
     * @param scene the scene to be traced
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a ray through the scene and calculates the color at the point where the ray intersects with an object.
     * @param ray the ray to trace through the scene
     * @return the color at the point where the ray intersects with an object, or the background color if no intersection is found
     */
    @Override
    public Color traceRay(Ray ray)
    {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if(intersections == null)// check if there is no point in the intersection
            return scene.background;
        GeoPoint closetPoint = ray.findClosestGeoPoint(intersections);// find the closet point to the head of the ray
        return calcColor(closetPoint,ray);// call the function that return the color of the point
    }
    /**
     * Calculates the local effects of color at a point in the scene.
     *
     * @param gp  the geometry point to calculate color for
     * @param ray the ray that intersects the point
     * @return the color at the given point, accounting for local effects
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Color color = Color.BLACK;
        Vector vector = ray.direction;
        Vector normal = gp.geometry.getNormal(gp.point);
        double nv = alignZero(normal.dotProduct(vector));
        if (nv == 0)
            return color;
        Material material = gp.geometry.getMaterial();
        for (LightSource lightSource : scene.lights) {
            Vector lightVector = lightSource.getL(gp.point);
            double nl = alignZero(normal.dotProduct(lightVector));
            if (nl * nv > 0) {
                Color lightIntensity = lightSource.getIntensity(gp.point);
                color = color.add(lightIntensity.scale(calcDiffusive(material, nl)),
                        lightIntensity.scale(calcSpecular(material, normal, lightVector, nl, vector)));
            }
        }
        return color;
    }
    /**
     * Calculates the specular color at a point on a geometry.
     *
     * @param material    the material of the geometry
     * @param normal      the normal of the geometry
     * @param lightVector the light vector
     * @param nl          the dot product of the normal and light vector
     * @param vector      the direction of the ray
     * @return the specular color at the given point
     */
    private Double3 calcSpecular(Material material, Vector normal, Vector lightVector, double nl, Vector vector) {
        Vector reflectedVector = lightVector.subtract(normal.scale(2 * nl));
        double max = Math.max(0, vector.scale(-1).dotProduct(reflectedVector));
        return material.kS.scale(Math.pow(max, material.nShininess));

    }

    /**
     * Calculates the diffusive color at a point on a geometry.
     *
     * @param material the material of the geometry
     * @param nl       the dot product of the normal and light vector
     * @return the diffusive color at the given point
     */
    private Double3 calcDiffusive(Material material, double nl) {
        return material.kD.scale(Math.abs(nl));
    }

    /**
     * Calculates the color of a point in the scene based on the ambient light present.
     * @param point the point in the scene for which to calculate the color
     * @return the color of the point based on the ambient light present
     */
    private Color calcColor(GeoPoint point, Ray ray)
    {
        return point.geometry.getEmission().add(scene.ambientLight.getIntensity(), calcLocalEffects(point, ray));
    }
}
