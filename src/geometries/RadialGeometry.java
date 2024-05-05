package geometries;
/**
 * The RadialGeometry class is an abstract class representing geometric shapes with a radial property,
 * such as spheres and cylinders.
 */
public abstract class RadialGeometry implements Geometry
{
    // The radius of the radial geometry
    final protected double radius;
    /**
     * Constructs a new RadialGeometry object with the specified radius.
     *
     * @param radius The radius of the radial geometry.
     */
    public RadialGeometry(double radius)
    {
        this.radius = radius;
    }
}
