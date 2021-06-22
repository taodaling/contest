package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.Best_Solution_Unknown;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.utils.SortUtils;

import java.util.stream.IntStream;

public class BestSolutionUnknown {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> Integer.compare(a[i], a[j]), 0, n);
        int maxVal = a[indices[n - 1]];
        IntegerDeque dq = new IntegerDequeImpl(n);
        int[] prev = new int[n];
        int[] next = new int[n];
        dq.clear();
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && a[dq.peekLast()] <= a[i]) {
                dq.removeLast();
            }
            prev[i] = dq.isEmpty() ? -1 : dq.peekLast();
            dq.addLast(i);
        }
        dq.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!dq.isEmpty() && a[dq.peekLast()] <= a[i]) {
                dq.removeLast();
            }
            next[i] = dq.isEmpty() ? n : dq.peekLast();
            dq.addLast(i);
        }

        boolean[] res = new boolean[n];
        DSUExt dsu = new DSUExt(n, a);
        dsu.init();
        for (int i : indices) {
            while (true) {
                int maxIndex = dsu.maxIndex[dsu.find(i)];
                int v = a[i];
                v += i - prev[maxIndex];
                v += next[maxIndex] - i;
                v -= 2;

                if (prev[maxIndex] >= 0 && v >= a[prev[maxIndex]]) {
                    dsu.merge(i, prev[maxIndex]);
                } else if (next[maxIndex] < n && v >= a[next[maxIndex]]) {
                    dsu.merge(i, next[maxIndex]);
                } else {
                    break;
                }
            }
            int maxIndex = dsu.maxIndex[dsu.find(i)];
            if (a[maxIndex] == maxVal) {
                res[i] = true;
            }
        }
        int cnt = 0;
        for (boolean x : res) {
            cnt += x ? 1 : 0;
        }
        out.println(cnt);
        for (int i = 0; i < n; i++) {
            if (res[i]) {
                out.append(i + 1).append(' ');
            }
        }
    }
}

class DSUExt extends DSU {
    int[] maxIndex;
    int[] v;

    public DSUExt(int n, int[] a) {
        super(n);
        maxIndex = new int[n];
        this.v = a;
    }

    @Override
    public void init(int n) {
        super.init(n);
        for (int i = 0; i < n; i++) {
            maxIndex[i] = i;
        }
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        if (v[maxIndex[a]] < v[maxIndex[b]]) {
            maxIndex[a] = maxIndex[b];
        }
    }
}
