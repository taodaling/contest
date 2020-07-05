package template.rand;

public class Hasher {
    private long time = System.nanoTime() + System.currentTimeMillis() * 31L;

    public int shuffle(long x) {
        // http://xorshift.di.unimi.it/splitmix64.c
        x += time;
        x += 0x9e3779b97f4a7c15L;
        x = (x ^ (x >>> 30)) * 0xbf58476d1ce4e5b9L;
        x = (x ^ (x >>> 27)) * 0x94d049bb133111ebL;
        return (int) (x ^ (x >>> 31));
    }

    public int hash(int x) {
        return shuffle(x);
    }

    public int hash(long x) {
        return shuffle(x);
    }

    public int hash(double x) {
        return shuffle(Double.doubleToRawLongBits(x));
    }

    public int hash(Object x) {
        return shuffle(x == null ? 0 : x.hashCode());
    }

}
