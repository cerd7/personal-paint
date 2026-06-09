package src.com.cerd.app.ui;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import src.com.cerd.app.core.command.CommandManager;
import src.com.cerd.app.core.command.PasteCommand;
import src.com.cerd.app.core.model.ClipboardBuffer;
import src.com.cerd.app.core.model.DrawModel;
import src.com.cerd.app.core.model.DrawingModel;
import src.com.cerd.app.core.model.Stroke;

import src.com.cerd.app.ui.components.CustomTitleBar;
import src.com.cerd.app.ui.events.CanvasInputHandler;
import src.com.cerd.app.ui.toolbar.Toolbar;

public class DrawApp extends JFrame {
    private final DrawingModel model = new DrawModel();
    private final CommandManager commandManager =  new CommandManager();
    private final CanvasPanel canvasPanel = new CanvasPanel(model);

    public DrawApp() {
        setUndecorated(true);

        setTitle("Cerd Draw");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        CustomTitleBar titleBar = new CustomTitleBar(this, "Cerd Draw - prototype");
        add(titleBar, BorderLayout.NORTH);

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
                    canvasPanel.invalidateBuffer();
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
            if(!s.hasRenderablePoints()) continue;

            Rectangle b = s.getBounds();
            if (bounds == null) bounds = b;
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
