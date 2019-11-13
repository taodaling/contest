package on2019_11.on2019_11_13_Educational_Codeforces_Round_76__Rated_for_Div__2_.C___Dominated_Subarray;



import template.FastInput;
import template.FastOutput;

import java.util.Arrays;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = in.readInt();
        }
        int[] registries = new int[n + 1];
        Arrays.fill(registries, -1);
        int minDiff = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (registries[data[i]] != -1) {
                minDiff = Math.min(minDiff, i - registries[data[i]] + 1);
            }
            registries[data[i]] = i;
        }

        out.println(minDiff == Integer.MAX_VALUE ? -1 : minDiff);
    }
}

