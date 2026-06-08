package src.com.cerd.app.ui;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;

import src.com.cerd.app.core.model.DrawModel;
import src.com.cerd.app.core.model.Stroke;

public class CanvasPanel extends JPanel {
    private final DrawModel model;
    private Stroke activeStroke;
    private Rectangle selectionRect;

    private BufferedImage canvasBuffer;
    private boolean needsBufferUpdate = true;

    public CanvasPanel(DrawModel model){
        this.model = model;
        setBackground(Color.WHITE);

        addComponentListener(new java.awt.event.ComponentAdapter(){
            public void componentResized(java.awt.event.ComponentEvent evt){
                updateBuffer();
            }
        });
    }

    public void setActiveStroke(Stroke stroke){
        this.activeStroke = stroke;
    }

    public void setSelectionRect(Rectangle rect) {
        this.selectionRect = rect;
    }

    public void repaint(){
        needsBufferUpdate = true;
        super.repaint();
    }

    public void repaintActiveStrokeOnly(){
        needsBufferUpdate = true;
        super.repaint();
    }

    private void updateBuffer(){
        if(getWidth() <= 0 || getHeight() <= 0) return;

        //Cria um buffer do tamanho do painel.
        canvasBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = canvasBuffer.createGraphics();

        //Limpa o fundo do buffer.
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for(Stroke stroke : model.getStrokes()) drawStroke(g2d, stroke);
        
        g2d.dispose();
        needsBufferUpdate = false;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        if(needsBufferUpdate || canvasBuffer == null){
            updateBuffer();
        }

        if(canvasBuffer != null){
            g.drawImage(canvasBuffer, 0, 0, null);
        }

        //Desenha apenas as interações em tempo real (active stroke).
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(activeStroke != null){
            g2d.setColor(Color.BLACK);
            drawStroke(g2d, activeStroke);
        }

        if (selectionRect != null) {
            g2d.setColor(new Color(0, 120, 215));
            float[] dash = {5.0f};
            g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            g2d.drawRect(selectionRect.x, selectionRect.y, selectionRect.width, selectionRect.height);
        }
    }

    private void drawStroke(Graphics g, Stroke stroke){
        Point prev = null;
        for(Point point : stroke.getPoints()){
            if(prev != null) g.drawLine(prev.x, prev.y, point.x, point.y);
            prev = point;
        }
    }

    public void registerKeyBindings(Runnable onCopy, Runnable onPaste){
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("control C"), "copy");
        am.put("copy", new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                onCopy.run();
            }
        });

        im.put(KeyStroke.getKeyStroke("control V"), "paste");
        am.put("paste", new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                onPaste.run();
            }
        });
    }
}