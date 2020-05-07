package on2020_05.on2020_05_08_TopCoder_SRM__748.Rectoggle;



import template.math.Nimber;
import template.utils.Debug;

public class Rectoggle {
    Debug debug = new Debug(true);

    public int whoWins(int[] ledrow, int[] ledcol, int maxrows, int maxcols) {
        int n = ledcol.length;
        long sum = 0;
        for (int i = 0; i < n; i++) {
            int x = ledrow[i] % maxrows;
            int y = ledcol[i] % maxcols;

            int nimX = x ^ (x + 1);
            int nimY = y ^ (y + 1);

            sum ^= Nimber.product(nimX, nimY);

            debug.debug("sum", sum);
        }

        if (sum == 0) {
            return 2;
        }
        return 1;
    }
}
