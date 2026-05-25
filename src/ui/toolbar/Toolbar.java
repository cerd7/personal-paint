package src.ui.toolbar;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import src.core.command.ClearCommand;
import src.core.command.CommandManager;
import src.core.command.EraseStrokeCommand;
import src.core.model.DrawModel;
import src.ui.CanvasPanel;

public class Toolbar {
    private final CommandManager commandManager;
    private final DrawModel model;
    private final CanvasPanel canvasPanel;

    public Toolbar (CommandManager commandManager, DrawModel model, CanvasPanel canvasPanel){
        this.commandManager = commandManager;
        this.model = model;
        this.canvasPanel = canvasPanel;
    }
    
    public JToolBar build() {
        JToolBar toolbar = new JToolBar();
        toolbar.setOrientation(SwingConstants.VERTICAL);
        toolbar.setFloatable(false);

        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");
        JButton eraseButton = new JButton("Erase");
        JButton clearButton = new JButton("Clear");

        undoButton.addActionListener(event -> {
            commandManager.undo();
            canvasPanel.repaint();
        });

        redoButton.addActionListener(event -> {
            commandManager.redo();
            canvasPanel.repaint();
        });

        eraseButton.addActionListener(event -> {
            commandManager.execute(new EraseStrokeCommand(model));
            canvasPanel.repaint();
        });

        clearButton.addActionListener(event -> {
            commandManager.execute(new ClearCommand(model));
            canvasPanel.repaint();
        });

        toolbar.add(undoButton);
        toolbar.add(redoButton);
        toolbar.add(eraseButton);
        toolbar.add(clearButton);

        return toolbar;
    }
}
