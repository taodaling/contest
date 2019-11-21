package template.math;

/**
 * \sum_{i=1}^{limit}f(\lfloor n/i \rfloor)
 */
public class FloorDivisionOptimizer {
    int l;
    int r;
    int n;
    int limit;


    public FloorDivisionOptimizer(int n, int l, int limit) {
        this.n = n;
        this.l = 0;
        this.limit = limit;
        this.r = l - 1;
    }

    public boolean hasNext() {
        return r < limit;
    }

    public int next() {
        l = r + 1;
        r = n / (n / l);
        return n / l;
    }
}
