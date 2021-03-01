package contest;

import template.binary.Bits;
import template.datastructure.DSU;
import template.graph.DirectedEdge;
import template.graph.DirectedTarjanSCC;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DSkate {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.ri();
        int w = in.ri();
        char[][] mat = new char[h][w];
        for (int i = 0; i < h; i++) {
            in.rs(mat[i]);
        }
        DSUExt dsu = new DSUExt(h + w);
        dsu.init();
        mat[0][0] = mat[0][w - 1] = mat[h - 1][0] = mat[h - 1][w - 1] = '#';
        for (int i = 0; i < h; i++) {
            if (mat[i][0] == '#' || mat[i][w - 1] == '#') {
                dsu.ok[i] |= 1;
            }
        }
        for (int i = 0; i < w; i++) {
            if (mat[0][i] == '#' || mat[h - 1][i] == '#') {
                dsu.ok[i + h] |= 2;
            }
        }
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (mat[i][j] == '#') {
                    dsu.merge(i, j + h);
                }
            }
        }

        int c0 = 0;
        int c1 = 0;
        Set<Integer> rowSet = new HashSet<>();
        Set<Integer> colSet = new HashSet<>();
        for(int i = 0; i < h; i++){
            rowSet.add(dsu.find(i));
        }
        for(int i = 0; i < w; i++){
            colSet.add(dsu.find(i + h));
        }
        for(int v : rowSet){
            if(Bits.get(dsu.ok[v], 0) == 0){
                c0++;
            }
        }
        for(int v : colSet){
            if(Bits.get(dsu.ok[v], 0) == 0){
                c1++;
            }
        }

        int ans = Math.min(c0, c1);
        out.println(ans);
    }
}

class DSUExt extends DSU {
    int[] ok;

    public DSUExt(int n) {
        super(n);
        ok = new int[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        Arrays.fill(ok, 0);
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        ok[a] = ok[a] | ok[b];
    }
}
