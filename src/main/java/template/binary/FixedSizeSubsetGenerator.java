package template.binary;

/**
 * Gosper's Hack
 *
 * 找到[0,m]中所有大小为k的元素，并升序输出
 */
public class FixedSizeSubsetGenerator {
    int m;
    int cur;

    public void init(int m, int k) {
        this.m = m;
        this.cur = (1 << k) - 1;
    }

    public boolean hasMore() {
        return cur <= m;
    }

    /**
     * O(1) seem fast enough
     * @return
     */
    public int next() {
        int ans = cur;
        int lb = cur & -cur;
        int high = cur + lb;
        cur = (((cur ^ high) >>> 2) / lb) | high;
        return ans;
    }
}
