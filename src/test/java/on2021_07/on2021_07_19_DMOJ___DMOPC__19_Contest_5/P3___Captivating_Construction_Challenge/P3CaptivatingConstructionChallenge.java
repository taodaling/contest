package on2021_07.on2021_07_19_DMOJ___DMOPC__19_Contest_5.P3___Captivating_Construction_Challenge;



import template.io.FastInput;
import template.io.FastOutput;

public class P3CaptivatingConstructionChallenge {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.ri();
        int w = in.ri();
        long ans1 = (long) h * (h - 1) / 2 * w * (w - 1) / 2;
        long ans2 = 0;

        int max = Math.max(h, w);
        int L = max * max;
        long[] row = new long[L + 1];
        long[] col = new long[L + 1];

        for (int i = 1; i <= L; i++) {
            int ij;
            for (int j = 1; (ij = j * i) <= L; j++) {
                int delta;
                if ((delta = h - i - j) > 0) {
                    row[ij] += delta;
                }
                if ((delta = w - i - j) > 0) {
                    col[ij] += delta;
                }
            }
        }
        for (int i = 1; i <= L; i++) {
            ans2 += row[i] * col[i];
        }
        long ans = ans1 + ans2;
        out.println(ans);
    }
}
