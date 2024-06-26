package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class SimpleRayTracer extends RayTracerBase {

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final double DELTA = 0.1;


    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray);
        if (closestPoint == null) {
            return scene.background;
        }
        return calcColor(closestPoint, ray);
    }

    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) return null;
        return ray.findClosestGeoPoint(intersections);
    }

    private Color calcColor(GeoPoint gp, Ray ray) {
        return calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, new Double3(1.0)).add(scene.ambientLight.getIntensity());
    }

    private Color calcColor(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = gp.geometry.getEmission().add(calcLocalEffects(gp, ray, k));
        return 1 == level ? color : color.add(calcGlobalEffects(gp, ray, level, k));
    }

    private Color calcLocalEffects(GeoPoint intersection, Ray ray, Double3 k) {
        Vector v = ray.direction;
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (isZero(nv)) {
            return Color.BLACK;
        }

        int nShininess = intersection.geometry.getMaterial().nShininess;
        Double3 kd = intersection.geometry.getMaterial().kD;
        Double3 ks = intersection.geometry.getMaterial().kS;

        Color color = Color.BLACK;
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(intersection.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sing(nv)
                Double3 ktr = transparency(intersection, lightSource, l, n);
                if (ktr.lowerThan(MIN_CALC_COLOR_K) == false) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                    color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }

    private Double3 transparency(GeoPoint geoPoint, LightSource light, Vector l, Vector n) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geoPoint.point, lightDirection);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
        if (intersections == null) return new Double3(1.0);

        double lightDistance = light.getDistance(geoPoint.point);
        Double3 ktr = new Double3(1.0);
        for (GeoPoint gp : intersections) {
            if (alignZero(gp.point.distance(geoPoint.point) - lightDistance) <= 0) {
                ktr = ktr.product(gp.geometry.getMaterial().kT);
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) return new Double3(0.0);
            }
        }
        return ktr;
    }

    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();
        return calcGlobalEffect(constructReflectedRay(gp.point, ray, n), level, k, material.kR)
                .add(calcGlobalEffect(constructRefractedRay(gp.point, ray, n), level, k, material.kT));
    }

    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ? scene.background : calcColor(gp, ray, level - 1, k.product(kx))).scale(kx);
    }

    private Ray constructReflectedRay(Point point, Ray ray, Vector n) {
        Vector v = ray.direction;
        double vn = v.dotProduct(n);
        if (isZero(vn)) {
            return null;
        }
        Vector r = v.subtract(n.scale(2 * vn)).normalize();
        return new Ray(point, r);
    }

    private Ray constructRefractedRay(Point point, Ray ray, Vector n) {
        return new Ray(point, ray.direction);
    }

    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color lightIntensity) {
        double ln = l.dotProduct(n);
        if (ln < 0) ln = -ln;
        return lightIntensity.scale(kd.scale(ln));
    }

    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int shininess, Color lightIntensity) {
        Vector r = l.subtract(n.scale(2 * l.dotProduct(n)));
        double vr = v.dotProduct(r);
        if (vr <= 0) return Color.BLACK;
        return lightIntensity.scale(ks.scale(Math.pow(vr, shininess)));
    }
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, double nl, LightSource light) {
        Vector lightDir = l.scale(-1);
        Ray lightRay = new Ray(gp.point.add(n.scale(nl < 0 ? DELTA : -DELTA)), lightDir);

        var intersections = scene.geometries.findGeoIntersections(lightRay, light.getDistance(gp.point));
        if (intersections == null)
            return true;
        for (GeoPoint p : intersections)
            if (!p.geometry.getMaterial().kT.equals(Double3.ZERO))
                return false;
        return true;}
}
