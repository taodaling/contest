package contest;

import template.ArrayUtils;
import template.FastInput;
import template.FastOutput;

public class TaskF {
    Modular mod = new Modular(998244353);
    Factorial fact = new Factorial(10000, mod);
    Composite comp = new Composite(fact);
    boolean[] rows;
    boolean[] cols;
    int[][] rowPutWays;
    int[][] colPutWays;

    int h;
    int w;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        h = in.readInt();
        w = in.readInt();

        rows = new boolean[h + 1];
        cols = new boolean[w + 1];

        int rowCnt = 0;
        int colCnt = 0;

        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            int x1 = in.readInt();
            int y1 = in.readInt();
            int x2 = in.readInt();
            int y2 = in.readInt();
            rows[x1] = rows[x2] = true;
            cols[y1] = cols[y2] = true;
        }

        for (int i = 1; i <= h; i++) {
            if (rows[i]) {
                continue;
            }
            rowCnt++;
        }

        for (int i = 1; i <= w; i++) {
            if (cols[i]) {
                continue;
            }
            colCnt++;
        }

        rowPutWays = new int[h + 1][h / 2 + 1];
        colPutWays = new int[w + 1][w / 2 + 1];

        ArrayUtils.deepFill(rowPutWays, -1);
        ArrayUtils.deepFill(colPutWays, -1);


        int ans = 0;
        for (int i = 0; i <= h / 2; i++) {
            for (int j = 0; j <= w / 2; j++) {
                // check
                if (i * 2 + j > rowCnt || j * 2 + i > colCnt) {
                    continue;
                }
                int rWay = rowPutWays(h, i);
                int cWay = colPutWays(w, j);
                int way = mod.mul(rWay, cWay);
                way = mod.mul(way, comp.composite(rowCnt - i * 2, j));
                way = mod.mul(way, comp.composite(colCnt - j * 2, i));
                way = mod.mul(way, fact.fact(j));
                way = mod.mul(way, fact.fact(i));
                ans = mod.plus(ans, way);
            }
        }

        out.println(ans);
    }

    public int rowPutWays(int i, int j) {
        if (j < 0) {
            return 0;
        }
        if (i <= 0) {
            return j == 0 ? 1 : 0;
        }
        if (rowPutWays[i][j] == -1) {
            if (rows[i]) {
                rowPutWays[i][j] = rowPutWays(i - 1, j);
            } else {
                rowPutWays[i][j] = rowPutWays(i - 1, j);
                if (i > 1 && !rows[i - 1]) {
                    rowPutWays[i][j] = mod.plus(rowPutWays[i][j], rowPutWays(i - 2, j - 1));
                }
            }
        }
        return rowPutWays[i][j];
    }

    public int colPutWays(int i, int j) {
        if (j < 0) {
            return 0;
        }
        if (i <= 0) {
            return j == 0 ? 1 : 0;
        }
        if (colPutWays[i][j] == -1) {
            if (cols[i]) {
                colPutWays[i][j] = colPutWays(i - 1, j);
            } else {
                colPutWays[i][j] = colPutWays(i - 1, j);
                if (i > 1 && !cols[i - 1]) {
                    colPutWays[i][j] = mod.plus(colPutWays[i][j], colPutWays(i - 2, j - 1));
                }
            }
        }
        return colPutWays[i][j];
    }

}
