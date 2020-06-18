package template.problem;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDiscreteMap;

/**
 * 静态区间众数
 */
public class MaxOccurenceNumberInInterval {
    private int[] data;
    private IntegerDiscreteMap dm;
    private int[][] answers;
    private int[][] cnts;
    private int n;
    private int m;
    private int blockSize;
    private int blockCnt;
    private int[] sum;

    private int leftOfBlock(int i) {
        return i * blockSize;
    }

    private int rightOfBlock(int i) {
        return Math.min((i + 1) * blockSize - 1, n - 1);
    }

    private int occurence(int l, int r, int x) {
        if (l > r) {
            return 0;
        }
        int ans = cnts[r][x];
        if (l > 0) {
            ans -= cnts[l - 1][x];
        }
        return ans;
    }


    private int occurTimes;
    private int minNumber;

    private void reset() {
        occurTimes = 0;
        minNumber = Integer.MAX_VALUE;
    }

    private void update(int x, int t) {
        if (occurTimes < t) {
            occurTimes = t;
            minNumber = Integer.MAX_VALUE;
        }
        if (occurTimes == t) {
            minNumber = Math.min(minNumber, x);
        }
    }

    /**
     * O(n^1.5) 空间时间复杂度，其中n为data的长度
     */
    public MaxOccurenceNumberInInterval(int[] data) {
        this.data = data;
        dm = new IntegerDiscreteMap(data.clone(), 0, data.length);
        for (int i = 0; i < data.length; i++) {
            data[i] = dm.rankOf(data[i]);
        }


        n = data.length;
        m = dm.maxRank() + 1;
        sum = new int[m];
        blockSize = (int) Math.ceil(Math.sqrt(n));
        blockCnt = DigitUtils.ceilDiv(n, blockSize);
        cnts = new int[blockCnt][m];
        answers = new int[blockCnt][blockCnt];

        //calculate cnts
        for (int i = 0; i < blockCnt; i++) {
            int l = leftOfBlock(i);
            int r = rightOfBlock(i);
            for (int j = l; j <= r; j++) {
                cnts[i][data[j]]++;
            }
        }
        //presum for fast delta calculation
        for (int i = 1; i < blockCnt; i++) {
            for (int j = 0; j < m; j++) {
                cnts[i][j] += cnts[i - 1][j];
            }
        }

        //calculate answers
        for (int i = 0; i < blockCnt; i++) {
            for (int j = i; j < blockCnt; j++) {
                reset();
                int l = leftOfBlock(j);
                int r = rightOfBlock(j);
                //last block
                for (int k = l; k <= r; k++) {
                    update(data[k], occurence(i, j, data[k]));
                }
                //prev block
                if (i < j) {
                    int cand = answers[i][j - 1];
                    update(cand, occurence(i, j, cand));
                }
                answers[i][j] = minNumber;
            }
        }
    }


    /**
     * <p>获得区间众数，如果有多个返回最小的</p>
     * <p>时间复杂度是$O(sqrt{n})$，其中$n$是数组长度</p>
     */
    public int query(int l, int r) {
        if (l > r) {
            throw new IllegalArgumentException();
        }
        int lBlock = l / blockSize;
        int rBlock = r / blockSize;
        reset();
        for (int i = l, until = Math.min(rightOfBlock(lBlock), r); i <= until; i++) {
            sum[data[i]]++;
        }
        if (lBlock != rBlock) {
            for (int i = Math.max(leftOfBlock(rBlock), l); i <= r; i++) {
                sum[data[i]]++;
            }
        }
        for (int i = l, until = Math.min(rightOfBlock(lBlock), r); i <= until; i++) {
            update(data[i], sum[data[i]] + occurence(lBlock + 1, rBlock - 1, data[i]));
        }
        if (lBlock != rBlock) {
            for (int i = Math.max(leftOfBlock(rBlock), l); i <= r; i++) {
                update(data[i], sum[data[i]] + occurence(lBlock + 1, rBlock - 1, data[i]));
            }
        }
        if (rBlock - lBlock > 1) {
            int cand = answers[lBlock + 1][rBlock - 1];
            update(cand, occurence(lBlock + 1, rBlock - 1, cand));
        }

        //handle left block
        for (int i = l, until = Math.min(rightOfBlock(lBlock), r); i <= until; i++) {
            sum[data[i]] = 0;
        }
        if (lBlock != rBlock) {
            for (int i = Math.max(leftOfBlock(rBlock), l); i <= r; i++) {
                sum[data[i]] = 0;
            }
        }
        return dm.iThElement(minNumber);
    }
}
