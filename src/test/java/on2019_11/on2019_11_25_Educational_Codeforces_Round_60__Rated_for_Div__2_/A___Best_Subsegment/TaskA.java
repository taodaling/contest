package on2019_11.on2019_11_25_Educational_Codeforces_Round_60__Rated_for_Div__2_.A___Best_Subsegment;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int mx = CompareUtils.maxOf(a, 0, n - 1);
        int ans = 0;
        int length = 0;
        for (int i = 0; i < n; i++) {
            if (a[i] == mx) {
                length++;
            } else {
                length = 0;
            }
            ans = Math.max(ans, length);
        }
        out.println(ans);
    }
}
