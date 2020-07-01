package on2020_07.on2020_07_01_Codeforces___Codeforces_Round__402__Div__1_.A__String_Game;



import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

public class AStringGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int limit = (int) 2e5;
        char[] p = new char[limit];
        char[] t = new char[limit];
        int n = in.readString(t, 0);
        int m = in.readString(p, 0);
        int[] timestamps = new int[n];
        int[] removed = new int[n];
        for (int i = 0; i < n; i++) {
            removed[i] = in.readInt() - 1;
            timestamps[removed[i]] = i;
        }

        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return !AStringGame.this.check(p, t, n, m, timestamps, mid);
            }
        };

        int ans = ibs.binarySearch(0, n - 1);
        out.println(ans);
    }

    public boolean check(char[] p, char[] t, int n, int m, int[] timestamps, int round) {
        int match = 0;
        for (int i = 0; i < n && match < m; i++) {
            if (timestamps[i] <= round) {
                continue;
            }
            if (t[i] == p[match]) {
                match++;
            }
        }
        return match == m;
    }

}
