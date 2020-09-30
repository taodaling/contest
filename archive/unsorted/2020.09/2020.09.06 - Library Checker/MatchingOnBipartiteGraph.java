package contest;

import template.graph.BipartiteMatch;
import template.graph.DinicBipartiteMatch;
import template.graph.HungrayAlgoMatrixStyle;
import template.io.FastInput;
import template.io.FastOutput;

public class MatchingOnBipartiteGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int l = in.readInt();
        int r = in.readInt();
        int n = in.readInt();

        DinicBipartiteMatch match = new DinicBipartiteMatch(l, r);
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            match.addEdge(x, y);
        }

        int mm = match.solve();

        out.println(mm);
        for (int i = 0; i < l; i++) {
            if (match.mateOfLeft(i) != -1) {
                out.append(i).append(' ').append(match.mateOfLeft(i)).println();
            }
        }
    }
}
