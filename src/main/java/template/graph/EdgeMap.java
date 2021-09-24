package template.graph;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.utils.LongBinaryMerger;

public class EdgeMap {
    LongHashMap map;
    LongBinaryMerger merger;
    private boolean directed;
    private long def;

    public EdgeMap(int e) {
        this(e, false, Long.MAX_VALUE, Math::min);
    }

    public EdgeMap(int e, boolean directed, long def, LongBinaryMerger merger) {
        this.def = def;
        this.directed = directed;
        map = new LongHashMap(e, false);
        this.merger = merger;
        //This line is used to ask CHelper to inline the merge function
        //There seem like some bug
        merger.merge(0, 0);
    }

    private long id(int a, int b) {
        if (a > b && !directed) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }

    public void add(int a, int b, long v) {
        if (a == b) {
            return;
        }
        long id = id(a, b);
        long cur = map.getOrDefault(id, Long.MAX_VALUE);
        if (cur != Long.MAX_VALUE) {
            v = merger.merge(cur, v);
        }
        map.put(id, v);
    }

    public boolean exist(int a, int b) {
        return map.containKey(id(a, b));
    }

    public long getOrDefault(int a, int b, long def) {
        return map.getOrDefault(id(a, b), def);
    }

    public long get(int a, int b) {
        return map.getOrDefault(id(a, b), def);
    }

    public static EdgeMap ofUnitUndirectedGraph() {
        return new EdgeMap(0, false, 1, (a, b) -> 1);
    }


    public static EdgeMap ofUnitDirectedGraph() {
        return new EdgeMap(0, true, 1, (a, b) -> 1);
    }
}
