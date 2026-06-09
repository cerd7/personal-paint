package src.com.cerd.app.core.selection;

import java.awt.Rectangle;
import java.util.List;

import src.com.cerd.app.core.model.Stroke;

public interface SelectionPolicy {
    List<Stroke> select(List<Stroke> strokes, Rectangle selectionRect);
}
