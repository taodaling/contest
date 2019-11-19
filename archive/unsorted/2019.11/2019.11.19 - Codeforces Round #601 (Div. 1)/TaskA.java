package contest;

import template.FastInput;
import template.FastOutput;

public class TaskA {
    char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int r = in.readInt();
        int c = in.readInt();
        int k = in.readInt();
        int sum = 0;
        int[][] mat = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                mat[i][j] = in.readChar() == 'R' ? 1 : 0;
                sum += mat[i][j];
            }
        }

        int avg = sum / k;
        int more = sum % k;
        char[][] ans = new char[r][c];
        int hold = 0;
        int sign = 0;

        for (int i = 0; i < r; i++) {
            int start;
            int end;
            int step;
            if (i % 2 == 0) {
                start = 0;
                end = c;
                step = 1;
            } else {
                start = c - 1;
                end = -1;
                step = -1;
            }
            for (int j = start; j != end; j += step) {
                if (hold + mat[i][j] > avg + Integer.signum(more)) {
                    if (more > 0) {
                        more--;
                    }
                    hold = 0;
                    sign++;
                }
                hold += mat[i][j];
                ans[i][j] = chars[sign];
            }
        }

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                out.append(ans[i][j]);
            }
            out.println();
        }
    }
}
