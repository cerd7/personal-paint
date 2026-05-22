package src.core.model;

import java.util.*;
import java.awt.Point;

public class Stroke {
    private final List<Point> points = new ArrayList<>();
    
    public void addPoint(Point point){
        points.add(point);
    }

    public List<Point> getPoints(){
        return Collections.unmodifiableList(points);
    }

    public boolean hasRenderablePoints(){
        return points.size() >= 2;
    }
}
