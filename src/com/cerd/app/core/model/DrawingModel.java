package src.com.cerd.app.core.model;

import java.util.List;

public interface DrawingModel {
    void addStroke(Stroke stroke);
    Stroke removeLastStroke();
    void clear();
    List<Stroke> getStrokes();
    List<Stroke> snapshot();
    void restore(List<Stroke> snapshot);
    void setSelection(List<Stroke> selection);
    void clearSelection();
    List<Stroke> getSelection();
}
