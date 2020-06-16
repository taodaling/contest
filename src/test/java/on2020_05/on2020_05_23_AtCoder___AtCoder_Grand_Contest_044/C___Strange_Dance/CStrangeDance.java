package on2020_05.on2020_05_23_AtCoder___AtCoder_Grand_Contest_044.C___Strange_Dance;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongRadix;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.ArrayIndex;

import java.util.BitSet;

public class CStrangeDance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] t = new char[(int) 2e5];
        len = in.readString(t, 0);
        basic = new BitSet(len);
        for (int i = 0; i < len; i++) {
            basic.set(i, t[i] == 'S');
        }
        int limit = (int) radix.set(0, n, 1);
        for (int i = 0; i < limit; i++) {
            dp(n - 1, i);
            int index = ai.indexOf(n - 1, i);
            int ans = cast[index];
            out.append(ans).append(' ');
        }
    }


    LongRadix radix = new LongRadix(3);
    ArrayIndex ai = new ArrayIndex(12, (int) radix.set(0, 12, 1));
    BitSet[] dp = new BitSet[ai.totalSize()];
    int[] cast = new int[ai.totalSize()];
    int[] sizes = new int[ai.totalSize()];
    BitSet basic;
    int len;

    IntegerList op = new IntegerList((int) 2e5);

    public int swap(int i) {
        if (i == 0) {
            return i;
        }
        return 3 - i;
    }

    public void dp(int i, int j) {
        int index = ai.indexOf(i, j);
        if (dp[index] == null) {
            BitSet bit = basic;
            int prefix = 0;
            int m = len;
            if (i > 0) {
                //use basic
                int next = (int) radix.set(j, i, 0);
                dp(i - 1, next);
                bit = dp[ai.indexOf(i - 1, next)];
                prefix = cast[ai.indexOf(i - 1, next)];
                m = sizes[ai.indexOf(i - 1, next)];
            }
            op.clear();
            int val = radix.get(j, i);
            for (int t = 0; t < m; t++) {
                if (bit.get(t)) {
                    //s
                    val = swap(val);
                    if (!op.isEmpty() && op.tail() == 1) {
                        op.pop();
                    } else {
                        op.add(1);
                    }
                } else {
                    //
                    val++;
                    if (val < 3) {
                        continue;
                    } else {
                        val = 0;
                        op.add(0);
                    }
                }
            }

            dp[index] = new BitSet(op.size());
            sizes[index] = op.size();
            int[] data = op.getData();
            for (int t = op.size() - 1; t >= 0; t--) {
                dp[index].set(t, data[t] == 1);
            }
            cast[index] = (int) radix.set(prefix, i, val);
        }
    }
}
