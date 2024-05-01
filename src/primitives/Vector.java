package primitives;

public class Vector extends Point
{
    public Vector(Double3 xyz)
    {
        super(xyz);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this==obj) return true;
        return super.equals(obj);
    }
}
