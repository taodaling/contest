package template.graph;

public class UndirectedEdge extends DirectedEdge {
    public UndirectedEdge rev;

    public UndirectedEdge(int to) {
        super(to);
    }
}
