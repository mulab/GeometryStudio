
package thugeometrystudio;

import java.util.Vector;

public class Polygon
{
    public Vector<MyPoint> free=new Vector<MyPoint>();
    public Vector<MyPoint> related=new Vector<MyPoint>();
    public MyPoint rcenter;
    public Line axis;
    public Vector<Polygon> trans=new Vector<Polygon>();
    public Vector<Polygon> rotate=new Vector<Polygon>();
    public Vector<Polygon> reflect=new Vector<Polygon>();
}
