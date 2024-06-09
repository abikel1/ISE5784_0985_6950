package primitives;

public class Material {
    /** Diffuse coefficient for the material. */
    public Double3 kD = Double3.ZERO;
    /** Specular coefficient for the material. */
    public Double3 kS = Double3.ZERO;
    /** Shininess exponent for the material. */
    public int nShininess = 0;

    /**
     * Sets the diffuse reflection coefficient of the material using a
     * {@code Double3} object.
     *
     * @param kD the diffuse reflection coefficient as a {@code Double3} object
     * @return this {@code Material} object for method chaining
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient of the material using a single double
     * value.
     *
     * @param kD the diffuse reflection coefficient as a double value
     * @return this {@code Material} object for method chaining
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular reflection coefficient of the material using a
     * {@code Double3} object.
     *
     * @param kS the specular reflection coefficient as a {@code Double3} object
     * @return this {@code Material} object for method chaining
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;

    }

    /**
     * Sets the specular reflection coefficient of the material using a single
     * double value.
     *
     * @param kS the specular reflection coefficient as a double value
     * @return this {@code Material} object for method chaining
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the shininess of the material.
     *
     * @param nShininess the shininess value to set
     * @return this {@code Material} object for method chaining
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;

    }
}
