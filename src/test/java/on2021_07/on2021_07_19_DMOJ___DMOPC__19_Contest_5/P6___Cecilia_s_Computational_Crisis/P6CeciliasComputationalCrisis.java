package on2021_07.on2021_07_19_DMOJ___DMOPC__19_Contest_5.P6___Cecilia_s_Computational_Crisis;



import template.binary.Bits;
import template.datastructure.BitSet;
import template.graph.BipartiteMatch;
import template.graph.DinicBipartiteMatch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P6CeciliasComputationalCrisis {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            int cnt = 0;
            for (int j = 0; j < m; j++) {
                char c = in.rc();
                mat[i][j] = c == '?' ? -1 : c == '.' ? 0 : 1;
                if (mat[i][j] == -1) {
                    if (cnt < 9) {
                        cnt++;
                    } else {
                        mat[i][j] = 0;
                    }
                }
            }
        }


        IntegerArrayList[] adj = new IntegerArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new IntegerArrayList(1 << 9);
        }
        IntegerArrayList undecide = new IntegerArrayList(m);
        for (int i = 0; i < n; i++) {
            undecide.clear();
            BitSet tmp = new BitSet(m);
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == -1) {
                    undecide.add(j);
                } else if (mat[i][j] == 1) {
                    tmp.set(j);
                }
            }
            int[] all = undecide.toArray();
            for (int j = 0; j < 1 << all.length; j++) {
                BitSet building = new BitSet(tmp);
                for (int k = 0; k < all.length; k++) {
                    if (Bits.get(j, k) == 1) {
                        building.set(all[k]);
                    }
                }
                adj[i].add(getId(building));
            }
        }
        DinicBipartiteMatch bm = new DinicBipartiteMatch(n, map.size());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                bm.addEdge(i, adj[i].get(j));
            }
        }

        int ans = bm.solve();
        if (ans < n) {
            out.println("NO");
            return;
        }
        out.println("YES");
        for (int i = 0; i < n; i++) {
            BitSet related = list.get(bm.mateOfLeft(i));
            for (int j = 0; j < m; j++) {
                out.append(related.get(j) ? 'X' : '.');
            }
            out.println();
        }
    }

    Map<BitSet, Integer> map = new HashMap<>((1 << 9) * 400);
    List<BitSet> list = new ArrayList<>((1 << 9) * 400);

    public int getId(BitSet bs) {
        Integer ans = map.get(bs);
        if (ans == null) {
            ans = map.size();
            map.put(bs, ans);
            list.add(bs);
        }
        return ans;
    }
}
