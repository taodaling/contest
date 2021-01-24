package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongDeque;
import template.primitve.generated.datastructure.LongDequeImpl;
import template.primitve.generated.graph.*;
import template.problem.GridHamiltonPath;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.List;

public class GridPathConstruction {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = new int[2];
        int[] b = new int[2];
        for (int i = 0; i < 2; i++) {
            a[i] = in.ri() - 1;
        }
        for (int i = 0; i < 2; i++) {
            b[i] = in.ri() - 1;
        }

        StringBuilder ans = new StringBuilder(n * m);
        boolean possible = new GridHamiltonPath().solve(n, m, a, b, (dx, dy) -> {
            if (dx == -1) {
                ans.append('U');
            } else if (dx == 1) {
                ans.append('D');
            } else if (dy == -1) {
                ans.append('L');
            } else {
                ans.append('R');
            }
        });

        out.println(possible ? "YES" : "NO");
        if (possible) {
            out.println(ans);
        }
    }
}
