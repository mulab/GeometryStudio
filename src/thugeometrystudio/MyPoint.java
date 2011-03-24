
package thugeometrystudio;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

public class MyPoint extends Shape
{
    public Point mypoint=new Point();
    public int tag = 0;
    public boolean isDrawTag = true;
    public MyPoint rcenter;
    public Line axis;
    public boolean isSelected = false;
    public boolean isOver = false;
    public Vector<MyPoint> trans=new Vector<MyPoint>();
    public Vector<MyPoint> rotate=new Vector<MyPoint>();
    public Vector<MyPoint> reflect=new Vector<MyPoint>();

    public MyPoint(int x,int y)
    {
        mypoint.x=x;
        mypoint.y=y;
    }

    public void drawPoint(Graphics g)
    {
        if(!isSelected&&!isOver)
        {
            g.setColor(Color.RED);
            g.fillOval(mypoint.x-5, mypoint.y-5, 10, 10);
        }
        else if(isSelected)
        {
            g.setColor(Color.RED);
            g.fillOval(mypoint.x-5, mypoint.y-5, 10, 10);
            g.setColor(Color.BLUE);
            g.drawOval(mypoint.x-7, mypoint.y-7, 14, 14);
        }
        else if(!isSelected&&isOver)
        {
            g.setColor(Color.RED);
            g.fillOval(mypoint.x-7, mypoint.y-7, 14, 14);
        }
        if(isDrawTag)
        {
            g.drawString(""+tag, mypoint.x+10, mypoint.y-10);
        }
    }

    public boolean mouseOver(int x, int y)
    {
        int tx = x - mypoint.x;
        int ty = y - mypoint.y;
        int res = tx*tx+ty*ty;
        if(res<=25)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void setRelated(MyPoint point)
    {

    }

    @Override
    public void adjustRelated()
    {

    }

    @Override
    public void changeRelated(RelatedPoint point, int x, int y)
    {

    }

    @Override
    public int getTag()
    {
        return this.tag;
    }
}
