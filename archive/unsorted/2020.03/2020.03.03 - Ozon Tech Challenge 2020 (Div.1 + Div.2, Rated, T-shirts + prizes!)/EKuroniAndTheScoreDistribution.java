package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class EKuroniAndTheScoreDistribution {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        if (n <= 2) {
            if(m != 0){
                out.println(-1);
                return;
            }
            for(int i = 0; i < n; i++){
                out.append(i + 1).append(' ');
            }
            return;
        }
        int step = 10000;
        int[] seq = new int[n];
        seq[0] = step;
        seq[1] = seq[0] + seq[0];
        int inf = (int) 1e9;
        for (int i = 2; i < n; i++) {
            if (m == 0) {
                seq[i] = inf - (n - i);
                continue;
            }
            int j = i - 2;
            while (j > 0 && m >= count(j - 1, i - 1)) {
                j--;
            }
            m -= count(j, i - 1);
            seq[i] = seq[i - 1] + seq[j];
        }

        if(m > 0){
            out.println(-1);
            return;
        }
        for(int i = 0; i < n; i++){
            out.append(seq[i]).append(' ');
        }
    }

    public int count(int l, int r) {
        return (r - l + 1) / 2;
    }
}
