package src.com.cerd.app.core.model;

import java.util.*;
import java.awt.Point;
import java.awt.Rectangle;

public class Stroke {
    private final List<Point> points = new ArrayList<>();
    private final List<Point> readOnlyPoints = Collections.unmodifiableList(points);
    
    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;

    public void addPoint(Point point){
        Objects.requireNonNull(point, "point");

        Point copy = new Point(point);

        points.add(copy);
        includeBounds(copy.x, copy.y);
    }

    private void includeBounds(int x, int y){
        if(x < minX) minX = x;
        if(y < minY) minY = y;
        if(x > maxX) maxX = x;        
        if(y > maxY) maxY = y;
    }

    public List<Point> getPoints(){
        return readOnlyPoints;
    }

    public boolean hasRenderablePoints(){
        return points.size() >= 2;
    }

    public Rectangle getBounds(){
        if(points.isEmpty()){
            return new Rectangle(0,0,0,0);
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public boolean intersects(Rectangle rect){
        if(rect == null || points.isEmpty()) return false;

        int rectMaxX = rect.x + rect.width;
        int rectMaxY = rect.y + rect.height;

        return rect.x <= maxX
            && rectMaxX >= minX
            && rect.y <= maxY
            && rectMaxY >= minY;
    }

    public void translate(int dx, int dy){
        if(points.isEmpty()) return;

        for(Point p : points){
            p.translate(dx, dy);     
        }

        minX += dx;
        maxX += dx;
        minY += dy;
        maxY += dy;
    }
}
