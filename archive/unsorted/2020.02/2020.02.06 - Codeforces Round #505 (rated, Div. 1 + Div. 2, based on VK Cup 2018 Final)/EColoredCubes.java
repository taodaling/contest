package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EColoredCubes {
    Cube[][] mat;
    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();
        mat = new Cube[n][n];
        int[][] init = new int[m][2];

        if (n == 1) {
            out.println(0);
            return;
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                init[i][j] = in.readInt() - 1;
            }
        }
        for (int i = 0; i < m; i++) {
            Cube cube = new Cube();
            cube.tx = in.readInt() - 1;
            cube.ty = in.readInt() - 1;
            mat[init[i][0]][init[i][1]] = cube;
        }

        int half = n / 2;
        while (true) {
            int leftist = m;
            for (int i = 0; i < m; i++) {
                if (mat[half][i] == null) {
                    leftist = i;
                    break;
                }
            }
            if (leftist == m) {
                break;
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (mat[i][j] == null) {
                        continue;
                    }
                    if (i != half && j < leftist && mat[i][j + 1] == null) {
                        moveTo(i, j, i, j + 1);
                    } else if (i < half && mat[i + 1][j] == null) {
                        moveTo(i, j, i + 1, j);
                    } else if (i > half && mat[i - 1][j] == null) {
                        moveTo(i, j, i - 1, j);
                    } else if (i == half && j > 0 && mat[i][j - 1] == null) {
                        moveTo(i, j, i, j - 1);
                    }
                }
            }
        }

        //insert sort
        for (int i = 0; i < m; i++) {
            int j = i;
            moveTo(half, j, half - 1, j);
            while (j - 1 >= 0 && mat[half][j - 1].ty > mat[half - 1][j].ty) {
                moveTo(half - 1, j, half - 1, j - 1);
                moveTo(half, j - 1, half, j);
                j--;
            }
            moveTo(half - 1, j, half, j);
        }

        //to position
        for (int i = 0; i < m; i++) {
            int j = half;
            if (mat[j][i] == null) {
                continue;
            }
            while (mat[j][i].tx < j) {
                moveTo(j, i, j - 1, i);
                j--;
            }
            while (mat[j][i].tx > j) {
                moveTo(j, i, j + 1, i);
                j++;
            }
        }

        for (int i = 0; i < n; i++) {
            //handle row
            Cube[] row = mat[i];
            while (true) {
                boolean handled = false;
                for (int j = 0; j < n; j++) {
                    if (row[j] == null) {
                        continue;
                    }
                    if (row[j].ty < j && row[j - 1] == null) {
                        handled = true;
                        moveTo(i, j, i, j - 1);
                    } else if (row[j].ty > j && row[j + 1] == null) {
                        handled = true;
                        moveTo(i, j, i, j + 1);
                    }
                }
                if (!handled) {
                    break;
                }
            }
        }

        out.println(operations.size());
        for (int[] op : operations) {
            for (int i = 0; i < 4; i++) {
                out.append(op[i] + 1).append(' ');
            }
            out.println();
        }
    }

    int[][] dirs = new int[][]{
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };

    public void moveTo(int i, int j, int ni, int nj) {
        mat[ni][nj] = mat[i][j];
        mat[i][j] = null;
        operations.add(SequenceUtils.wrapArray(i, j,
                ni, nj));
    }

    List<int[]> operations = new ArrayList<>(100000);
}

class Cube {
    int tx;
    int ty;
}
