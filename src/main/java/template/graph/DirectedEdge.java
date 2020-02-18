package template.graph;

public class DirectedEdge {
    public int to;

    public DirectedEdge(int to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "->" + to;
    }
}
