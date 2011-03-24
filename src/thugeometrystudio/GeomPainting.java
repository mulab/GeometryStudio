package thugeometrystudio;

import java.awt.Button;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;



class WorkSpace extends JPanel
{
    @Override
    public void paint(Graphics g)
    {
        g.drawImage(GeomFrame.bk, 0, 0,1800,1900,null);
        for(int i=0;i<GeomFrame.vp.size();i++)
        {
            GeomFrame.vp.elementAt(i).drawPoint(g);
        }
        for(int i=0;i<GeomFrame.vl.size();i++)
        {
            GeomFrame.vl.elementAt(i).drawRelatedPoint(g);
        }
        for(int i=0;i<GeomFrame.vc.size();i++)
        {
            GeomFrame.vc.elementAt(i).drawRelatedPoint(g);
        }
        for(int i=0;i<GeomFrame.vl.size();i++)
        {
            GeomFrame.vl.elementAt(i).drawLine(g);
        }
        for(int i=0;i<GeomFrame.vc.size();i++)
        {
            GeomFrame.vc.elementAt(i).drawCircle(g);
        }
    }
}

class GeomFrame extends JFrame
{
    public static WorkSpace workspace=new WorkSpace();
    public static JPanel control=new JPanel();
    public static int workstate=0;//1,Point 2,Line  3,Straight  4,Circle  5,Polygon
    Container c;
    //绘制用
    public static Vector<MyPoint> vp=new Vector<MyPoint>();
    public static Vector<MyPoint> vps=new Vector<MyPoint>();
    public static Vector<RelatedPoint> vprs=new Vector<RelatedPoint>();
    public static Vector<Line> vl=new Vector<Line>();
    public static Vector<Straight> vs=new Vector<Straight>();
    public static Vector<Circle> vc=new Vector<Circle>();
    public static Vector<Polygon> vpp=new Vector<Polygon>();
    //计算用
    public static Vector<MyPoint> vpmath=new Vector<MyPoint>();
    public static Vector<Line> vlmath=new Vector<Line>();
    public static Vector<Circle> vcmath=new Vector<Circle>();

    public static MyPoint start = null;
    public static MyPoint end = null;
    public static BufferedImage bk;
    public static boolean isfinish = false;
    private int preX = 0, preY = 0;
    private boolean flag = false;
    //动画集
    public Vector<Animation> animation = new Vector<Animation>();
    public Vector<Button> animationbutton = new Vector<Button>();
    public RelatedPoint relatepoint = null;
    public Shape parent = null;
    public JTextArea textresult = new JTextArea();
    public int tag = 0;

