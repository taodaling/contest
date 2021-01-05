package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

public class CLatinSquare {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.ri() - 1;
            }
        }
        ElementModifyWrapper[] elements = new ElementModifyWrapper[]{
                new ElementModifyWrapper((i, j, v) -> i),
                new ElementModifyWrapper((i, j, v) -> j),
                new ElementModifyWrapper((i, j, v) -> v),
        };
        for (int i = 0; i < m; i++) {
            char x = in.rc();
            if (x == 'R') {
                elements[1].modify(1);
            } else if (x == 'L') {
                elements[1].modify(-1);
            } else if (x == 'U') {
                elements[0].modify(-1);
            } else if (x == 'D') {
                elements[0].modify(1);
            } else if (x == 'I') {
                SequenceUtils.swap(elements, 1, 2);
            } else if (x == 'C') {
                SequenceUtils.swap(elements, 0, 2);
            }
        }

        int[][] transform = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int ci = DigitUtils.mod(elements[0].apply(i, j, mat[i][j]), n);
                int cj = DigitUtils.mod(elements[1].apply(i, j, mat[i][j]), n);
                int cv = DigitUtils.mod(elements[2].apply(i, j, mat[i][j]), n);
                transform[ci][cj] = cv;
            }
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                out.append(transform[i][j] + 1).append(' ');
            }
            out.println();
        }
        out.println();
    }
}

interface Element {
    int apply(int i, int j, int v);
}

class ElementModifyWrapper implements Element {
    public ElementModifyWrapper(Element e) {
        this.e = e;
    }

    int mod;

    @Override
    public int apply(int i, int j, int v) {
        return e.apply(i, j, v) + mod;
    }

    public void modify(int x) {
        mod += x;
    }

    Element e;
}