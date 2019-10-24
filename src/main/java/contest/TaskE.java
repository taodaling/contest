package contest;

import template.FastInput;
import template.FastOutput;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        int[] dists = new int[n];
        long[] presumOfDists = new long[n];
        for(int i = 1; i < n; i++){
            dists[i] = in.readInt();
            presumOfDists[i] = presumOfDists[i - 1] + dists[i];
        }

        int[] meal = new int[m];
        long sum = 0;
        for(int i = 0; i < m; i++){
            meal[i] = in.readInt();
            sum += meal[i];
        }

        long ans = sum;

        for(int i = 2; i <= n; i++){
            for(int j = 0; j < m; j++){
                int d = in.readInt();
                if(d > meal[j]){
                    sum -= meal[j];
                    meal[j] = d;
                    sum += d;
                }
            }
            ans = Math.max(ans, sum - presumOfDists[i - 1]);
        }

        out.println(ans);
    }
}