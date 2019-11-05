package contest;

import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.FastOutput;
import template.PreSum;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int w = in.readInt();

        List<Point> pts = new ArrayList<>(h * w);
        int[][] rowData = new int[1801][1801];
        int[][] colData = new int[1801][1801];
        for (int i = 1; i <= h; i++) {
            for (int j = 1; j <= w; j++) {
                if (in.readChar() == '#') {
                    Point pt = new Point();
                    pt.x = i + j + 600;
                    pt.y = i - j + w + 600;
                    pts.add(pt);
                    rowData[pt.y][pt.x] = 1;
                    colData[pt.x][pt.y] = 1;
                }
            }
        }


        PreSum[] rows = new PreSum[1801];
        PreSum[] cols = new PreSum[1801];
        for (int i = 1; i <= 1800; i++) {
            rows[i] = new PreSum(rowData[i]);
            cols[i] = new PreSum(colData[i]);
        }


        long ans = 0;

        int[][] rowDp = new int[1801][1801];
        int[][] colDp = new int[1801][1801];
        for (int side = 2; side <= h + w; side += 2) {
            int len = 1 + side * 2;
            // rowDp
            for (int i = 1 + 600; i <= h + w + 600; i++) {
                for (int j = 600; j <= 1800; j++) {
                    rowDp[i][j] = rowDp[i][j - 1];
                    if (j - len > 0 && rowData[i][j - len] == 1 && rowData[i][j - len + side] == 1) {
                        rowDp[i][j]--;
                    }
                    if (j - side > 0 && rowData[i][j - side] == 1 && rowData[i][j] == 1) {
                        rowDp[i][j]++;
                    }
                }
            }
            // colDp
            for (int j = 1 + 600; j <= h + w + 600; j++) {
                for (int i = 600; i <= 1800; i++) {
                    colDp[i][j] = colDp[i - 1][j];
                    if (i - len > 0 && rowData[i - len][j] == 1 && rowData[i - len + side][j] == 1) {
                        colDp[i][j]--;
                    }
                    if (i - side > 0 && rowData[i - side][j] == 1 && rowData[i][j] == 1) {
                        colDp[i][j]++;
                    }
                }
            }

            for (Point pt : pts) {
                int l = pt.x - side;
                int r = pt.x + side;
                int b = pt.y - side;
                int t = pt.y + side;

                long contri = 0;
                contri += rowDp[t][r];
                contri += rowDp[b][r];
                contri += colDp[t][l];
                contri += colDp[t][r];

                contri += rowData[t][pt.x] * (cols[l].intervalSum(pt.y, t - 1) + cols[r].intervalSum(pt.y, t - 1));
                contri += rowData[b][pt.x] * (cols[l].intervalSum(b + 1, pt.y) + cols[r].intervalSum(b + 1, pt.y));
                contri += rowData[pt.y][l] * (rows[b].intervalSum(l + 1, pt.x - 1) + rows[t].intervalSum(l + 1, pt.x - 1));
                contri += rowData[pt.y][r] * (rows[b].intervalSum(pt.x + 1, r - 1) + rows[t].intervalSum(pt.x + 1, r - 1));

                ans += contri;
            }
        }

        out.println(ans / 3);
    }

    public long choose2(int n) {
        return (long) n * (n - 1) / 2;
    }
}


class Point {
    int x;
    int y;

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
