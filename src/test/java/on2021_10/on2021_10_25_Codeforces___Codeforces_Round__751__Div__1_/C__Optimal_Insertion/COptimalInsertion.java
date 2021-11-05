package on2021_10.on2021_10_25_Codeforces___Codeforces_Round__751__Div__1_.C__Optimal_Insertion;



import template.algo.InversePair;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SortUtils;

import java.util.Arrays;

public class COptimalInsertion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        a = in.ri(n);
        b = in.ri(m);
        assign = new int[m];
        SortUtils.radixSort(b, 0, m - 1);
        cnt = new int[n + 1];
        dac(0, n, 0, m - 1);
        IntegerArrayList list = new IntegerArrayList(n + m);
        int iter = 0;
        for (int i = 0; i < m; i++) {
            while (iter < assign[i]) {
                list.add(a[iter++]);
            }
            list.add(b[i]);
        }
        while(iter < n){
            list.add(a[iter++]);
        }
        debug.debug("list", list);
        long ans = InversePair.inversePairCount(list.toArray());
        out.println(ans);
    }

    Debug debug = new Debug(false);
    int[] a;
    int[] cnt;
    int[] b;
    int[] assign;

    public void dac(int l, int r, int L, int R) {
        if (L > R) {
            return;
        }
        int M = (L + R) / 2;
        Arrays.fill(cnt, l, r + 1, 0);
        int ps = 0;
        for (int i = l; i < r; i++) {
            if (a[i] > b[M]) {
                ps++;
            }
            cnt[i + 1] += ps;
        }
        ps = 0;
        for (int i = r - 1; i >= l; i--) {
            if (a[i] < b[M]) {
                ps++;
            }
            cnt[i] += ps;
        }
        int bestChoice = l;
        for (int i = l; i <= r; i++) {
            if (cnt[i] < cnt[bestChoice]) {
                bestChoice = i;
            }
        }
        assign[M] = bestChoice;
        dac(l, bestChoice, L, M - 1);
        dac(bestChoice, r, M + 1, R);
    }
}
