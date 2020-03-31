package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;

public class DWalkOnMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        if (k == 0) {
            out.append(1).append(' ').append(1).println();
            out.println(0);
            return;
        }
        int ceil = 1 << (Log2.floorLog(k) + 1);
        int[][] mat = new int[][]
                {{k + ceil, ceil, 0},
                        {k, k + ceil, k},
                        {0, k, k}};

        out.append(3).append(' ').append(3).println();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                out.append(mat[i][j]).append(' ');
            }
            out.println();
        }
    }
}
