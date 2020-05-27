package on2020_05.on2020_05_27_AtCoder___AtCoder_Grand_Contest_016.A___Shrinking;



import template.io.FastInput;
import template.io.FastOutput;

public class AShrinking {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();

        int n = s.length;

        int ans = n;
        for (int i = 'a'; i <= 'z'; i++) {
            int last = -1;
            int local = 0;
            for (int j = n - 1; j >= 0; j--) {
                if (s[j] == i) {
                    last = j;
                }
                if (last == -1) {
                    local = Math.max(local, n - j);
                } else {
                    int cost = Math.min(n - j, last - j);
                    local = Math.max(local, cost);
                }
            }
            ans = Math.min(ans, local);
        }

        out.println(ans);
    }
}
