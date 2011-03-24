package thugeometrystudio;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;

public class Main
{
    public static JButton func=new JButton("��������");
    public static JButton geom=new JButton("���λ���");
    public static JButton wiring=new JButton("��·����");
    public static JButton help=new JButton("�����ĵ�");

    public static void main(String[] args)
    {
        JFrame app=new JFrame("ͼ�ι����䡪���廪���");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(1000, 700);
        Container c=app.getContentPane();
        c.setLayout(new GridLayout(2,2));
        c.add(func);
        c.add(geom);
        c.add(wiring);
        c.add(help);
        func.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                FuncPainting fp=new FuncPainting();
            }
        });
        geom.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GeomPainting gp=new GeomPainting();
            }
        });
        wiring.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                LinePainting lp=new LinePainting();
            }
        });
        app.setVisible(true);

    }

}
