
package thugeometrystudio;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;


public class Line extends Shape
{
    public MyPoint start;
    public MyPoint to;
    public MyPoint rcenter=start;
    public Line axis;
    public boolean isSelected = false;
    public boolean isOver = false;
    public Vector<RelatedPoint> related=new Vector<RelatedPoint>();
    public Vector<Line> trans=new Vector<Line>();
    public Vector<Line> rotate=new Vector<Line>();
    public Vector<Line> reflect=new Vector<Line>();

    public Line(MyPoint start,MyPoint to)
    {
        this.start = start;
        this.to = to;
    }

    public void init()
    {
        start = null;
        to = null;
    }

    public void drawRelatedPoint(Graphics g)
    {
        adjustRelated();
        for(int i=0;i<related.size();i++)
        {
            related.elementAt(i).point.drawPoint(g);
        }
    }

    public void drawLine(Graphics g)
    {
        if(!isSelected&&!isOver)
        {
            g.setColor(Color.BLUE);
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(2.5f));
            g.drawLine(start.mypoint.x, start.mypoint.y, to.mypoint.x, to.mypoint.y);
        }
        else if(isSelected)
        {
            g.setColor(Color.BLUE);
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(2.5f));
            g.drawLine(start.mypoint.x, start.mypoint.y, to.mypoint.x, to.mypoint.y);
            g2d.setStroke(new BasicStroke(0.5f));
            g.drawLine(start.mypoint.x, start.mypoint.y-5, to.mypoint.x, to.mypoint.y-5);
            g.drawLine(start.mypoint.x, start.mypoint.y+5, to.mypoint.x, to.mypoint.y+5);
        }
        else if(!isSelected&&isOver)
        {
            g.setColor(Color.RED);
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(2.5f));
            g.drawLine(start.mypoint.x, start.mypoint.y, to.mypoint.x, to.mypoint.y);
        }
    }

    public boolean mouseOver(int x, int y)
    {
        int l1 = (int) Math.sqrt((start.mypoint.x-x)*(start.mypoint.x-x)+(start.mypoint.y-y)*(start.mypoint.y-y));
        int l2 = (int) Math.sqrt((to.mypoint.x-x)*(to.mypoint.x-x)+(to.mypoint.y-y)*(to.mypoint.y-y));
        int l = (int) Math.sqrt((to.mypoint.x-start.mypoint.x)*(to.mypoint.x-start.mypoint.x)+(to.mypoint.y-start.mypoint.y)*(to.mypoint.y-start.mypoint.y));
        if(l1+l2-l==0)
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
        RelatedPoint rpoint = new RelatedPoint();
        rpoint.point = point;
        rpoint.condition = (double)(point.mypoint.x+point.mypoint.y-start.mypoint.x-start.mypoint.y)/(to.mypoint.x+to.mypoint.y-start.mypoint.x-start.mypoint.y);
        related.add(rpoint);
        rpoint.parent = this;
        adjustRelated();
    }

    @Override
    public void adjustRelated()
    {
        for(int i=0;i<related.size();i++)
        {
            related.elementAt(i).point.mypoint.x = (int) (start.mypoint.x + (to.mypoint.x - start.mypoint.x) * related.elementAt(i).condition);
            related.elementAt(i).point.mypoint.y = (int) (start.mypoint.y + (to.mypoint.y - start.mypoint.y) * related.elementAt(i).condition);
        }
    }

    @Override
    public void changeRelated(RelatedPoint point, int x, int y)
    {
        for(int i=0;i<related.size();i++)
        {
            RelatedPoint rp = related.elementAt(i);
            if(rp.equals(point))
            {
                int x1 = to.mypoint.x - start.mypoint.x;
                int y1 = to.mypoint.y - start.mypoint.y;
                int length = (int) Math.sqrt(Math.pow((to.mypoint.x-start.mypoint.x), 2)+Math.pow((to.mypoint.y-start.mypoint.y), 2));
                rp.condition += (double)(x*x1+y*y1)/(double)(length*length);
                if(rp.condition>1)
                    rp.condition = 1;
                if(rp.condition<0)
                    rp.condition = 0;
                rp.point.mypoint.x = (int) (start.mypoint.x + (to.mypoint.x - start.mypoint.x) * related.elementAt(i).condition);
                rp.point.mypoint.y = (int) (start.mypoint.y + (to.mypoint.y - start.mypoint.y) * related.elementAt(i).condition);
            }
        }
    }

    @Override
    public int getTag()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
