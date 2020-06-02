package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BTheUnbearableLightnessOfWeights {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] cnts = new int[101];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            cnts[x]++;
            sum += x;
        }
        int type = 0;
        for(int i = 0; i <= 100; i++){
            if(cnts[i] > 0){
                type++;
            }
        }
        int[][] last = new int[n + 1][sum + 1];
        last[0][0] = 1;
        int[][] cur = new int[n + 1][sum + 1];
        for (int i = 1; i <= 100; i++) {
            if(cnts[i] == 0){
                continue;
            }
            SequenceUtils.deepFill(cur, 0);
            for (int j = 0; j <= cnts[i]; j++) {
                int val = i * j;
                for (int level = j; level <= n; level++) {
                    for (int t = val; t <= sum; t++) {
                        cur[level][t] += last[level - j][t - val];
                    }
                }
            }
            for (int level = 0; level <= n; level++) {
                for (int j = 0; j <= sum; j++) {
                    cur[level][j] = Math.min(cur[level][j], 2);
                }
            }
            int[][] tmp = last;
            last = cur;
            cur = tmp;
        }
        int ans = 1;
        for (int i = 1; i <= 100; i++) {
            for (int j = 1; j <= cnts[i]; j++) {
                if (last[j][j * i] == 1 || last[n - j][sum - j * i] == 1) {
                    if(j == cnts[i] && type == 2){
                        ans = Math.max(ans, n);
                    }
                    ans = Math.max(ans, j);
                }

            }
        }
        out.println(ans);
    }
}
