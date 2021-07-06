package template.graph;

import template.primitve.generated.datastructure.IntToIntegerFunction;

public class ParentOnTreeByFunction implements ParentOnTree {
    int[] p;

    public ParentOnTreeByFunction(int n, IntToIntegerFunction pFunc) {
        p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = pFunc.apply(i);
        }
    }

    @Override
    public int parent(int node) {
        return p[node];
    }
}
