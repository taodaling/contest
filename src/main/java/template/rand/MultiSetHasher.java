package template.rand;

public interface MultiSetHasher {
    long hash(long x);

    long merge(long a, long b);

    long remove(long a, long b);

    long mul(long set, long time);
}
