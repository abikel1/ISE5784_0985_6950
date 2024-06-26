package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;
import geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class SimpleRayTracer extends RayTracerBase{
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INIT_CALC_COLOR_K = Double3.ONE;
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
        GeoPoint closestPoint = findClosestIntersection(ray);
        return closestPoint == null ? scene.background : calcColor(closestPoint, ray);
    }

    /**
     * Calculates the local effects (diffuse and specular) at the intersection point.
     *
     * @param gp  The intersection point.
     * @param ray The ray.
     * @return The color with local effects.
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Color color = gp.geometry.getEmission();
        Vector v = ray.direction;
        Vector n = gp.geometry.getNormal(gp.point);
        double nv = Util.alignZero(n.dotProduct(v));
        if (nv == 0)
            return color;
        Material material = gp.geometry.getMaterial();
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = Util.alignZero(n.dotProduct(l));
            if (nl * nv > 0 && unshaded(gp, l, n, nl,lightSource)) {
                Color iL = lightSource.getIntensity(gp.point);
                color = color.add(iL.scale(calcDiffusive(material, nl)), iL.scale(calcSpecular(material, n, l, nl, v)));
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
        // חישוב וקטור ההשתקפות על פי הנוסחה: R = L - 2*(N.L)*N
        Vector reflectedVector = lightVector.subtract(normal.scale(2 * nl));
        // חישוב המכפלה הנקודתית בין וקטור הקרן ההפוכה (המצלמה לנקודת הפגיעה) לוקטור ההשתקפות
        double max = Math.max(0, vector.scale(-1).dotProduct(reflectedVector));
        // חישוב עוצמת ההשתקפות (צבע ספקולרי) על פי הנוסחה: ks * max^nShininess
        // הקטן מבין max או 0 (שלא יהיה מספר שלילי) בחזקת nShininess
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
        // חישוב הרכיב הדיפוזי של האור
        // הכפלת מקדם הדיפוזיה של החומר (kD) במכפלה הנקודתית המוחלטת בין הנורמל לוקטור האור (nl)
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
        Color color = calcLocalEffects(geoPoint, ray);
        return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }
    /**
     * Helping method for color calculation (function calcGlobalEffects) of a point
     * on a geometry as it sees from the camera point of view. Calculating the
     * global effects.
     *
     * @param ray   - ray from the camera that intersect the geometry
     * @param level - level of recursion
     * @param k     - the material reflection\refraction coefficient value (between
     *              0-1)
     * @param kx    - product of the global and local attenuation coefficient
     * @return the color of the point with consideration of global effect
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        if (gp == null)
            return scene.background.scale(kx);
        return isZero(gp.geometry.getNormal(gp.point).dotProduct(ray.direction)) ? Color.BLACK
                : calcColor(gp, ray, level - 1, kkx).scale(kx);
    }
    /**
     * Helping method for color calculation of a point on a geometry as it sees from
     * the camera point of view. The function is modeling transparent objects (with
     * various opacity levels) and reflecting surfaces such as mirrors.
     *
     * @param geoPoint - the observed point on the geometry
     * @param ray      - from the camera that intersect the geometry
     * @param level    - of recursion for the global effects calculation
     * @param k        - initial attenuation coefficient value (between 0-1)
     * @return the color of the point with consideration of global effects
     */
    private Color calcGlobalEffects(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Material material = geoPoint.geometry.getMaterial();
        Vector v = ray.direction;
        Vector normal = geoPoint.geometry.getNormal(geoPoint.point);
        Ray reflectedRay = constructReflectedRay(geoPoint, normal, v);
        Ray refractedRay = constructRefractedRay(geoPoint, normal, v);
        return calcGlobalEffect(reflectedRay, level, k, material.kR)
                .add(calcGlobalEffect(refractedRay, level, k, material.kT));
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


}
