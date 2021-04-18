package on2021_03.on2021_03_28_AtCoder___AtCoder_Regular_Contest_116.F___Deque_Game;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.Int2ToIntegerFunction;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.CloneSupportObject;

import java.util.*;

public class FDequeGame {


    public int apply(int[] res, int l, int r, Int2ToIntegerFunction first, Int2ToIntegerFunction last) {
        int len = r - l + 1;
        if (len == 1) {
            return res[l];
        }
        int half = (l + r) / 2;
        return first.apply(last.apply(res[half - 1], res[half]), last.apply(res[half], res[half + 1]));
    }

    public int apply(int[] res, Int2ToIntegerFunction first, Int2ToIntegerFunction second, Int2ToIntegerFunction last) {
        return first.apply(apply(res, 1, res.length - 1, second, last), apply(res, 0, res.length - 2, second, last));
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        int[][] seqs = new int[k][];
        int N = 0;
        List<int[]> even = new ArrayList<>(k);
        List<int[]> odd = new ArrayList<>(k);

        long ans = 0;
        for (int i = 0; i < k; i++) {
            int n = in.ri();
            seqs[i] = in.ri(n);
            N += n;
            if (n % 2 == 0) {
                even.add(seqs[i]);
            } else {
                if (n == 1) {
                    ans += seqs[i][0];
                } else {
                    odd.add(seqs[i]);
                }
            }
        }
        Int2ToIntegerFunction first = null;
        Int2ToIntegerFunction last = null;
        if ((N - k) % 2 == 0) {
            last = Math::min;
            first = Math::max;
        } else {
            last = Math::max;
            first = Math::min;
        }

        IntegerArrayList profit = new IntegerArrayList(k);
        for (int[] x : odd) {
            ans += apply(x, 0, x.length - 1, first, last);
        }
        for (int[] x : even) {
            int best = apply(x, Math::max, first, last);
            int worst = apply(x, Math::min, first, last);
            profit.add(best - worst);
            ans += worst;
        }


        profit.sort();
        int[] dq = profit.toArray();
        for (int i = dq.length - 2; i >= 0; i -= 2) {
            dq[i] = 0;
        }
        for (int x : dq) {
            ans += x;
        }
        out.println(ans);
//        assert sum == check();
    }

}