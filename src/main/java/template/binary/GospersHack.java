package template.binary;

/**
 * Gosper's Hack
 * <p>
 * 找到[0,m]中所有大小为k的元素，并升序输出。
 * 注意不是子集，而是所有小于等于m的自然数
 */

/**
 * It's too slow caused by division, use {@link FixedSizeSubsetGenerator} instead which 1.2 times faster than this
 */
@Deprecated
public class GospersHack {
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
     *
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
