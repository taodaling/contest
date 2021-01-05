package template.problem;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.primitve.generated.datastructure.LongArrayList;

/**
 * O(n^1.5) space complexity, init O(n^1.5) time complexity and each query will be handled in O(n^0.5)
 */
public class IntervalInversePairProblem {
    private int[] a;
    private int n;
    private int blockSize;
    private int BIT;
    private int mask;
    private int blockCnt;
    private long[][] blockPairs;
    private long[] prefix;
    private int[][] prefixSorted;
    private int[][] prefixOccur;
    private int m;
    private int[] zero;
    private long[] suffix;

    private int left(int i) {
        return i << BIT;
    }

    private int right(int i) {
        return Math.min(left(i + 1), n) - 1;
    }

    private int getBlock(int i) {
        return i >> BIT;
    }

    public IntervalInversePairProblem(IntToLongFunction func, int n) {
        this.n = n;
        LongArrayList list = new LongArrayList(n);
        for (int i = 0; i < n; i++) {
            list.add(func.apply(i));
        }
        list.unique();
        m = list.size();
        zero = new int[m];
        a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = list.binarySearch(func.apply(i));
        }
        prepare();
    }

    private int getPrefixOccur(int i, int j) {
        if (i < 0 || j < 0) {
            return 0;
        }
        j = Math.min(j, m - 1);
        return prefixOccur[i][j];
    }

    private int getInterval(int i, int l, int r) {
        r = Math.min(r, m - 1);
        l = Math.max(l, 0);
        if (i < 0 || l > r) {
            return 0;
        }
        int ans = prefixOccur[i][r];
        if (l > 0) {
            ans -= prefixOccur[i][l - 1];
        }
        return ans;
    }

    private int geq(int l, int r, int x) {
        return getInterval(r, x, m - 1) - getInterval(l - 1, x, m - 1);
    }

    private int leq(int l, int r, int x) {
        return getPrefixOccur(r, x) - getPrefixOccur(l - 1, x);
    }

    private void prepare() {
        BIT = 0;
        while ((1 << BIT * 2) < n) {
            BIT++;
        }
        blockSize = 1 << BIT;
        mask = blockSize - 1;
        blockCnt = (n - 1) / blockSize + 1;
        prefix = new long[n];
        prefixSorted = new int[n][];
        for (int i = 0; i < blockCnt; i++) {
            int l = left(i);
            int r = right(i);
            for (int j = l; j <= r; j++) {
                prefixSorted[j] = new int[j - l + 1];
                if (j == l) {
                    prefixSorted[j][0] = a[j];
                } else {
                    System.arraycopy(prefixSorted[j - 1], 0, prefixSorted[j], 0, j - l);
                    int tail = j - l;
                    while (tail - 1 >= 0 && prefixSorted[j][tail - 1] > a[j]) {
                        prefixSorted[j][tail] = prefixSorted[j][tail - 1];
                        tail--;
                    }
                    prefixSorted[j][tail] = a[j];
                    prefix[j] = prefix[j - 1] + (j - l) - tail;
                }
            }
        }
        int[] buf = new int[blockSize];
        suffix = new long[n];
        for (int i = 0; i < blockCnt; i++) {
            int l = left(i);
            int r = right(i);
            for (int j = r; j >= l; j--) {
                if (j == r) {
                    buf[0] = a[j];
                } else {
                    int tail = r - j;
                    while (tail - 1 >= 0 && buf[tail - 1] >= a[j]) {
                        buf[tail] = buf[tail - 1];
                        tail--;
                    }
                    buf[tail] = a[j];
                    suffix[j] = tail + suffix[j + 1];
                }
            }
        }
        prefixOccur = new int[blockCnt][m];
        for (int i = 0; i < blockCnt; i++) {
            int l = left(i);
            int r = right(i);
            for (int j = l; j <= r; j++) {
                prefixOccur[i][a[j]]++;
            }
        }
        for (int i = 0; i < blockCnt; i++) {
            for (int j = 1; j < m; j++) {
                prefixOccur[i][j] += prefixOccur[i][j - 1];
            }
            if (i > 0) {
                for (int j = 0; j < m; j++) {
                    prefixOccur[i][j] += prefixOccur[i - 1][j];
                }
            }
        }
        blockPairs = new long[blockCnt][blockCnt];
        for (int i = 0; i < blockCnt; i++) {
            for (int j = i; j < blockCnt; j++) {
                if (i == j) {
                    blockPairs[i][j] = prefix[right(i)];
                } else {
                    blockPairs[i][j] = blockPairs[i][j - 1] + prefix[right(j)];
                    int l = left(j);
                    int r = right(j);

                    int[] subOccur = i == 0 ? zero : prefixOccur[i - 1];
                    int[] plusOccur = prefixOccur[j - 1];
                    for (int k = l; k <= r; k++) {
                        // blockPairs[i][j] += geq(i, j - 1, a[k] + 1);
                        blockPairs[i][j] += plusOccur[m - 1] -
                                plusOccur[a[k]] - subOccur[m - 1] +
                                subOccur[a[k]];
                    }
                }
            }
        }
    }


    private long query0(int l, int r) {
        assert l % blockSize == 0;
        int rId = getBlock(r);
        int lId = getBlock(l);
        if (lId == rId) {
            return prefix[r];
        }
        long ans = blockPairs[lId][rId - 1] + prefix[r];
        int from = left(rId);
        int to = r;

        int[] subOccur = lId == 0 ? zero : prefixOccur[lId - 1];
        int[] plusOccur = prefixOccur[rId - 1];
        for (int i = from; i <= to; i++) {
            ans += plusOccur[m - 1] - plusOccur[a[i]]
                    - subOccur[m - 1] + subOccur[a[i]];
            //  ans += geq(lId, rId - 1, a[i] + 1);
        }
        return ans;
    }

    public long query1(int l, int r) {
        assert getBlock(l) == getBlock(r) && l > left(getBlock(l));
        int ll = l - 1;
        int i = 0;
        int j = 0;
        long ans = prefix[r] - prefix[ll];
        while (i < prefixSorted[ll].length) {
            if (prefixSorted[r][j] == prefixSorted[ll][i]) {
                ans -= j - i;
                i++;
                j++;
                continue;
            }
            j++;
        }
        return ans;
    }

    private long query2(int a, int b) {
        int i = 0;
        int j = 0;
        long ans = 0;
        while (i < prefixSorted[a].length) {
            if (j >= prefixSorted[b].length ||
                    prefixSorted[a][i] <= prefixSorted[b][j]) {
                ans += j;
                i++;
            } else {
                j++;
            }
        }
        return ans;
    }

    public long query(int l, int r) {
        int lId = getBlock(l);
        int rId = getBlock(r);
        if (left(lId) == l) {
            return query0(l, r);
        }
        if (lId == rId) {
            return query1(l, r);
        }
        int ll = left(lId);
        int lr = right(lId);
        long ans = query0(ll, r) - prefix[lr] + suffix[l];
        int[] subOccur = prefixOccur[lId];
        int[] plusOccur = prefixOccur[rId - 1];
        for (int i = ll; i < l; i++) {
            if (a[i] > 0) {
                ans -= plusOccur[a[i] - 1] - subOccur[a[i] - 1];
            }
            // ans -= leq(lId + 1, rId - 1, a[i] - 1);
        }
        ans -= query2(l - 1, r);
        return ans;
    }
}