    public GeomFrame() 
    {
        super("几何画板");
        c = getContentPane();
        c.setLayout(null);
        workspace.setBounds(10, 10, 800, 615);
        c.add(workspace);
        textresult.setBounds(830, 300, 150, 315);
        c.add(textresult);
        
        workspace.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if(!flag)
                {
                    preX = e.getX();
                    preY = e.getY();
                    flag = true;
                }            
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if(vps!=null)
                {
                    for(int i=0;i<vps.size();i++)
                    {
                        vps.elementAt(i).mypoint.x += e.getX() - preX;
                        vps.elementAt(i).mypoint.y += e.getY() - preY;
                    }
                }
                if(vprs!=null)
                {
                    for(int i=0;i<vprs.size();i++)
                    {
                        if(vprs.elementAt(i).parent instanceof Line)
                            vprs.elementAt(i).parent.changeRelated(vprs.elementAt(i), e.getX() - preX, e.getY() - preY);
                        else
                            if((e.getX() - preX)!=0&&(e.getY() - preY)!=0)
                                vprs.elementAt(i).parent.changeRelated(vprs.elementAt(i), e.getX(), e.getY());
                    }
                }
                repaint();
                flag = false;
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(workstate == 0)
                {
                    if(!setSelected(e))
                    {
                        for(int i=0;i<vp.size();i++)
                        {
                            if(vp.elementAt(i).isSelected) vp.elementAt(i).isSelected = false;
                        }
                        for(int i=0;i<vl.size();i++)
                        {
                            if(vl.elementAt(i).isSelected) vl.elementAt(i).isSelected = false;
                            for(int j=0;j<vl.elementAt(i).related.size();j++)
                            {
                                vl.elementAt(i).related.elementAt(j).point.isSelected = false;
                            }
                        }
                        for(int i=0;i<vc.size();i++)
                        {
                            if(vc.elementAt(i).isSelected) vc.elementAt(i).isSelected = false;
                            for(int j=0;j<vc.elementAt(i).related.size();j++)
                            {
                                vc.elementAt(i).related.elementAt(j).point.isSelected = false;
                            }
                        }
                        vps.clear();
                        vprs.clear();
                        vpmath.clear();
                        vlmath.clear();
                        vcmath.clear();
                        relatepoint = null;
                        parent =null;
                    }
                    repaint();
                }
                else if(workstate == 1)
                {
                    if(!isMouseOver(e))
                    {
                        MyPoint temp = new MyPoint(e.getX(),e.getY());
                        temp.tag = ++tag;
                        vp.add(temp);
                        repaint();
                    }
                    else
                    {
                        Shape select = getSelected(e);
                        MyPoint temp = new MyPoint(e.getX(),e.getY());
                        temp.tag = ++tag;
                        select.setRelated(temp);
                    }
                }
                else if(workstate == 2)
                {
                    if(!isfinish)
                    {
                        if(!isMouseOver(e))
                        {
                            start = new MyPoint(e.getX(),e.getY());
                            start.tag = ++tag;
                            vp.add(start);
                            isfinish = true;
                        }
                        else
                        {
                            Shape select = getSelected(e);
                            if(select instanceof MyPoint)
                            {
                                start = (MyPoint)select;
                                start.tag = select.getTag();
                            }
                            else
                            {
                                start = new MyPoint(e.getX(),e.getY());
                                start.tag = ++tag;
                                select.setRelated(start);
                            }
                            isfinish = true;
                        }
                    }
                    else
                    {
                        if(!isMouseOver(e))
                        {
                            end = new MyPoint(e.getX(),e.getY());
                            end.tag = ++tag;
                            Line temp = new Line(start,end);
                            vp.add(end);
                            vl.add(temp);
                            isfinish = false;
                        }
                        else
                        {
                            Shape select = getSelected(e);
                            if(select instanceof MyPoint)
                            {
                                end = (MyPoint)select;
                                end.tag = select.getTag();
                            }
                            else
                            {
                                end = new MyPoint(e.getX(),e.getY());
                                end.tag = ++tag;
                                select.setRelated(end);
                            }
                            Line temp = new Line(start,end);
                            vl.add(temp);
                            isfinish = false;
                        }
                    }
                    repaint();
                }
                else if(workstate==4)
                {
                    if(!isfinish)
                    {
                        if(!isMouseOver(e))
                        {
                            start = new MyPoint(e.getX(),e.getY());
                            start.tag = ++tag;
                            vp.add(start);
                            isfinish = true;
                        }
                        else
                        {
                            Shape select = getSelected(e);
                            if(select instanceof MyPoint)
                            {
                                start = (MyPoint)select;
                                start.tag = select.getTag();
                            }
                            else
                            {
                                start = new MyPoint(e.getX(),e.getY());
                                start.tag = ++tag;
                                select.setRelated(start);
                            }
                            isfinish = true;
                        }
                    }
                    else
                    {
                        if(!isMouseOver(e))
                        {
                            end = new MyPoint(e.getX(),e.getY());
                            end.tag = ++tag;
                            Circle temp = new Circle(start,end);
                            vp.add(end);
                            vc.add(temp);
                            isfinish = false;
                        }
                        else
                        {
                            Shape select = getSelected(e);
                            if(select instanceof MyPoint)
                            {
                                end = (MyPoint)select;
                                end.tag = select.getTag();
                            }
                            else
                            {
                                end = new MyPoint(e.getX(),e.getY());
                                end.tag = ++tag;
                                select.setRelated(end);
                            }
                            Circle temp = new Circle(start,end);
                            vc.add(temp);
                            isfinish = false;
                        }
                    }
                    repaint();
                }
            }
        });
        workspace.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                if(isMouseOver(e))
                {
                    workspace.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
                }
                else
                {
                    workspace.setCursor(Cursor.getDefaultCursor());
                }
                repaint();
            }
        });
        try
        {
            bk = ImageIO.read(new File("image/bk.jpg"));
        } 
        catch (IOException ex)
        {
            Logger.getLogger(GeomFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        JMenuBar mBar = new JMenuBar();                                //创建菜单栏
        setJMenuBar(mBar);
        JMenu []m = {new JMenu("文件"),new JMenu("编辑"),new JMenu("基本元素"),new JMenu("基本计算"),new JMenu("动画")};
        JMenuItem [][]mI =
        {
            {new JMenuItem("新建")},
            {new JMenuItem("还原"),new JMenuItem("撤销"),new JMenuItem("删除")},
            {new JMenuItem("点"),new JMenuItem("线段"),new JMenuItem("直线"),new JMenuItem("圆"),new JMenuItem("多边形"),new JMenuItem("光标")},
            {new JMenuItem("计算长度"),new JMenuItem("计算角度"),new JMenuItem("计算面积")},
            {new JMenuItem("生成动画"),new JMenuItem("停止")}
        };
        for(int i=0;i<m.length;i++)                               //添加菜单项
        {
            mBar.add(m[i]);
            for(int j=0;j<mI[i].length;j++)
            {
                m[i].add(mI[i][j]);
            }
        }
        mI[0][0].addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                init();
                repaint();
            }
        });
        mI[2][0].addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                workstate = 1;
            }
        });
        mI[2][1].addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                workstate = 2;
            }
        });
        mI[2][3].addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                workstate = 4;
            }
        });
        mI[2][5].addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                workstate = 0;
            }
        });
        mI[3][0].addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                for(int i=0;i<vlmath.size();i++)
                {
                    double result = MathMethod.getLength(vlmath.elementAt(i));
                    textresult.append("线段"+vl.elementAt(i).start.tag+vl.elementAt(i).to.tag+"的长度为："+result+"\n");
                }
                for(int i=0;i<vcmath.size();i++)
                {
                    double result = MathMethod.getLength(vcmath.elementAt(i));
                    textresult.append("圆"+vcmath.elementAt(i).center.tag+"的周长为："+result+"\n");
                }
            }
        });
        mI[3][1].addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                double result = MathMethod.getAngle(vpmath.elementAt(0), vpmath.elementAt(1), vpmath.elementAt(2));
                textresult.append("角"+vpmath.elementAt(0).tag+vpmath.elementAt(1).tag+vpmath.elementAt(2).tag+"的度数为："+result+"\n");
            }
        });
        mI[4][0].addActionListener(new ActionListener() //生成动画
        {
            public void actionPerformed(ActionEvent e)
            {
                if(relatepoint!=null&&parent!=null&&relatepoint.parent.equals(parent))
                {
                    final Animation temp = new Animation(relatepoint,parent);
                    animation.add(temp);
                    temp.id = animation.size();
                    final Button button = new Button("开始动画"+temp.id);
                    animationbutton.add(button);
                    button.setBounds(830, 10+animation.size()*30, 100, 20);
                    c.add(button);
                    button.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e) 
                        {
                            if(temp.m_stop)
                            {
                                temp.start();
                                button.setLabel("停止动画"+temp.id);
                                temp.m_stop = false;
                            }
                            else
                            {
                                temp.stop();
                                button.setLabel("开始动画"+temp.id);
                                temp.m_stop = true;
                            }
                        }
                    });
                }
            }
        });
    }

    public void init()
    {
        workstate = 0;
        vp.clear();
        vl.clear();
        vs.clear();
        vc.clear();
        vpp.clear();
        vps.clear();
        vprs.clear();
        vpmath.clear();
        vlmath.clear();
        vcmath.clear();
        relatepoint = null;
        parent = null;
        isfinish = false;
        flag = false;
        preX = 0;
        preY = 0;
        tag = 0;
        for(int i=0;i<animationbutton.size();i++)
            animationbutton.elementAt(i).setVisible(false);
        animationbutton.clear();
        animation.clear();
        textresult.setText(null);
    }

    public void setAllOverFalse()
    {
        for(int i=0;i<vp.size();i++)
        {
            vp.elementAt(i).isOver = false;
        }
        for(int i=0;i<vl.size();i++)
        {
            vl.elementAt(i).isOver = false;
            for(int j=0;j<vl.elementAt(i).related.size();j++)
            {
                vl.elementAt(i).related.elementAt(j).point.isOver = false;
            }
        }
        for(int i=0;i<vc.size();i++)
        {
            vc.elementAt(i).isOver = false;
            for(int j=0;j<vc.elementAt(i).related.size();j++)
            {
                vc.elementAt(i).related.elementAt(j).point.isOver = false;
            }
        }
    }

    public boolean isMouseOver(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();
        for(int i=0;i<vp.size();i++)
        {
            if(vp.elementAt(i).mouseOver(x, y)) 
            {
                setAllOverFalse();
                vp.elementAt(i).isOver = true;
                return true;
            }
            else
                vp.elementAt(i).isOver = false;
        }
        for(int i=0;i<vl.size();i++)
        {
            for(int j=0;j<vl.elementAt(i).related.size();j++)
            {
                if(vl.elementAt(i).related.elementAt(j).point.mouseOver(x, y))
                {
                    setAllOverFalse();
                    vl.elementAt(i).related.elementAt(j).point.isOver = true;
                    return true;
                }
                else
                    vl.elementAt(i).related.elementAt(j).point.isOver = false;
            }
        }
        for(int i=0;i<vc.size();i++)
        {
            for(int j=0;j<vc.elementAt(i).related.size();j++)
            {
                if(vc.elementAt(i).related.elementAt(j).point.mouseOver(x, y))
                {
                    setAllOverFalse();
                    vc.elementAt(i).related.elementAt(j).point.isOver = true;
                    return true;
                }
                else
                    vc.elementAt(i).related.elementAt(j).point.isOver = false;
            }
        }
        for(int i=0;i<vl.size();i++)
        {
            if(vl.elementAt(i).mouseOver(x, y))
            {
                setAllOverFalse();
                vl.elementAt(i).isOver = true;
                return true;
            }
            else
                vl.elementAt(i).isOver = false;
        }
        for(int i=0;i<vc.size();i++)
        {
            if(vc.elementAt(i).mouseOver(x, y))
            {
                setAllOverFalse();
                vc.elementAt(i).isOver = true;
                return true;
            }
            else
                vc.elementAt(i).isOver = false;
        }
        return false;
    }

    public boolean setSelected(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();
        for(int i=0;i<vp.size();i++)
        {
            if(vp.elementAt(i).mouseOver(x, y))
            {
                if(vp.elementAt(i).isSelected == false)
                {
                    vp.elementAt(i).isSelected = true;
                    vps.add(vp.elementAt(i));
                    vpmath.add(vp.elementAt(i));
                }
                else
                {
                    vp.elementAt(i).isSelected = false;
                    vps.removeElement(vp.elementAt(i));
                    vpmath.removeElement(vp.elementAt(i));
                }
                return true;
            }
        }
        for(int i=0;i<vl.size();i++)
        {
            for(int j=0;j<vl.elementAt(i).related.size();j++)
            {
                MyPoint t = vl.elementAt(i).related.elementAt(j).point;
                if(t.mouseOver(x, y))
                {
                    RelatedPoint rp = vl.elementAt(i).related.elementAt(j);
                    if(t.isSelected == false)
                    {
                        t.isSelected = true;
                        vprs.add(rp);
                        vpmath.add(rp.point);
                        relatepoint = rp;
                    }
                    else
                    {
                        t.isSelected = false;
                        vprs.remove(rp);
                        vpmath.remove(rp.point);
                        relatepoint = null;
                    }

                    return true;
                }
            }
            if(vl.elementAt(i).mouseOver(x, y))
            {
                if(vl.elementAt(i).isSelected == false)
                {
                    vl.elementAt(i).isSelected = true;
                    vps.add(vl.elementAt(i).start);
                    vps.add(vl.elementAt(i).to);
                    vlmath.add(vl.elementAt(i));
                    parent = vl.elementAt(i);
                }
                else
                {
                    vl.elementAt(i).isSelected = false;
                    vps.removeElement(vl.elementAt(i).start);
                    vps.removeElement(vl.elementAt(i).to);
                    vlmath.remove(vl.elementAt(i));
                    parent = null;
                }
                return true;
            }
        }
        for(int i=0;i<vc.size();i++)
        {
            for(int j=0;j<vc.elementAt(i).related.size();j++)
            {
                MyPoint t = vc.elementAt(i).related.elementAt(j).point;
                if(t.mouseOver(x, y))
                {
                    RelatedPoint rp = vc.elementAt(i).related.elementAt(j);
                    if(t.isSelected == false)
                    {
                        t.isSelected = true;
                        vprs.add(rp);
                        vpmath.add(rp.point);
                        relatepoint = rp;
                    }
                    else
                    {
                        t.isSelected = false;
                        vprs.remove(rp);
                        vpmath.remove(rp.point);
                        relatepoint = null;
                    }

                    return true;
                }
            }
            if(vc.elementAt(i).mouseOver(x, y))
            {
                if(vc.elementAt(i).isSelected == false)
                {
                    vc.elementAt(i).isSelected = true;
                    vps.add(vc.elementAt(i).center);
                    vps.add(vc.elementAt(i).to);
                    vcmath.add(vc.elementAt(i));
                    parent = vc.elementAt(i);
                }
                else
                {
                    vc.elementAt(i).isSelected = false;
                    vps.removeElement(vc.elementAt(i).center);
                    vps.removeElement(vc.elementAt(i).to);
                    vcmath.remove(vc.elementAt(i));
                    parent = null;
                }
                return true;
            }
        }
        return false;
    }

    public Shape getSelected(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();
        for(int i=0;i<vp.size();i++)
        {
            if(vp.elementAt(i).mouseOver(x, y))
            {
                return vp.elementAt(i);
            }
        }
        for(int i=0;i<vl.size();i++)
        {
            for(int j=0;j<vl.elementAt(i).related.size();j++)
            {
                if(vl.elementAt(i).related.elementAt(j).point.mouseOver(x, y))
                    return vl.elementAt(i).related.elementAt(j).point;
            }
        }
        for(int i=0;i<vc.size();i++)
        {
            for(int j=0;j<vc.elementAt(i).related.size();j++)
            {
                if(vc.elementAt(i).related.elementAt(j).point.mouseOver(x, y))
                    return vc.elementAt(i).related.elementAt(j).point;
            }
        }
        for(int i=0;i<vl.size();i++)
        {
            if(vl.elementAt(i).mouseOver(x, y))
            {
                return vl.elementAt(i);
            }
        }
        for(int i=0;i<vc.size();i++)
        {
            if(vc.elementAt(i).mouseOver(x, y))
            {
                return vc.elementAt(i);
            }
        }
        return null;
    }
}

public class GeomPainting
{
    public static GeomFrame geom=new GeomFrame();

    public GeomPainting()
    {
        geom.setSize(1000, 700);
        geom.setVisible(true);
    }
}
