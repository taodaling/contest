package on2021_05.on2021_05_05_Codeforces___Codeforces_Global_Round_14.D__Phoenix_and_Socks;



import template.io.FastInput;
import template.io.FastOutput;

public class DPhoenixAndSocks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int l = in.ri();
        int r = in.ri();
        int left = 0;
        int right = 0;
        int[] color = new int[n];
        for (int i = 0; i < l; i++) {
            color[in.ri() - 1]++;
        }
        for (int i = 0; i < r; i++) {
            color[in.ri() - 1]--;
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (color[i] > 0) {
                if (l > r) {
                    int move = Math.min(color[i] / 2, (l - r) / 2);
                    ans += move;
                    r += move;
                    l -= move;
                    color[i] -= 2 * move;
                }
                left += color[i];
            } else {
                if (r > l) {
                    int move = Math.min(-color[i] / 2, (r - l) / 2);
                    ans += move;
                    r -= move;
                    l += move;
                    color[i] += 2 * move;
                }
                right -= color[i];
            }
        }

        ans += Math.max(left, right);
        out.println(ans);
    }
}
