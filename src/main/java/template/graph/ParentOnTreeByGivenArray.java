package template.graph;

public class ParentOnTreeByGivenArray implements ParentOnTree{
    int[] p;

    public ParentOnTreeByGivenArray(int[] p) {
        this.p = p;
    }

    @Override
    public int parent(int node) {
        return p[node];
    }
}
