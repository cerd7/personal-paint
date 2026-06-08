package src.com.cerd.app;

import javax.swing.SwingUtilities;

import src.com.cerd.app.ui.DrawApp;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new DrawApp().setVisible(true));
    }
}
