package src.core.model;

import java.util.*;
import java.awt.Point;
import java.awt.Rectangle;

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

    public Rectangle getBounds(){
        if(points.isEmpty()){
            return new Rectangle(0,0,0,0);
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

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public void translate(int dx, int dy){
        for(int i=0;i<points.size();i++){
            Point p = points.get(i);
            points.set(i, new Point(p.x + dx, p.y + dy));
        }
    }
}
