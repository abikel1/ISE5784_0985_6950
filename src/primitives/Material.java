package primitives;

public class Material {
    /** Diffuse coefficient for the material. */
    public Double3 kD = Double3.ZERO;
    /** Specular coefficient for the material. */
    public Double3 kS = Double3.ZERO;
    /** Transmission coefficient for the material. */
    public Double3 kT = Double3.ZERO;
    /** Reflection coefficient for the material. */
    public Double3 kR = Double3.ZERO;
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
     * Sets the transmission coefficient of the material using a {@code Double3}
     * object.
     *
     * @param kT the transmission coefficient as a {@code Double3} object
     * @return this {@code Material} object for method chaining
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transmission coefficient of the material using a single double
     * value.
     *
     * @param kT the transmission coefficient as a double value
     * @return this {@code Material} object for method chaining
     */
    public Material setKt(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Sets the reflection coefficient of the material using a {@code Double3}
     * object.
     *
     * @param kR the reflection coefficient as a {@code Double3} object
     * @return this {@code Material} object for method chaining
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection coefficient of the material using a single double value.
     *
     * @param kR the reflection coefficient as a double value
     * @return this {@code Material} object for method chaining
     */
    public Material setKr(double kR) {
        this.kR = new Double3(kR);
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
