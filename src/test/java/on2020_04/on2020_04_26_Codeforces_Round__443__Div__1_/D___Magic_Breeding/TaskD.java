package on2020_04.on2020_04_26_Codeforces_Round__443__Div__1_.D___Magic_Breeding;




import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.datastructure.BitSet;

import java.util.ArrayList;
import java.util.List;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int q = in.readInt();
        int[][] creature = new int[k][n];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                creature[i][j] = in.readInt();
            }
        }

        List<Query> third = new ArrayList<>(q);
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            qs[i].t = in.readInt();
            qs[i].x = in.readInt() - 1;
            qs[i].y = in.readInt() - 1;
            if (qs[i].t == 3) {
                third.add(qs[i]);
            }
        }

        int mask = 1 << k;
        BitSet[] traces = new BitSet[k + q];
        for (int i = 0; i < k + q; i++) {
            traces[i] = new BitSet(mask);
        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < mask; j++) {
                if (Bits.get(j, i) == 1) {
                    traces[i].set(j);
                } else {
                    traces[i].clear(j);
                }
            }
        }

        int len = k;
        for (int i = 0; i < q; i++) {
            if (qs[i].t == 3) {
                continue;
            }
            if (qs[i].t == 1) {
                //max
                traces[len].or(traces[qs[i].x]);
                traces[len].or(traces[qs[i].y]);
            }
            if (qs[i].t == 2) {
                traces[len].or(traces[qs[i].x]);
                traces[len].and(traces[qs[i].y]);
            }
            len++;
        }

        for (Query query : third) {
            //reverse scan
            //find min 1
            query.ans = -1;
            for (int i = 0; i < k; i++) {
                int bits = 0;
                for (int j = 0; j < k; j++) {
                    bits = Bits.set(bits, j, creature[j][query.y] >= creature[i][query.y]);
                }
                if (traces[query.x].get(bits)) {
                    query.ans = Math.max(query.ans, creature[i][query.y]);
                }
            }
        }

        for (Query query : third) {
            out.println(query.ans);
        }
    }
}

class Query {
    int t;
    int x;
    int y;
    int ans;
}
