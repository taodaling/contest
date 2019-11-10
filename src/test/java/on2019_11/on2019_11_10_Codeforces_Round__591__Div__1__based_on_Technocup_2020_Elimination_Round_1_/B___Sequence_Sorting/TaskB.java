package on2019_11.on2019_11_10_Codeforces_Round__591__Div__1__based_on_Technocup_2020_Elimination_Round_1_.B___Sequence_Sorting;



import template.CompareUtils;
import template.FastInput;
import template.FastOutput;

import java.util.Arrays;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];

        int[] lastPosition = new int[n + 1];
        Arrays.fill(lastPosition, -1);
        int[] firstPosition = new int[n + 1];
        Arrays.fill(firstPosition, -1);
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            lastPosition[a[i]] = i;
            if (firstPosition[a[i]] == -1) {
                firstPosition[a[i]] = i;
            }
        }


        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            if(lastPosition[i] == -1){
                continue;
            }
            int lastNumber = i - 1;
            while(lastNumber >= 1 && lastPosition[lastNumber] == -1){
                lastNumber--;
            }
            if (lastPosition[lastNumber] < firstPosition[i]) {
                dp[i] = dp[lastNumber] + 1;
            } else {
                dp[i] = 1;
            }
        }

        int ans = 0;
        for (int i = 1; i <= n; i++) {
            if (lastPosition[i] == -1) {
                continue;
            }
            ans++;
        }

        int mx = 0;
        for (int i = 0; i <= n; i++) {
            mx = Math.max(mx, dp[i]);
        }

        out.println(ans - mx);
    }
}
