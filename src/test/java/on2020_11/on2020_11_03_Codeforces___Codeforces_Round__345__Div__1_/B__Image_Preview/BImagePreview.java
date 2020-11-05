package on2020_11.on2020_11_03_Codeforces___Codeforces_Round__345__Div__1_.B__Image_Preview;



import template.io.FastInput;
import template.io.FastOutput;

public class BImagePreview {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        int t = in.readInt();
        int[] s = new int[n * 2];
        for (int i = 0; i < n; i++) {
            s[i] = s[i + n] = in.readChar() == 'w' ? b + 1 : 1;
        }

        t -= s[n];
        if (t < 0) {
            out.println(0);
            return;
        }

        int[] left = new int[n * 2];
        int[] right = new int[n * 2];

        for (int i = n - 1; i >= 1; i--) {
            left[i] = left[i + 1] + a + s[i];
        }
        for (int i = n + 1; i < 2 * n; i++) {
            right[i] = right[i - 1] + a + s[i];
        }
        int ans = 1;
        for (int i = 1; i < n; i++) {
            if (left[i] <= t) {
                ans = Math.max(ans, n - i + 1);
            }
        }
        for (int i = n + 1; i < 2 * n; i++) {
            if (right[i] <= t) {
                ans = Math.max(ans, i - n + 1);
            }
        }
        int r = n;
        int l = 1;
        while (left[l] > t) {
            l++;
        }
        for (int i = n + 1; i < 2 * n; i++) {
            r = i;
            while (r - l + 1 > n) {
                l++;
            }
            while (l <= n && left[l] + right[r] + Math.min(n - l, r - n) * a > t) {
                l++;
            }
            if (l > n) {
                break;
            }
            ans = Math.max(ans, r - l + 1);
        }

        out.println(ans);
    }
}
