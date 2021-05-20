package template.math;

public class SumOfModMul {
    long sum;
    int mod;
    long limit;
    long sub;
    Barrett barrett;

    public SumOfModMul(int mod) {
        this.mod = mod;
        barrett = new Barrett(mod);
        limit = Long.MAX_VALUE - (long) (mod - 1) * (mod - 1);
        sub = limit - limit % mod;
    }

    public void clear() {
        sum = 0;
    }

    public void add(long a, long b) {
        sum += a * b;
        if (sum >= limit) {
            sum -= sub;
        }
    }

    public void add(long x) {
        sum += x;
        if (sum >= limit) {
            sum -= sub;
        }
    }

    public void add(int a, int b) {
        sum += (long) a * b;
        if (sum >= limit) {
            sum -= sub;
        }
    }

    public int sum() {
        if (sum >= mod) {
            sum = barrett.reduce(sum);
        }
        return (int) sum;
    }

    @Override
    public String toString() {
        return "" + sum;
    }
}
