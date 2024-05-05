package geometries;

import primitives.Ray;
/**
 * The Cylinder class represents a cylinder in 3D space, extending from a given axis with a specified radius and height.
 * It inherits properties and behaviors from the Tube class.
 */
public class Cylinder extends Tube
{
    // The height of the cylinder
    final private double height;
    /**
     * Constructs a new Cylinder object with the specified axis, radius, and height.
     *
     * @param axis   The axis of the cylinder.
     * @param radius The radius of the cylinder.
     * @param height The height of the cylinder.
     */
    public Cylinder (Ray axis, double radius,double height)
    {
        super(axis,radius);
        this.height = height;
    }
}
