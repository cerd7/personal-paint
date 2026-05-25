package src.ui;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import src.core.command.CommandManager;
import src.core.model.DrawModel;
import src.ui.events.CanvasInputHandler;
import src.ui.toolbar.Toolbar;

public class DrawApp extends JFrame {
    DrawModel model = new DrawModel();
    CommandManager commandManager =  new CommandManager();
    CanvasPanel canvasPanel = new CanvasPanel(model);

    public DrawApp() {
        setTitle("Cerd Draw");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        CanvasInputHandler input = new CanvasInputHandler(model, commandManager, canvasPanel);
        input.register();
        add(canvasPanel, BorderLayout.CENTER);

        Toolbar toolbar = new Toolbar(commandManager, model, canvasPanel);
        add(toolbar.build(), BorderLayout.WEST);
    }
}