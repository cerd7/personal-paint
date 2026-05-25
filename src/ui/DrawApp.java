package src.ui;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import src.core.command.CommandManager;
import src.core.command.PasteCommand;
import src.core.model.ClipboardBuffer;
import src.core.model.DrawModel;
import src.core.model.Stroke;
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
        ClipboardBuffer clipboard = new ClipboardBuffer();
        Toolbar toolbar = new Toolbar(commandManager, model, canvasPanel);

        canvasPanel.registerKeyBindings(
            () -> {
                if(!model.getSelection().isEmpty()) clipboard.set(model.getSelection());
            },
            () -> {
                if(!clipboard.isEmpty()){
                    Point cursor = canvasPanel.getMousePosition();
                    if(cursor == null) cursor = new Point(0,0);
                    
                    List<Stroke> clones = clipboard.getClones();
                    offsetStrokesToCursor(clones, cursor);
                    commandManager.execute(new PasteCommand(model, clones));
                    canvasPanel.repaint();
                }
            }
        );

        input.register();
        add(canvasPanel, BorderLayout.CENTER);
        add(toolbar.build(), BorderLayout.WEST);
    }

    private void offsetStrokesToCursor(List<Stroke> strokes, Point cursor){
        Rectangle bounds = null;
        for(Stroke s : strokes){
            Rectangle b = s.getBounds();
            if (bounds == null) bounds = new Rectangle(b);
            else bounds = bounds.union(b);
        }
        if (bounds == null) return;

        int dx = cursor.x - bounds.x;
        int dy = cursor.y - bounds.y;

        for(Stroke s : strokes){
            s.translate(dx, dy);
        }
    }
}