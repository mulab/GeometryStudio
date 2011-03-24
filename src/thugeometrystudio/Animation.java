package thugeometrystudio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Animation implements ActionListener
{
    Timer timer;
    RelatedPoint rp = null;
    Shape parent = null;
    boolean m_stop = true;
    int delay = 50;
    boolean toward = false;
    int id = 0;

    public Animation(RelatedPoint rp, Shape parent)
    {
        timer = new Timer(delay,this);
        timer.setInitialDelay(0);
        timer.setCoalesce(true);
        this.rp = rp;
        this.parent = parent;
    }

    public void start()
    {
        this.timer.start();
    }

    public void stop()
    {
        this.timer.stop();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(parent instanceof Line)
        {
            if(!toward)
            {
                if(rp.condition<1)
                    rp.condition+=0.01;
                else
                {
                    toward = true;
                    rp.condition = 1;
                }
            }
            else
            {
                if(rp.condition>0)
                    rp.condition-=0.01;
                else
                {
                    toward = false;
                    rp.condition = 0;
                }
            }
        }
        else if(parent instanceof Circle)
        {
            if(!toward)
            {
                rp.condition+=0.02;
                if(rp.condition>Math.PI)
                    rp.condition = -Math.PI;
            }
            else
            {
                rp.condition-=0.02;
                if(rp.condition<-Math.PI)
                    rp.condition = Math.PI;
            }
        }
        GeomPainting.geom.repaint();
    }
}
