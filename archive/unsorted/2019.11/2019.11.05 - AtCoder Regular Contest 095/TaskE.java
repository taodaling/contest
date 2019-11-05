package contest;

import java.io.PrintWriter;
import java.util.Arrays;

import template.FastInput;
import template.SequenceUtils;

public class TaskE {
    int h;
    int w;
    char[][] mat;

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        h = in.readInt();
        w = in.readInt();

        matched = new boolean[w];
        mat = new char[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                mat[i][j] = in.readChar();
            }
        }

        prepareGroup(0, h - 1, 0);

        out.println(valid ? "YES" : "NO");
    }

    boolean valid = false;
    boolean[] matched;

    public boolean match(int i, int j) {
        for (int k = 0; k < h; k++) {
            if (mat[k][i] != mat[h - 1 - k][j]) {
                return false;
            }
        }
        return true;
    }

    public void check() {
        Arrays.fill(matched, false);
        int single = 0;
        for (int i = 0; i < w; i++) {
            if (matched[i]) {
                continue;
            }
            for (int j = i + 1; j < w; j++) {
                if (match(i, j)) {
                    matched[i] = matched[j] = true;
                    break;
                }
            }

            if (!matched[i]) {
                if (single < w % 2 && match(i, i)) {
                    matched[i] = true;
                    single++;
                }
            }

            if (!matched[i]) {
                return;
            }
        }

        valid = true;
    }

    public void prepareGroup(int l, int r, int single) {
        if (valid) {
            return;
        }

        if (l >= r) {
            check();
            return;
        }

        if (single < h % 2) {
            SequenceUtils.swap(mat, l, h / 2);
            prepareGroup(l, r, single + 1);
            SequenceUtils.swap(mat, l, h / 2);
        }

        for (int i = l + 1; i <= r; i++) {
            SequenceUtils.swap(mat, i, r);
            prepareGroup(l + 1, r - 1, single);
            SequenceUtils.swap(mat, i, r);
        }
    }

}


class Group {
    int a;
    int b;
}
