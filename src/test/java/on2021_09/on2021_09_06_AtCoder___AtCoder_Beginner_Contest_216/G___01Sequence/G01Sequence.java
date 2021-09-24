package on2021_09.on2021_09_06_AtCoder___AtCoder_Beginner_Contest_216.G___01Sequence;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class G01Sequence {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        List<Query>[] qs = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            Query q = new Query();
            q.l = in.ri() - 1;
            q.r = in.ri() - 1;
            q.s = in.ri();
            qs[q.r].add(q);
        }
        IntegerBIT bit = new IntegerBIT(n);
        Deque<Integer> dq = new ArrayDeque<>(n);
        for (int i = 0; i < n; i++) {
            debug.debug("i", i);
            dq.addLast(i);
            for (Query q : qs[i]) {
                while (q.s - bit.query(q.l + 1, q.r + 1) > 0) {
                    int tail = dq.removeLast();
                    bit.update(tail + 1, 1);
                }
            }
            debug.debug("bit", bit);
        }
        for(int i = 0; i < n; i++){
            int v = bit.query(i + 1, i + 1);
            out.append(v).append(' ');
        }
    }

    Debug debug = new Debug(false);
}

class Query {
    int l;
    int r;
    int s;
}
