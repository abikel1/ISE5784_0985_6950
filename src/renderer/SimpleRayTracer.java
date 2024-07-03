package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;
import geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;

public class SimpleRayTracer extends RayTracerBase{
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INIT_CALC_COLOR_K = Double3.ONE;
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
        GeoPoint closestPoint = findClosestIntersection(ray);
        return closestPoint == null ? scene.background : calcColor(closestPoint, ray);
    }
    /**
     * Calculates the local effects of color at a point in the scene.
     *
     * @param gp  the geometry point to calculate color for
     * @param ray the ray that intersects the point
     * @return the color at the given point, accounting for local effects
     */
    /**
     * Calculates the local effects (diffuse and specular reflection) at a given
     * geometric point.
     *
     * @param geoPoint The geometric point in the scene.
     * @param ray      The ray that intersects the geometric point.
     * @param k        The coefficient values for local effects.
     * @return The calculated color due to local effects at the given point.
     */
    private Color calcLocalEffects(GeoPoint geoPoint, Ray ray, Double3 k) {
        Color color = geoPoint.geometry.getEmission();
        Vector vector = ray.direction;
        Vector normal = geoPoint.geometry.getNormal(geoPoint.point);
        double nv = alignZero(normal.dotProduct(vector));
        if (nv == 0)
            return color;
        Material material = geoPoint.geometry.getMaterial();
        for (LightSource lightSource : scene.lights) {
            Vector lightVector = lightSource.getL(geoPoint.point);
            double nl = alignZero(normal.dotProduct(lightVector));
            if (nl * nv > 0) {
                Double3 ktr = transparency(geoPoint, lightVector, normal, nv, lightSource);
                if (!(ktr.product(k).lowerThan(MIN_CALC_COLOR_K))) {
                    Color lightIntensity = lightSource.getIntensity(geoPoint.point).scale(ktr);
                    color = color.add(lightIntensity.scale(calcDiffusive(material, nl)),
                            lightIntensity.scale(calcSpecular(material, normal, lightVector, nl, vector)));
                }
            }
        }
        return color;
    }
