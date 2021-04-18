package on2021_03.on2021_03_29_Codeforces___Codeforces_Round__223__Div__1_.E__Sereja_and_Dividing;



import template.datastructure.RangeTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahanSummation;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SortUtils;

import java.util.TreeSet;
import java.util.stream.IntStream;

public class ESerejaAndDividing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] b = in.ri(n);
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> -Integer.compare(b[i], b[j]), 0, n);
        IntegerArrayList prev = new IntegerArrayList();
        IntegerArrayList next = new IntegerArrayList();
        RangeTree rt = new RangeTree(n + 2);
        rt.add(0);
        rt.add(n + 1);
        double ans = 0;
        int lim = 60;
        for (int index : indices) {
            prev.clear();
            prev.add(index + 1);
            for (int i = 0, x = index + 1; i < lim; i++) {
                int floor = rt.floor(x - 1);
                if (floor == -1) {
                    break;
                }
                x = floor;
                prev.add(x);
            }
            next.clear();
            next.add(index + 1);

            for (int i = 0, x = index + 1; i < lim; i++) {
                int ceil = rt.ceil(x + 1);
                if (ceil == -1) {
                    break;
                }
                x = ceil;
                next.add(x);
            }
            int[] A = prev.getData();
            int[] B = next.getData();
            int aSize = prev.size();
            int bSize = next.size();
            double L = 0;
            double R = 0;
            for (int i = 0; i + 1 < aSize; i++) {
                L += (double) (A[i] - A[i + 1]) / (1L << i);
            }
            for (int i = 0; i + 1 < bSize; i++) {
                R += (double) (B[i + 1] - B[i]) / (1L << i);
            }
            ans += b[index] * L * R / 2;
            rt.add(index + 1);
        }

        ans /= n;
        ans /= n;
        out.println(ans);
    }
}

