package on2020_05.on2020_05_20_Codeforces___Codeforces_Round__419__Div__1_.D__Karen_and_Cards0;



import template.geometry.geo2.IntegerRect2;
import template.geometry.geo3.IntegerRect3;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class DKarenAndCards {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] limits = new int[3];
        for (int i = 0; i < 3; i++) {
            limits[i] = in.readInt();
        }
        int[][] cards = new int[n + 1][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                cards[i][j] = in.readInt();
            }
        }
        cards[n][2] = limits[2];
        n++;

        Arrays.sort(cards, (a, b) -> Integer.compare(a[2], b[2]));
        int[][] prefix = new int[n][2];
        int[][] suffix = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                prefix[i][j] = cards[i][j];
                if (i > 0) {
                    prefix[i][j] = Math.max(prefix[i][j], prefix[i - 1][j]);
                }
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < 2; j++) {
                suffix[i][j] = cards[i][j];
                if (i < n - 1) {
                    suffix[i][j] = Math.max(suffix[i][j], suffix[i + 1][j]);
                }
            }
        }

        debug.debug("cards", cards);
        debug.debug("prefix", prefix);
        debug.debug("suffix", suffix);
        long ans = 0;
        IntegerRect2 empty = new IntegerRect2(0, 0, 0, 0);
        int last = 0;
        for (int i = 0; i < n; i++) {
            int r = i;
            while (r + 1 < n && cards[i][2] == cards[r + 1][2]) {
                r++;
            }
            int way = cards[i][2] - last;
            last = cards[i][2];
            IntegerRect2 left = i == 0 ? empty : new IntegerRect2(0, 0, prefix[i - 1][0], prefix[i - 1][1]);
            IntegerRect2 right = new IntegerRect2(suffix[i][0], suffix[i][1], limits[0], limits[1]);
            long contrib = way * (right.area() - IntegerRect2.intersect(left, right).area());
            ans += contrib;
            debug.debug("i", i);
            debug.debug("h", cards[i][2]);
            //debug.debug("cards[i]", cards[i]);
            debug.debug("contrib", contrib);
            i = r;
        }

        out.println(ans);
    }
}


