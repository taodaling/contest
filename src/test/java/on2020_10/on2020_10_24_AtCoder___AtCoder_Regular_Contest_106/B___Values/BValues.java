package on2020_10.on2020_10_24_AtCoder___AtCoder_Regular_Contest_106.B___Values;



import template.datastructure.DSU;
import template.io.FastInput;

import java.io.PrintWriter;

public class BValues {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        DSUExt ext = new DSUExt(n);
        ext.init();
        for (int i = 0; i < n; i++) {
            ext.src[i] = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            ext.dst[i] = in.readInt();
        }
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            ext.merge(u, v);
        }
        for (int i = 0; i < n; i++) {
            if (ext.find(i) == i) {
                if(ext.src[i] != ext.dst[i]){
                    out.println("No");
                    return;
                }
            }
        }
        out.println("Yes");
    }
}

class DSUExt extends DSU {
    long[] src;
    long[] dst;

    public DSUExt(int n) {
        super(n);
        src = new long[n];
        dst = new long[n];
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        src[a] += src[b];
        dst[a] += dst[b];
    }
}