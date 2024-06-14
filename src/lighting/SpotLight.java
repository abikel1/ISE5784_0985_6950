package lighting;

import primitives.Vector;
import primitives.Point;
import primitives.Color;

public class SpotLight extends PointLight{
    /**
     * Represents the direction of the spot light source.
     */
    private Vector direction;
    /**
     * Represents the narrowness of the light beam emitted by the spot light source.
     * A value of 1 represents a wide beam, while values less than 1 create a
     * narrower beam.
     */
    private double narrowBeam = 1;// from 4253

    /**
     * Constructor of a spot light object
     *
     * @param intensity, the color of the light source
     * @param position   of the light source
     * @param dir  of the light
     */
    public SpotLight(Color intensity, Point position, Vector dir) {
        super(intensity, position);
        this.direction = dir.normalize();
    }
    /**
     * Sets the constant attenuation factor.
     * @param kC the constant attenuation factor
     * @return the current SpotLight instance (for chaining)
     */
    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     * @param kL the linear attenuation factor
     * @return the current SpotLight instance (for chaining)
     */
    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     * @param kQ the quadratic attenuation factor
     * @return the current SpotLight instance (for chaining)
     */
    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    /**
     * Calculates and returns the intensity of the light at the specified point. The
     * intensity is attenuated based on the distance and the angle between the light
     * direction and the surface normal.
     *
     * @param point the point at which to calculate the intensity
     * @return the attenuated intensity at the point
     */

    /**
     * Sets the narrowness of the light beam. A value of 1 represents a wide beam,
     * while values less than 1 create a narrower beam.
     *
     * @param narrowBeam the narrowness of the light beam
     * @return the SpotLight object for method chaining
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    @Override
    public Color getIntensity(Point point) {
        // check if it is flashlight
        return narrowBeam != 1
                ? super.getIntensity(point).scale(Math.pow(Math.max(0, direction.dotProduct(getL(point))), narrowBeam))
                : super.getIntensity(point).scale(Math.max(0, direction.dotProduct(getL(point))));
    }
}
