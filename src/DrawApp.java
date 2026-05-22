package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DrawApp extends JFrame{
    private ArrayList<Point> points = new ArrayList<>();

    public DrawApp(){
        setTitle("Cerd Draw");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel drawPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                //Draw line entry the save points
                for(int i=1;i<points.size(); i++){
                    Point p1 = points.get(i - 1);
                    Point p2 = points.get(i);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        };

        drawPanel.setBackground(Color.WHITE);

        //Mouse events
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                points.add(e.getPoint());
            }
        });

        drawPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e){
                points.add(e.getPoint());
                drawPanel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'mouseMoved'");
            }
        });

        add(drawPanel);
    }
}
