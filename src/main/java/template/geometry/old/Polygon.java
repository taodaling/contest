package template.geometry.old;

import java.util.List;

public class Polygon<T> {
    protected List<T> data;

    protected Polygon(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }
}
