package src.com.cerd.app.core.selection;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import src.com.cerd.app.core.model.Stroke;

public class BoundsSelectionPolicy implements SelectionPolicy {
    @Override
    public List<Stroke> select(List<Stroke> strokes, Rectangle selectionRect) {
        List<Stroke> selected = new ArrayList<>();

        if (selectionRect == null) {
            return selected;
        }

        for (Stroke stroke : strokes) {
            if (stroke.intersects(selectionRect)) {
                selected.add(stroke);
            }
        }

        return selected;
    }
}
