package on2021_03.on2021_03_18_Codeforces___Codeforces_Round__248__Div__1_.B__Nanami_s_Digital_Board;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.utils.Debug;
import template.utils.SequenceUtils;

import javax.swing.*;
import java.util.Arrays;
import java.util.function.Consumer;

public class BNanamisDigitalBoard {
    public void flip(int[][] mat) {
        int n = mat.length;
        for (int i = 0; i < n; i++) {
            SequenceUtils.reverse(mat[i]);
        }
    }

    public int[][] rotate(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        int[][] ans = new int[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans[j][i] = mat[i][j];
            }
        }
        return ans;
    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int q = in.ri();
        int[][] left = new int[n][m];
        int[][] right = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                right[i][j] = left[i][j] = in.ri();
            }
        }
        flip(right);
        int[][] top = rotate(left);
        int[][] bot = rotate(right);
        flip(bot);
        Board leftBorder = new Board(left);
        Board rightBorder = new Board(right);
        Board botBorder = new Board(bot);
        Board topBorder = new Board(top);
        for (int i = 0; i < q; i++) {
            debug.debug("i", i);
            //debug.debug("leftBorder", leftBorder);
            //debug.debug("rightBorder", rightBorder);
            debug.debug("topBorder", topBorder);
            //debug.debug("topBorder", topBorder);
            int t = in.ri();
            int x = in.ri() - 1;
            int y = in.ri() - 1;
            if (t == 1) {
                leftBorder.flip(x, y);
                rightBorder.flip(x, m - 1 - y);
                topBorder.flip(y, x);
                botBorder.flip(m - 1 - y, n - 1 - x);
            } else {
                int ans = leftBorder.query(x, y);
                ans = Math.max(ans, rightBorder.query(x, m - 1 - y));
                ans = Math.max(ans, topBorder.query(y, x));
                ans = Math.max(ans, botBorder.query(m - 1 - y, n - 1 - x));
                out.println(ans);
            }
        }

        return;
    }
}

class Board {
    int[][] mat;
    int[][] right;
    int n;
    int m;
    IntegerMultiWayStack stack;

    public void flip(int i, int j) {
        mat[i][j] ^= 1;
        recalcRow(i);
    }

    public Board(int[][] mat) {
        n = mat.length;
        m = mat[0].length;
        this.mat = mat;
        right = new int[n][m];
        for (int i = 0; i < n; i++) {
            recalcRow(i);
        }
        stack = new IntegerMultiWayStack(m + 1, n);
    }

    public void recalcRow(int r) {
        for (int i = m - 1; i >= 0; i--) {
            right[r][i] = i == m - 1 ? m : right[r][i + 1];
            if (mat[r][i] == 0) {
                right[r][i] = i;
            }
        }
    }

    public int query(int x, int y) {
        int h = -1;
        int l = n;
        stack.clear();
        for (int i = 0; i < n; i++) {
            stack.addLast(right[i][y], i);
        }
        int maxArea = 0;
        for (int i = y; i < m; i++) {
            while (!stack.isEmpty(i)) {
                int r = stack.removeLast(i);
                if (r <= x) {
                    h = Math.max(r, h);
                }
                if (r >= x) {
                    l = Math.min(r, l);
                }
            }
            maxArea = Math.max(maxArea, (l - h - 1) * (i - y + 1));
        }
        return maxArea;
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("\n");
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                ans.append(mat[i][j]).append(' ');
            }
            ans.append("\n");
        }
        return ans.toString();
    }
}