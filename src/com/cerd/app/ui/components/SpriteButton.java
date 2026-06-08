package src.com.cerd.app.ui.components;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SpriteButton extends JButton {

    public SpriteButton(String normalIconPath, String hoverIconPath) {
        //Remoção dos estilos genéricos do swing.
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setMargin(new Insets(0,0,0,0));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        //Carrega as imagens do Classpath
        ImageIcon normal = loadIcon(normalIconPath);
        if(normal != null) setIcon(normal);
        else setText("Error_Img");
    
        if(hoverIconPath != null){
            ImageIcon hover = loadIcon(hoverIconPath);
            if(hover != null) setRolloverIcon(hover);
        }
    }

    private ImageIcon loadIcon(String path){
        URL imgUrl = getClass().getResource(path);
        if(imgUrl !=  null) return new ImageIcon(imgUrl);
        
        System.err.println("Aviso: Sprite não encontrada -> " + path);
        return null;
    }
}
