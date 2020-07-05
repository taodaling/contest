package on2020_07.on2020_07_04_Codeforces___Codeforces_Global_Round_9.D__Replace_by_MEX;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerVersionArray;

import java.util.ArrayList;
import java.util.List;

public class DReplaceByMEX {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);

        List<Integer> ans = new ArrayList<>(n * 2);
        while (true) {
            int mex = mex(a);
            if (mex < n) {
                ans.add(mex);
                a[mex] = mex;
                continue;
            }
            int index = notSame(a);
            if (index == -1) {
                break;
            }
            ans.add(index);
            a[index] = mex;
        }

        out.println(ans.size());
        for (int x : ans) {
            out.append(x + 1).append(' ');
        }
        out.println();
    }

    public int notSame(int[] a) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] != i && a[i] < a.length) {
                return i;
            }
        }
        return -1;
    }

    IntegerVersionArray iva = new IntegerVersionArray(10000);

    public int mex(int[] a) {
        iva.clear();
        for (int x : a) {
            iva.set(x, 1);
        }
        int ans = 0;
        while (iva.get(ans) == 1) {
            ans++;
        }
        return ans;
    }
}
