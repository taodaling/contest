package template.rand;

public class DenseMultiSetHasher extends MultiSetHasherImpl {
    long[] cache;
    long l;

    public DenseMultiSetHasher(long l, long r) {
        this.l = l;
        cache = new long[(int) (r - l + 1)];
        for (int i = 0; i < cache.length; i++) {
            cache[i] = super.hash(i);
        }
    }

    @Override
    public long hash(long x) {
        return cache[(int) (x - l)];
    }
}
