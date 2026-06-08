package src.com.cerd.app.core.model;

import java.util.*;
import java.awt.Point;
import java.awt.Rectangle;

public class Stroke {
    private final List<Point> points = new ArrayList<>();
    
    private Rectangle cachedBounds = null;
    private boolean isDirty = true;

    public void addPoint(Point point){
        points.add(point);
        isDirty = true;
    
    }

    public List<Point> getPoints(){
        return Collections.unmodifiableList(points);
    }

    public boolean hasRenderablePoints(){
        return points.size() >= 2;
    }

    public Rectangle getBounds(){
        if(!isDirty && cachedBounds != null) return cachedBounds;

        if(points.isEmpty()){
            cachedBounds = new Rectangle(0,0,0,0);
            isDirty = false;
            return cachedBounds;
        }

        int minX = points.get(0).x;
        int minY = points.get(0).y;
        int maxX = minX;
        int maxY = minY;

        for(Point p : points){
            if(p.x < minX) minX = p.x;
            if(p.y < minY) minY = p.y;
            if(p.x > maxX) maxX = p.x;        
            if(p.y > maxY) maxY = p.y;        
        }

        cachedBounds = new Rectangle(minX, minY, maxX - minX, maxY - minY);
        isDirty = false;
        return cachedBounds;
    }

    public void translate(int dx, int dy){
        for(Point p : points){
            p.translate(dx, dy);
        }
        isDirty = true;
    }
}
