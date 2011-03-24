/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thugeometrystudio;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;


public class Circle extends Shape
{
    public MyPoint center;
    public MyPoint to;
    public MyPoint rcenter=center;
    public int radius=0;
    public Line axis;
    public boolean isSelected = false;
    public boolean isOver = false;
    public Vector<RelatedPoint> related=new Vector<RelatedPoint>();
    public Vector<Circle> trans=new Vector<Circle>();
    public Vector<Circle> rotate=new Vector<Circle>();
    public Vector<Circle> reflect=new Vector<Circle>();

    public Circle(MyPoint center,MyPoint to)
    {
        this.center = center;
        this.to = to;
    }

    public void drawRelatedPoint(Graphics g)
    {
        adjustRelated();
        for(int i=0;i<related.size();i++)
        {
            related.elementAt(i).point.drawPoint(g);
        }
    }

    public void drawCircle(Graphics g)
    {
        if(!isSelected&&!isOver)
        {
            this.radius = (int) Math.sqrt((center.mypoint.x-to.mypoint.x)*(center.mypoint.x-to.mypoint.x)+(center.mypoint.y-to.mypoint.y)*(center.mypoint.y-to.mypoint.y));
            g.setColor(Color.BLUE);
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(2.5f));
            g.drawOval(center.mypoint.x-radius, center.mypoint.y-radius, 2*radius, 2*radius);
        }
        else if(isSelected)
        {
            this.radius = (int) Math.sqrt((center.mypoint.x-to.mypoint.x)*(center.mypoint.x-to.mypoint.x)+(center.mypoint.y-to.mypoint.y)*(center.mypoint.y-to.mypoint.y));
            g.setColor(Color.BLUE);
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(2.5f));
            g.drawOval(center.mypoint.x-radius, center.mypoint.y-radius, 2*radius, 2*radius);
            g2d.setStroke(new BasicStroke(0.5f));
            g.drawOval(center.mypoint.x-radius+5, center.mypoint.y-radius+5, 2*radius-10, 2*radius-10);
            g.drawOval(center.mypoint.x-radius-5, center.mypoint.y-radius-5, 2*radius+10, 2*radius+10);
        }
        else if(!isSelected&&isOver)
        {
            this.radius = (int) Math.sqrt((center.mypoint.x-to.mypoint.x)*(center.mypoint.x-to.mypoint.x)+(center.mypoint.y-to.mypoint.y)*(center.mypoint.y-to.mypoint.y));
            g.setColor(Color.RED);
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(2.5f));
            g.drawOval(center.mypoint.x-radius, center.mypoint.y-radius, 2*radius, 2*radius);
        }
    }

    public boolean mouseOver(int x, int y)
    {
        int l = (int) Math.sqrt((center.mypoint.x-x)*(center.mypoint.x-x)+(center.mypoint.y-y)*(center.mypoint.y-y));
        if(l>=radius-5&&l<=radius+5)
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
        rpoint.parent = this;
        if(point.mypoint.x>=center.mypoint.x)
            rpoint.condition = (double)Math.asin((double)(point.mypoint.y-center.mypoint.y)/(double)radius);
        else if(point.mypoint.y>=center.mypoint.y)
            rpoint.condition = Math.PI-(double)Math.asin((double)(point.mypoint.y-center.mypoint.y)/(double)radius);
        else
            rpoint.condition = -Math.PI-(double)Math.asin((double)(point.mypoint.y-center.mypoint.y)/(double)radius);
        related.add(rpoint);
        adjustRelated();
    }

    @Override
    public void adjustRelated()
    {
        this.radius = (int) Math.sqrt((center.mypoint.x-to.mypoint.x)*(center.mypoint.x-to.mypoint.x)+(center.mypoint.y-to.mypoint.y)*(center.mypoint.y-to.mypoint.y));
        for(int i=0;i<related.size();i++)
        {
            related.elementAt(i).point.mypoint.x = (int) (center.mypoint.x + (double)Math.cos(related.elementAt(i).condition)*radius);
            related.elementAt(i).point.mypoint.y = (int) (center.mypoint.y + (double)Math.sin(related.elementAt(i).condition)*radius);
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
                if(x>=center.mypoint.x)
                    rp.condition = (double)Math.asin((double)(y-center.mypoint.y)/(double)radius);
                else
                    rp.condition = Math.PI-(double)Math.asin((double)(y-center.mypoint.y)/(double)radius);
                rp.point.mypoint.x = (int) (center.mypoint.x + (double)Math.cos(related.elementAt(i).condition)*radius);
                rp.point.mypoint.y = (int) (center.mypoint.y + (double)Math.sin(related.elementAt(i).condition)*radius);
            }
        }
    }

    @Override
    public int getTag()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
