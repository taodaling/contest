package template.rand;

import template.primitve.generated.datastructure.LongHashMap;

public class SparseMultiSetHasher extends MultiSetHasherImpl {
    private LongHashMap map;

    public SparseMultiSetHasher(int cap) {
        map = new LongHashMap(cap, true);
    }

    @Override
    public long hash(long x) {
        long ans = map.getOrDefault(x, -1);
        if (ans == -1) {
            ans = super.hash(x);
            map.put(x, ans);
        }
        return map.get(x);
    }
}
