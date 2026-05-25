package src.core.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class ClipboardBuffer {
    private final List<Stroke> copied = new ArrayList<>();

    public void set(List<Stroke> strokes){
        copied.clear();
        for(Stroke stroke : strokes){
            copied.add(cloneStroke(stroke));
        }
    }

    public boolean isEmpty(){
        return copied.isEmpty();
    }

    public List<Stroke> getClones(){
        List<Stroke> clones = new ArrayList<>();
        for(Stroke stroke : copied){
            clones.add(cloneStroke(stroke));
        }
        return clones;
    }

    private Stroke cloneStroke(Stroke original){
        Stroke copy = new Stroke();
        for(Point p : original.getPoints()){
            copy.addPoint(new Point(p.x, p.y));
        }
        return copy;
    }
}
