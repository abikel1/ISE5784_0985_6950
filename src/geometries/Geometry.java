package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
/**
 * The Geometry interface represents geometric shapes in a 3D space.
 * It provides a method to calculate the normal vector at a given point on the geometry.
 */
public abstract class Geometry extends Intersectable
{
    protected Color emission= Color.BLACK;
    private Material material = new Material();
    /**
     * Calculates the normal vector at the given point on the geometry.
     *
     * @param p The point on the geometry to calculate the normal vector for.
     * @return The normal vector at the given point.
     */
    public abstract Vector getNormal(Point p);

    /**
     *
     *
     * @return the emission color of the geometry
     */
    public Color getEmission()
    {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the emission color to set
     * @return the geometry itself
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
    /**
     * Returns the material of the geometry.
     *
     * @return the material of the geometry
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the geometry.
     *
     * @param material the material to set
     * @return the geometry itself
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
