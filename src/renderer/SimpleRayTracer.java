package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;
import geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;

public class SimpleRayTracer extends RayTracerBase{
    private static final double DELTA = 0.1;
    /**
     * Constructs with one param.
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
                if (unshaded(gp, lightVector, normal, nv,lightSource)) {
                    Color lightIntensity = lightSource.getIntensity(gp.point);
                    color = color.add(lightIntensity.scale(calcDiffusive(material, nl)), lightIntensity.scale(calcSpecular(material, normal, lightVector, nl, vector)));
                }
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
    /**
     * Checks if a given point is unshaded by finding intersections between the point and the light source.
     *
     * @param gp   The geometric point to check for shading.
     * @param l    The direction from the point towards the light source.
     * @param n    The normal vector at the point.
     * @param light The light source.
     * @param nv   The dot product between the normal vector and the light direction.
     * @return {@code true} if the point is unshaded, {@code false} otherwise.
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, double nv, LightSource light) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector epsVector = n.scale(nv < 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);
        Ray lightRay = new Ray(point, lightDirection);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
        if (intersections == null)
            return true;
        double lightDistance = light.getDistance(gp.point);
        for (GeoPoint gp1 : intersections) {
            if (Util.alignZero(gp1.point.distance(gp.point) - lightDistance) <= 0)
                //&& gp1.geometry.getMaterial().kT == 0)
                return false;
        }
      return true;
    }
}
