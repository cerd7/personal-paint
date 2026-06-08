package src.com.cerd.app.ui.components;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class CustomTitleBar extends JPanel{
    private Point initialClick;
    private final JFrame parentFrame;

    public CustomTitleBar(JFrame parentFrame, String title) {
        this.parentFrame = parentFrame;
    
        setLayout(new BorderLayout());
        //Cor da barra superior (dark mode)
        setBackground(new Color(30,30,30));
        setPreferredSize(new Dimension(parentFrame.getWidth(), 35));

        //Título à esquerda
        JLabel titleLabel = new JLabel(" " + title);
        titleLabel.setForeground(Color.LIGHT_GRAY);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(titleLabel, BorderLayout.WEST);

        //Botão de fechar
        JButton closeBtn = new JButton("X");
        closeBtn.setBackground(new Color(232,17,35));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.addActionListener(e -> System.exit(0));

        //Painel para alinhar os controles à direita.
        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,0));
        rightControls.add(closeBtn);
        add(rightControls, BorderLayout.EAST);


        //Movimentação da janela
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                initialClick = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent e){
                int thisX = parentFrame.getLocation().x;
                int thisY = parentFrame.getLocation().y;
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                parentFrame.setLocation(thisX + xMoved, thisY + yMoved);
            }
        });
    }
}
