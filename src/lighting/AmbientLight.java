package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {
    // a constant field the default intensity is black (no light intensity):
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

    private final Color intensity;
    /**
     * Constructor (double3) that recives 2 parameters and calculate light intensity
     *
     * @param iA - the original color of the light (the intensity of the light
     *           according to RGB)
     * @param kA - the attenuation factor of the original light
     */
    public AmbientLight(Color iA, Double3 kA) {
        this.intensity=iA.scale(kA);// iP = kA*iA
    }
    /**
     * Constructor (double) that recives 2 parameters and calculate light intensity
     *
     * @param iA - the original color of the light (the intensity of the light
     *           according to RGB)
     * @param kA - the attenuation factor of the original light
     */
    public AmbientLight(Color iA, double kA) {
        this.intensity=iA.scale(kA);// iP = kA*iA
    }
    /**
     * return light intensity
     *
     * @return Color
     */
    public Color getIntensity() {
        return intensity;
    }

}
