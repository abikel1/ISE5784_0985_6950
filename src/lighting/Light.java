package lighting;

import primitives.Color;

public abstract class Light
{
    protected final Color intensity;

    /**
     * @param intensity
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * @return the intensity
     */
    public Color getIntensity() {
        return intensity;
    }

}
