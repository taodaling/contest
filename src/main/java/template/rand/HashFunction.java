package template.rand;

public interface HashFunction {
    long f(long x);

    HashFunction upgrade();
}
