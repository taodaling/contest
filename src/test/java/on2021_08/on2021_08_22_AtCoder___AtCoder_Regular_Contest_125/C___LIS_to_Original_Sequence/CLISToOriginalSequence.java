package on2021_08.on2021_08_22_AtCoder___AtCoder_Regular_Contest_125.C___LIS_to_Original_Sequence;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class CLISToOriginalSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int k = in.ri();
        int[] a = in.ri(k);
        occur = new boolean[n];
        for (int i = 0; i < k; i++) {
            a[i]--;
            occur[a[i]] = true;
        }
        backIter = n - 1;
        IntegerArrayList seq = new IntegerArrayList(n);
        int[] dp = new int[k + 1];
        dp[0] = -1;
        for (int i = 0; i < k; i++) {
            if (i == k - 1) {
                int p;
                while ((p = prev()) > a[i]) {
                    seq.add(p);
                    backIter--;
                }
            }
            seq.add(a[i]);
            dp[i + 1] = a[i];
            if (i + 1 < k) {
                int next = next();
                if (next < a[i]) {
                    seq.add(next);
                    dp[i + 1] = next;
                    iter++;
                }
            }
        }
        while (seq.size() < n) {
            int next = next();
            iter++;
            int pos = BinarySearch.lowerBound(dp, 0, k, next);
            dp[pos] = next;
            if (pos == k) {
                //
                int p;
                while ((p = prev()) > next) {
                    seq.add(p);
                    backIter--;
                }
            }
            seq.add(next);
        }

        for(int x : seq.toArray()){
            out.append(x + 1).append(' ');
        }
    }

    int n;
    int iter = 0;
    int backIter;
    boolean[] occur;

    public int next() {
        while (iter <= backIter && occur[iter]) {
            iter++;
        }
        return iter > backIter ? n : iter;
    }

    public int prev() {
        while (backIter >= iter && occur[backIter]) {
            backIter--;
        }
        return iter > backIter ? -1 : backIter;
    }

}
