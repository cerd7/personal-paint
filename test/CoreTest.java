package test;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.Point;
import java.util.List;
import org.junit.jupiter.api.Test;

import src.core.command.PasteCommand;
import src.core.model.ClipboardBuffer;
import src.core.model.DrawModel;
import src.core.model.Stroke;

class CoreTest {
    @Test
    void strokeBounds(){
        Stroke s = new Stroke();
        s.addPoint(new Point(10, 20));
        s.addPoint(new Point(30, 40));
        assertEquals(10, s.getBounds().x);
        assertEquals(20, s.getBounds().y);
        assertEquals(20, s.getBounds().width);
        assertEquals(20, s.getBounds().height);
    }

    @Test
    void strokeTranslate(){
        Stroke s = new Stroke();
        s.addPoint(new Point(1,1));
        s.translate(2, 3);
        Point p = s.getPoints().get(0);
        assertEquals(3, p.x);
        assertEquals(4, p.y);
    }

    @Test
    void clipboardClones(){
        Stroke s = new Stroke();
        s.addPoint(new Point(5,5));
        ClipboardBuffer cb = new ClipboardBuffer();
        cb.set(List.of(s));
        List<Stroke> clones = cb.getClones();
        assertNotSame(s, clones.get(0));
    }

    @Test
    void pasteCommandUndo(){
        DrawModel model = new DrawModel();
        Stroke s1 = new Stroke();
        s1.addPoint(new Point(1,1));
        PasteCommand cmd = new PasteCommand(model, List.of(s1));
        cmd.execute();
        assertEquals(1, model.getStrokes().size());
        cmd.undo();
        assertEquals(0, model.getStrokes().size());
    }
}
