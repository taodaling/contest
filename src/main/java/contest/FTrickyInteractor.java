package contest;


import template.io.FastInput;
import template.io.FastOutput;

public class FTrickyInteractor {
    FastInput in;
    FastOutput out;
    int n;
    int t;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;

        n = in.readInt();
        t = in.readInt();

        int[] ans = new int[n + 1];
        int preSum = 0;
        for (int i = 1; i <= n; i++) {
            int total = (i + t - querySuffix(i)) / 2;
            if (total > preSum) {
                ans[i] = 1;
                preSum++;
            }
        }

        out.append("! ");
        for (int i = 1; i <= n; i++) {
            out.append(ans[i]);
        }
        out.println();
        out.flush();
    }

    public int querySuffix(int p, int t) {
        int s = p;
        boolean prevOdd = p % 2 == 1;
        boolean sufOdd = !prevOdd;
        if (sufOdd != ((n - p + 1) % 2 == 1)) {
            s++;
        }
        boolean lFlip = false;
        boolean rFlip = false;
        int ans = t;
        while (!(lFlip && !rFlip)) {
            int val = query(, p);
            if ((Math.abs(ans - val) % 2 == 1) == prevOdd) {
                lFlip = !lFlip;
            } else {
                rFlip = !rFlip;
            }
            ans = val;
        }
        int ans =
    }

    public int query(int l, int r) {
        out.append("? ").append(l).append(' ').append(r).println();
        out.flush();
        return in.readInt();
    }
}
