package lighting;

import primitives.Point;
import primitives.Color;
import primitives.Vector;

public class PointLight extends Light implements LightSource{
    /** Represents the position of the point light source. */
    private Point position;
    /** Represents the constant attenuation factor of the point light source. */
    private double kC = 1;
    /** Represents the linear attenuation factor of the point light source. */
    private double kL = 0;
    /** Represents the quadratic attenuation factor of the point light source. */
    private double kQ = 0;

    /**
     * Constructor of PointLight initialize the intensity color and the position of
     * the light source in the scene.
     *
     * @param intensity of the light source
     * @param position  of the light source in the scene
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the constant attenuation factor of the light.
     *
     * @param kC the constant attenuation factor to set
     * @return the PointLight object for method chaining
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor of the light.
     *
     * @param kL the linear attenuation factor to set
     * @return the PointLight object for method chaining
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;

    }

    /**
     * Sets the quadratic attenuation factor of the light.
     *
     * @param kQ the quadratic attenuation factor to set
     * @return the PointLight object for method chaining
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }
    /**
     * Calculates and returns the intensity of the light at the specified point. The
     * intensity is attenuated based on the distance from the light source.
     *
     * @param p the point at which to calculate the intensity
     * @return the attenuated intensity at the point
     */
    @Override
    public Color getIntensity(Point p) {
        double distanceSquared=p.distanceSquared(position);//d^2
        double distance=p.distance(position);//d
        return getIntensity().scale(1 / (kC + kL * distance + kQ * distanceSquared));
    }

    @Override
    public Vector getL(Point p) {
        if (p.equals(position)) {//check if p==position ,because vector zero is bad
            return null;
        }
        return p.subtract(position).normalize();//return the normalized vector of (p-position)
    }
}
