package thugeometrystudio;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;

public class Main
{
    public static JButton func=new JButton("函数绘制");
    public static JButton geom=new JButton("几何画板");
    public static JButton wiring=new JButton("电路布线");
    public static JButton help=new JButton("帮助文档");

    public static void main(String[] args)
    {
        JFrame app=new JFrame("图形工作间——清华软件");
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
