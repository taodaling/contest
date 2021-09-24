package on2021_07.on2021_07_20_CodeChef___June_Cook_Off_2021_Division_1.Binary_String_on_Steroids;



import template.io.FastInput;
import template.io.FastOutput;

public class BinaryStringOnSteroids {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s);
        int sum = 0;
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
            sum += s[i];
        }
        int inf = (int) 1e9;
        int best = inf;
        int[] ps = new int[n];
        for (int d = 1; d <= n; d++) {
            if (n % d != 0) {
                continue;
            }
            for (int j = n - 1; j >= 0; j--) {
                ps[j] = s[j];
                if (j + d < n) {
                    ps[j] += ps[j + d];
                }
            }
            int each = n / d;
            for (int first = 0; first < d; first++) {
                int contrib = each - ps[first];
                contrib += sum - ps[first];
                best = Math.min(best, contrib);
            }
        }
        out.println(best);
    }
}
