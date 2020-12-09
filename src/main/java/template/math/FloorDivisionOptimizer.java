package template.math;

/**
 * \sum_{i=l}^{r}f(\lfloor n/i \rfloor)
 */
public class FloorDivisionOptimizer {
    public int l;
    public int r;
    private int n;
    private int limit;


    public FloorDivisionOptimizer(int n, int l, int r) {
        this.n = n;
        this.l = l;
        this.limit = r;
        this.r = l - 1;
    }

    public boolean hasNext() {
        return r < limit;
    }

    public int next() {
        int v = n / l;
        l = r + 1;
        r = Math.min(n / v, limit);
        return v;
    }
}
