package template.graph;

public class UndirectedEdge extends DirectedEdge {
    public UndirectedEdge rev;

    public UndirectedEdge(int to) {
        super(to);
    }

    @Override
    public String toString() {
        return String.format("%d<->%d", rev.to, to);
    }
}
