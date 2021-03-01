package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class ARemovingColumns {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        char[][] mat = new char[n][];
        for (int i = 0; i < n; i++) {
            mat[i] = in.rc(m);
        }
        List<Block> prev = new ArrayList<>();
        prev.add(new Block(0, n - 1));
        int ans = 0;
        for (int i = 0; i < m; i++) {
            boolean conflict = false;
            for (Block b : prev) {
                for (int j = b.l + 1; j <= b.r; j++) {
                    if (mat[j][i] < mat[j - 1][i]) {
                        conflict = true;
                    }
                }
            }
            if (conflict) {
                ans++;
                continue;
            }
            List<Block> next = new ArrayList<>(n);
            for (Block b : prev) {
                int last = b.l;
                for (int j = b.l; j <= b.r; j++) {
                    if (j == b.r || mat[j][i] < mat[j + 1][i]) {
                        next.add(new Block(last, j));
                        last = j + 1;
                    }
                }
            }
            prev = next;
        }
        out.println(ans);
    }
}

class Block {
    int l;
    int r;

    public Block(int l, int r) {
        this.l = l;
        this.r = r;
    }
}
