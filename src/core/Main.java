package src.core;

import javax.swing.SwingUtilities;

import src.ui.DrawApp;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new DrawApp().setVisible(true));
    }
}
