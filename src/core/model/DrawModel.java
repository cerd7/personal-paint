package src.core.model;

import java.util.*;

public class DrawModel {
    private final List<Stroke> strokes = new ArrayList<>();

    public void addStroke(Stroke stroke){
        strokes.add(stroke);
    }

    public Stroke removeLastStroke(){
        if(strokes.isEmpty()){
            return null;
        }
        return strokes.remove(strokes.size() - 1);
    }

    public void clear(){
        strokes.clear();
    }

    public List<Stroke> getStrokes(){
        return Collections.unmodifiableList(strokes);
    }

    public List<Stroke> snapshot(){
        return new ArrayList<>(strokes);
    }

    public void restore(List<Stroke> snapshot){
        strokes.clear();
        strokes.addAll(snapshot);
    }
}
