package src.com.cerd.app.ui;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;

import src.com.cerd.app.core.model.DrawModel;
import src.com.cerd.app.core.model.Stroke;

public class CanvasPanel extends JPanel {
    private final DrawModel model;

    private Rectangle selectionRect;
    private BufferedImage canvasBuffer;
    private BufferedImage activeBuffer;
    private Point activeLastPoint;
    
    private static final int DIRTY_PAD = 4;
    private boolean needsBufferUpdate = true;

    private static final Color SELECTION_COLOR = new Color(0, 120, 215);
    private static final java.awt.Stroke SELECTION_STROKE = 
        new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f); 

    public CanvasPanel(DrawModel model){
        this.model = model;
        setBackground(Color.WHITE);

        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent evt){
                invalidateBuffer();
            }
        });
    }

    public void beginActiveStroke(Point start){
        ensureActiveBuffer();
        clearActiveBuffer();
        activeLastPoint = new Point(start);
    }

    public void appendActivePoint(Point point){
        if(activeLastPoint == null){
            beginActiveStroke(point);
            return;
        }

        ensureActiveBuffer();
        if(activeBuffer == null) return;

        Point current = new Point(point);

        Graphics2D g2d = activeBuffer.createGraphics();
        configureStrokeGraphics(g2d);
        g2d.drawLine(activeLastPoint.x, activeLastPoint.y, current.x, current.y);
        g2d.dispose();

        repaintDirtyLine(activeLastPoint, current);
        activeLastPoint = current;
    }

    public void clearActiveStroke(){
        activeLastPoint = null;
        clearActiveBuffer();
        repaint();
    }

    private void configureStrokeGraphics(Graphics2D g2d){
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void ensureActiveBuffer(){
        int w = getWidth();
        int h = getHeight();

        if(w <= 0 || h <= 0) return;

        if(activeBuffer == null || activeBuffer.getWidth() != w || activeBuffer.getHeight() != h){
            activeBuffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        }
    }

    private void clearActiveBuffer(){
        if(activeBuffer == null) return;

        Graphics2D g2d = activeBuffer.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, activeBuffer.getWidth(), activeBuffer.getHeight());
        g2d.dispose();
    }

    private void repaintDirtyLine(Point a, Point b){
        int x1 = Math.max(0, Math.min(a.x, b.x) - DIRTY_PAD);
        int y1 = Math.max(0, Math.min(a.y, b.y) - DIRTY_PAD);
        int x2 = Math.min(getWidth(), Math.max(a.x, b.x) + DIRTY_PAD);
        int y2 = Math.min(getHeight(), Math.max(a.y, b.y) + DIRTY_PAD);
    
        repaint(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
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

        if(activeBuffer != null){
            g.drawImage(activeBuffer, 0, 0, null);
        }

        //Desenha apenas as interações em tempo real (active stroke).
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (selectionRect != null) {
            g2d.setColor(SELECTION_COLOR);
            g2d.setStroke(SELECTION_STROKE);
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

    /* Chama apenas quando o histórico real sofrer alterações. */
    public void invalidateBuffer(){
        needsBufferUpdate = true;
        super.repaint();
    }

    private void updateBuffer(){
        int w = getWidth();
        int h = getHeight();

        if(w <= 0 || h <= 0) return;

        //Só realoca se a janela for redimensionada.
        if(canvasBuffer == null || canvasBuffer.getWidth() != w || canvasBuffer.getHeight() != h){
            canvasBuffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2d = canvasBuffer.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);
        configureStrokeGraphics(g2d);

        for(Stroke stroke : model.getStrokes()) drawStroke(g2d, stroke);
        
        g2d.dispose();
        needsBufferUpdate = false;
    }

    public void setSelectionRect(Rectangle rect) {
        this.selectionRect = rect;
    }
}