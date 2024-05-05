package primitives;
/**
 * This class represents a ray in three-dimensional space.
 */
public class Ray
{
    // The starting point of the ray
    final private Point head;
    // The direction of the ray
    final private Vector direction;

    /**
     * Constructs a new Ray object with the given starting point and direction.
     * @param head The starting point of the ray.
     * @param direction The direction of the ray.
     */
    public Ray(Point head, Vector direction)
    {
        this.head = head;
        this.direction = direction.normalize();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "Ray:" + head.toString()+" "+direction.toString();
    }
}
