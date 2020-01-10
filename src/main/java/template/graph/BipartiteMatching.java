package template.graph;

public class BipartiteMatching {
    LongDinicBeta dinic;
    int l;
    int r;

    private int left(int i) {
        return i;
    }

    private int right(int i) {
        return l + i;
    }

    private int src() {
        return l + r;
    }

    private int sink() {
        return src() + 1;
    }

    public BipartiteMatching(int l, int r, int edgeNum) {
        this.l = l;
        this.r = r;
        dinic = new LongDinicBeta(sink() + 1, edgeNum + l + r, src(), sink());
        for (int i = 0; i < l; i++) {
            dinic.addChannel(src(), left(i)).expand(1);
        }
        for (int i = 0; i < r; i++) {
            dinic.addChannel(right(i), sink()).expand(1);
        }
    }

    public void addEdge(int lId, int rId){}
}
