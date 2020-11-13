package contest;

import template.graph.ErdosGallaiTheorem;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ChessTournament {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] x = new int[n];
        in.populate(x);
        if (!ErdosGallaiTheorem.possible(x.clone())) {
            out.println("IMPOSSIBLE");
            return;
        }
        out.println(Arrays.stream(x).sum() / 2);
        ErdosGallaiTheorem.buildGraph(x, (a, b) ->
                out.append(a + 1).append(' ').append(b + 1).println());
    }
}
