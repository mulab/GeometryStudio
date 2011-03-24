package thugeometrystudio;


public abstract class Shape
{
    public abstract void setRelated(MyPoint point);
    public abstract void adjustRelated();
    public abstract void changeRelated(RelatedPoint point, int x, int y);
    public abstract int getTag();
}
