package contest;

import template.ArrayUtils;
import template.FastInput;
import template.FastOutput;
import template.PermutationUtils;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] xs = new int[n];
        int[] dx = new int[n];
        int[] swap = new int[n];
        for(int i = 0; i < n; i++){
            xs[i] = in.readInt();
            if(i > 0) {
                dx[i] = xs[i] - xs[i - 1];
            }else{
                dx[0] = xs[0];
            }
            swap[i] = i;
        }

        int m = in.readInt();
        long k = in.readLong();
        for(int i = 0; i < m; i++){
            int j = in.readInt() - 1;
            ArrayUtils.swap(swap, j, j + 1);
        }

        PermutationUtils.PowerPermutation perm = new PermutationUtils.PowerPermutation(swap);

        int[] results = new int[n + 1];
        results[0] = dx[0];
        for(int i = 1; i < n; i++){
            results[i] = dx[perm.apply(i, k)];
        }

        long prefix = 0;
        for(int i = 0; i < n; i++){
            prefix += results[i];
            out.println(prefix);
        }
    }
}
