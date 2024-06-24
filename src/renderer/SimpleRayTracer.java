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
    private Color calcLocalEffects(GeoPoint gp, Ray ray)
    {
        // אתחול צבע שחור כצבע הבסיס
        Color color = Color.BLACK;
        // כיוון הקרן
        Vector vector = ray.direction;
        // וקטור הנורמל בנקודת הפגיעה
        Vector normal = gp.geometry.getNormal(gp.point);
        // חישוב המכפלה הנקודתית בין הנורמל לכיוון הקרן
        double nv = alignZero(normal.dotProduct(vector));
        // אם המכפלה הנקודתית היא אפס, אין החזר אור ולכן מחזירים את הצבע השחור
        if (nv == 0)
            return color;
        // קבלת החומר של הגיאומטריה בנקודת הפגיעה
        Material material = gp.geometry.getMaterial();
        // עבור כל מקור אור בסצנה
        for (LightSource lightSource : scene.lights)
        {
            // וקטור האור מנקודת הפגיעה למקור האור
            Vector lightVector = lightSource.getL(gp.point);
            // חישוב המכפלה הנקודתית בין הנורמל לוקטור האור
            double nl = alignZero(normal.dotProduct(lightVector));
            // אם המכפלות הנקודתיות בעלות אותו סימן, האור מגיע מכיוון מתאים
            if (nl * nv > 0&& unshaded(gp,lightVector,normal,nl,lightSource)) {
                // קבלת עוצמת האור בנקודת הפגיעה
                Color lightIntensity = lightSource.getIntensity(gp.point);
                // הוספת הצבע הדיפוזי והספקולרי לצבע הכולל
                color = color.add(
                        // חישוב הצבע הדיפוזי (מפוזר)
                        lightIntensity.scale(calcDiffusive(material, nl)),
                        // חישוב הצבע הספקולרי (משתקף)
                        lightIntensity.scale(calcSpecular(material, normal, lightVector, nl, vector))
                );
            }
        }

        // החזרת הצבע המחושב
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
     * Calculates the color of a point in the scene based on the ambient light present.
     * @param geoPoint the point in the scene for which to calculate the color
     * @return the color of the point based on the ambient light present
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene.ambientLight.getIntensity().add(geoPoint.geometry.getEmission()).add(calcLocalEffects(geoPoint,ray));
    }
    /**
     * function will check if point is unshaded
     *
     * @param gp geometry point to check
     * @param l  light vector
     * @param n  normal vector
     * @return true if unshaded
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, double nl, LightSource light) {
        Vector lightDir = l.scale(-1);
        Ray lightRay = new Ray(gp.point.add(n.scale(nl < 0 ? DELTA : -DELTA)), lightDir);
        return scene.geometries.findGeoIntersections(lightRay, light.getDistance(gp.point)) == null;
    }


}
