package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The LightSource interface represents a light source in a scene. Implementing
 * classes must define methods for retrieving the intensity and direction of the
 * light source.
 *
 * @author Ayala and Avital
 *
 */
public interface LightSource {
    /**
     * Returns the intensity of the light source at the given point.
     *
     * @param p the point in the scene
     * @return the intensity of the light source at the given point
     */
    public Color getIntensity(Point p);

    /**
     * Returns the direction vector from the light source to the given point.
     *
     * @param p the point in the scene
     * @return the direction vector from the light source to the given point
     */
    public Vector getL(Point p);
    /**
     * returns the distance of the light source
     *
     * @param point point to check the distance
     * @return distance of the light source
     */
    double getDistance(Point point);
}
