package on2020_09.on2020_09_29_Library_Checker.Persistent_UnionFind;



import template.datastructure.PersistentArray;
import template.io.FastInput;

import java.io.PrintWriter;

public class PersistentUnionFind {
    PersistentArray<Integer> curP;
    PersistentArray<Integer> curSize;

    PersistentArray<Integer>[] historyP;
    PersistentArray<Integer>[] historySize;

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int q = in.readInt();

        historyP = new PersistentArray[q + 1];
        historySize = new PersistentArray[q + 1];
        historyP[0] = new PersistentArray<>(n);
        historySize[0] = new PersistentArray<Integer>(n).fill(1);

        for(int i = 1; i <= q; i++) {
            int t = in.readInt();
            int k = in.readInt() + 1;
            int u = in.readInt();
            int v = in.readInt();

            curP = historyP[k];
            curSize = historySize[k];
            if(t == 0){
                merge(u, v);
            }else{
                out.println(find(u) == find(v) ? 1 : 0);
            }
            historyP[i] = curP;
            historySize[i] = curSize;
        }
    }

    public int find(int x) {
        Integer p = curP.get(x);
        return p == null ? x : find(p);
    }

    public void merge(int a, int b){
        a = find(a);
        b = find(b);
        if(a == b){
            return;
        }
        int sizeA = curSize.get(a);
        int sizeB = curSize.get(b);
        if(sizeA < sizeB){
            int tmp = a;
            a = b;
            b = tmp;
        }
        curP = curP.set(b, a);
        curSize = curSize.set(a, sizeA + sizeB);
    }
}

