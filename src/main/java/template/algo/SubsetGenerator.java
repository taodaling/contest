package template.algo;

/**
 * Iterate all subset of m in descending order
 */
public class SubsetGenerator {
    private int m;
    private int x;

    public void reset(int m) {
        this.m = m;
        this.x = m + 1;
    }

    public boolean hasNext() {
        return x != 0;
    }

    public int next() {
        return x = (x - 1) & m;
    }
}
