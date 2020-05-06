package on2020_05.on2020_05_06_TopCoder_SRM__748.Rectoggle;



import template.math.Nimber;

public class Rectoggle {
    public int whoWins(int[] ledrow, int[] ledcol, int maxrows, int maxcols) {
        int n = ledcol.length;
        maxrows++;
        maxcols++;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            int x = ledrow[i];
            int y = ledcol[i];

            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    int nx = x + j;
                    int ny = y + k;
                    nx %= maxrows;
                    ny %= maxcols;
                    sum ^= Nimber.product(nx, ny);
                }
            }
        }

        if (sum == 0) {
            return 2;
        }
        return 1;
    }
}
