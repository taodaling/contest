package template.math;

public class SumOfModMul {
    long sum;
    int mod;
    long limit;
    long sub;

    public SumOfModMul(int mod) {
        this.mod = mod;
        limit = Long.MAX_VALUE - (long) (mod - 1) * (mod - 1);
        sub = limit - limit % mod;
        assert sub <= limit;
    }

    public void clear() {
        sum = 0;
    }

    public void add(long a, long b) {
        add(a * b);
    }

    public void add(long x) {
        sum += x;
        if (sum >= limit) {
            sum -= sub;
        }
    }

    public void add(int a, int b) {
        add((long)a * b);
    }

    public int sum() {
        if (sum >= mod) {
            sum %= mod;
        }
        return (int) sum;
    }

    @Override
    public String toString() {
        return "" + sum;
    }
}
