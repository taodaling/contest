package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class Advertisement {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        DSUExt dsu = new DSUExt(n);
        dsu.init(n);
        boolean[] added = new boolean[n];
        Board[] boards = new Board[n];
        for (int i = 0; i < n; i++) {
            boards[i] = new Board();
            boards[i].x = i;
            boards[i].h = in.readInt();
        }
        Arrays.sort(boards, (a, b) -> -Integer.compare(a.h, b.h));
        long ans = 0;
        for (Board b : boards) {
            added[b.x] = true;
            if (b.x > 0 && added[b.x - 1]) {
                dsu.merge(b.x, b.x - 1);
            }
            if (b.x + 1 < n && added[b.x + 1]) {
                dsu.merge(b.x, b.x + 1);
            }
            int p = dsu.find(b.x);
            int width = dsu.right[p] - dsu.left[p] + 1;
            ans = Math.max(ans, (long)b.h * width);
        }
        out.println(ans);
    }
}

class Board {
    int h;
    int x;
}

class DSUExt extends DSU {
    int[] left;
    int[] right;


    public DSUExt(int n) {
        super(n);
        left = new int[n];
        right = new int[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        for (int i = 0; i < n; i++) {
            left[i] = right[i] = i;
        }
    }

    @Override
    protected void preMerge(int a, int b) {
        left[a] = Math.min(left[a], left[b]);
        right[a] = Math.max(right[a], right[b]);
        super.preMerge(a, b);
    }
}
