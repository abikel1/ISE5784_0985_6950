package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight extends Light {
    // a constant field the default intensity is black (no light intensity):
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

    /**
     * Constructor (double3) that recives 2 parameters and calculate light intensity
     *
     * @param iA - the original color of the light (the intensity of the light
     *           according to RGB)
     * @param kA - the attenuation factor of the original light
     */
    public AmbientLight(Color iA, Double3 kA) {
        super(iA.scale(kA));// iP = kA*iA
    }
    /**
     * Constructor (double) that recives 2 parameters and calculate light intensity
     *
     * @param iA - the original color of the light (the intensity of the light
     *           according to RGB)
     * @param kA - the attenuation factor of the original light
     */
    public AmbientLight(Color iA, double kA) {
        super(iA.scale(kA));// iP = kA*iA
    }

}
