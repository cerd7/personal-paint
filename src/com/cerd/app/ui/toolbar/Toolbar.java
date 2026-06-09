package src.com.cerd.app.ui.toolbar;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import src.com.cerd.app.core.command.ClearCommand;
import src.com.cerd.app.core.command.CommandManager;
import src.com.cerd.app.core.command.EraseStrokeCommand;
import src.com.cerd.app.core.model.DrawingModel;
import src.com.cerd.app.ui.CanvasSurface;

public class Toolbar {
    private final CommandManager commandManager;
    private final DrawingModel model;
    private final CanvasSurface canvasPanel;

    public Toolbar (CommandManager commandManager, DrawingModel model, CanvasSurface canvasPanel){
        this.commandManager = commandManager;
        this.model = model;
        this.canvasPanel = canvasPanel;
    }
    
    public JToolBar build() {
        JToolBar toolbar = new JToolBar();
        toolbar.setOrientation(SwingConstants.VERTICAL);
        toolbar.setFloatable(false);

        JButton undoButton = new JButton("Undo");
        undoButton.setBorderPainted(false);
        
        JButton redoButton = new JButton("Redo");
        redoButton.setBorderPainted(false);
        
        JButton eraseButton = new JButton("Erase");
        eraseButton.setBorderPainted(false);
        
        JButton clearButton = new JButton("Clear");
        clearButton.setBorderPainted(false);
        
        //clearButton.setIcon(new ImageIcon("C:\\Users\\caio.rodrigues\\Documents\\dev\\cerddev\\personal-paint\\src\\resources\\Design sem nome.png"));

        undoButton.addActionListener(event -> {
            commandManager.undo();
            canvasPanel.invalidateBuffer();
        });

        redoButton.addActionListener(event -> {
            commandManager.redo();
            canvasPanel.invalidateBuffer();
        });

        eraseButton.addActionListener(event -> {
            if(model.getStrokes().isEmpty()) return;

            commandManager.execute(new EraseStrokeCommand(model));
            canvasPanel.invalidateBuffer();
        });

        clearButton.addActionListener(event -> {
            if(model.getStrokes().isEmpty()) return;

            commandManager.execute(new ClearCommand(model));
            canvasPanel.invalidateBuffer();
        });

        toolbar.add(undoButton);
        toolbar.add(redoButton);
        toolbar.add(eraseButton);
        toolbar.add(clearButton);

        return toolbar;
    }
}
