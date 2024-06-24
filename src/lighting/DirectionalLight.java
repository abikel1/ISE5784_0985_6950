package lighting;

import primitives.Vector;
import primitives.Color;
import primitives.Point;

public class DirectionalLight extends Light implements LightSource{
    /** The direction of the light rays in a DirectionalLight object. */
    private final Vector direction;
    /**
     * DirectionalLight constructor initialize direction direction of the light
     * source and the intensity
     *
     * @param intensity of the color
     */
    public DirectionalLight(Color intensity, Vector dir) {
        super(intensity);
        this.direction = dir.normalize();
    }
    /**
     * Returns the intensity of the light at a given point.
     *
     * Since a directional light source has parallel rays, the intensity is the same
     * for all points in the scene.
     *
     * @param point The point at which to calculate the intensity (not used).
     * @return The intensity of the light.
     */
    @Override
    public Color getIntensity(Point point) {
        return getIntensity();
    }

    /**
     * Returns the direction of the light rays at a given point.
     *
     * Since a directional light source has a uniform direction, the direction is
     * constant for all points in the scene.
     *
     * @param point The point at which to calculate the direction (not used).
     * @return The direction of the light rays.
     */
    @Override
    public Vector getL(Point point) {
        return direction;
    }
    /**
     * Returns the distance from the light source to a given point.
     *
     * @param point The point for which to calculate the distance.
     * @return The distance from the light source to the point (always positive infinity).
     */
    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}
