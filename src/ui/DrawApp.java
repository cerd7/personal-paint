package src.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import src.core.command.AddStrokeCommand;
import src.core.command.ClearCommand;
import src.core.command.CommandManager;
import src.core.command.EraseStrokeCommand;
import src.core.model.DrawModel;
import src.core.model.Stroke;

public class DrawApp extends JFrame{
    private final DrawModel model = new DrawModel();
    private final CommandManager commandManager = new CommandManager();
    private final CanvasPanel canvasPanel = new CanvasPanel(model);
    private Stroke activeStroke;

    public DrawApp(){
        setTitle("Cerd-Draw");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildToolbar(), BorderLayout.NORTH);
        add(canvasPanel, BorderLayout.CENTER);

        canvasPanel.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e){
                activeStroke = new Stroke();
                activeStroke.addPoint(e.getPoint());
                canvasPanel.setActiveStroke(activeStroke);
            }

            @Override
            public void mouseReleased(MouseEvent e){
                if(activeStroke != null && activeStroke.hasRenderablePoints()){
                    commandManager.execute(new AddStrokeCommand(model, activeStroke));
                }
                activeStroke = null;
                canvasPanel.setActiveStroke(null);
                canvasPanel.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");}

            @Override
            public void mouseEntered(MouseEvent e) {throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");}

            @Override
            public void mouseExited(MouseEvent e) {throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");}
        });

        canvasPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                if(activeStroke == null) return;
                activeStroke.addPoint(e.getPoint());
                canvasPanel.repaint();
            }
        });
    }

    private JToolBar buildToolbar(){
        JToolBar toolbar = new JToolBar();
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