//    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
//        Color color = Color.BLACK;
//        Vector vector = ray.direction;
//        Vector normal = gp.geometry.getNormal(gp.point);
//        double nv = alignZero(normal.dotProduct(vector));
//        if (nv == 0)
//            return color;
//        Material material = gp.geometry.getMaterial();
//        for (LightSource lightSource : scene.lights) {
//            Vector lightVector = lightSource.getL(gp.point);
//            double nl = alignZero(normal.dotProduct(lightVector));
//            if (nl * nv > 0) {
//                if (unshaded(gp, lightVector, normal, nv,lightSource)) {
//                    Color lightIntensity = lightSource.getIntensity(gp.point);
//                    color = color.add(lightIntensity.scale(calcDiffusive(material, nl)), lightIntensity.scale(calcSpecular(material, normal, lightVector, nl, vector)));
//                }
//            }
//        }
//        return color;
//    }
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
     * Calculates the color of a given point in the scene.
     *
     * @param geoPoint the point to calculate the color for
     * @param ray      the ray that intersects the point
     * @return the color at the given point
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return calcColor(geoPoint, ray, MAX_CALC_COLOR_LEVEL, INIT_CALC_COLOR_K).add(scene.ambientLight.getIntensity());
    }
    /**
     * Calculates the color at a given point in the scene, taking into account local
     * and global effects.
     *
     * @param geoPoint The geometric point in the scene.
     * @param ray      The ray that intersects the geometric point.
     * @param level    The recursion level for global effects.
     * @param k        The coefficient values for global effects.
     * @return The calculated color at the given point.
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(geoPoint, ray, k);
        return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }
    /**
     * Checks if a given point is unshaded by finding intersections between the point and the light source.
     *
     * @param gp   The geometric point to check for shading.
     * @param l    The direction from the point towards the light source.
     * @param n    The normal vector at the point.
     * @param nv   The dot product between the normal vector and the light direction.
     * @param light The light source.
     * @return {@code true} if the point is unshaded, {@code false} otherwise.
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, double nv, LightSource light) {
        // Calculate the light direction vector (from the point towards the light source)
        Vector lightDirection = l.scale(-1); // from point to light source

        // Small vector to offset the point to avoid precision issues
        Vector epsVector = n.scale(nv < 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);

        // Create a ray from the point towards the light source
        Ray lightRay = new Ray(point, lightDirection);

        // Find intersections of the ray with other objects in the scene
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
        if (intersections == null)
            return true; // No intersections, so the point is unshaded

        // Calculate the distance from the point to the light source
        double lightDistance = light.getDistance(gp.point);

        // Check if any intersection points are between the point and the light source
        for (GeoPoint gp1 : intersections) {
            if (Util.alignZero(gp1.point.distance(gp.point) - lightDistance) <= 0)
                return false; // Intersection is between the point and the light source, so the point is shaded
        }

        return true; // No intersections block the light, so the point is unshaded
    }

    /**
     * Calculates the global effects (reflection and refraction) at a given
     * geometric point.
     *
     * @param geoPoint The geometric point in the scene.
     * @param level    The recursion level for global effects.
     * @param color    The current color at the point.
     * @param kx       The coefficient values for the specific effect (reflection or
     *                 refraction).
     * @param k        The overall coefficient values for global effects.
     * @param ray      The ray used for the specific effect (reflection or
     *                 refraction).
     * @return The updated color after considering the global effects.
     */
    private Color calcGlobalEffects(GeoPoint geoPoint, int level, Color color, Double3 kx, Double3 k, Ray ray) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;
        GeoPoint reflectedPoint = findClosestIntersection(ray);
        if (reflectedPoint != null) {
            color = color.add(calcColor(reflectedPoint, ray, level - 1, kkx).scale(kx));
            return color;
        }
        return  Color.BLACK;
    }
    /**
     * Calculates the global effects (reflection and refraction) at a given
     * geometric point.
     *
     * @param gp    The geometric point in the scene.
     * @param ray   The ray that intersects the geometric point.
     * @param level The recursion level for global effects.
     * @param k     The coefficient values for global effects.
     * @return The calculated color due to global effects at the given point.
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;
        Vector v = ray.direction;
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();
        return calcGlobalEffects(gp, level, color, material.kR, k, constructReflectedRay(gp, v, n))
                .add(calcGlobalEffects(gp, level, color, material.kT, k, constructRefractedRay(gp, v, n)));
    }
    /**
     * Finds the closest point of intersection between the given ray and objects in
     * the scene.
     *
     * @param ray the ray to be tested for intersections.
     * @return the closest GeoPoint of intersection, or null if there are no
     *         intersections.
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        return intersections == null ? null : ray.findClosestGeoPoint(intersections);
    }
    /**
     * Constructs a refracted ray at a given geometric point.
     *
     * @param gp The geometric point in the scene.
     * @param v  The incident ray direction.
     * @param n  The normal vector at the point.
     * @return The refracted ray.
     */
    private Ray constructRefractedRay(GeoPoint gp, Vector v, Vector n) {
        return new Ray(gp.point, v, n);
    }
    /**
     * Constructs a reflected ray at a given geometric point.
     *
     * @param gp The geometric point in the scene.
     * @param v  The incident ray direction.
     * @param n  The normal vector at the point.
     * @return The reflected ray.
     */
    private Ray constructReflectedRay(GeoPoint gp, Vector v, Vector n) {
        Vector reflectedVector = v.subtract(n.scale(2 * v.dotProduct(n)));
        return new Ray(gp.point, reflectedVector, n);
    }
    /**
     * Calculates the transparency factor for a given geometric point and light source.
     *
     * @param gp    The geometric point in the scene.
     * @param l     The direction from the point to the light source.
     * @param n     The normal vector at the point.
     * @param nv    The dot product of the normal and the incident ray direction.
     * @param light The light source.
     * @return The transparency factor (ktr) as a Double3 vector.
     */
    private Double3 transparency(GeoPoint gp, Vector l, Vector n, double nv, LightSource light) {
        // Calculate the light direction vector (from the point towards the light source)
        Vector lightDirection = l.scale(-1); // from point to light source

        // Create a ray from the point towards the light source
        Ray lightRay = new Ray(gp.point, lightDirection, n);

        // Find intersections of the ray with other objects in the scene
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
        if (intersections == null)
            return Double3.ONE; // No intersections, so the transparency factor is 1 (fully transparent)

        Double3 ktr = Double3.ONE; // Initial transparency factor
        double lightDistance = light.getDistance(gp.point);

        // Calculate the transparency factor by considering each intersected object
        for (GeoPoint gp1 : intersections) {
            if (alignZero(gp1.point.distance(gp.point) - lightDistance) <= 0) {
                ktr = ktr.product(gp1.geometry.getMaterial().kT); // Reduce transparency factor based on the object's transparency
                if (ktr.lowerThan(MIN_CALC_COLOR_K))
                    return Double3.ZERO; // Transparency factor is too low, return zero (fully opaque)
            }
        }

        return ktr; // Return the final transparency factor
    }

}
