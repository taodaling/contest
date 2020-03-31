package on2020_03.on2020_03_31_Codeforces_Round__630__Div__2_.B__Composite_Coloring;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerVersionArray;
import template.utils.Debug;

public class BCompositeColoring {
    private int decompose(int x) {
        for (int i = 2; i <= x; i++) {
            if (x % i == 0) {
                return i;
            }
        }
        return -1;
    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] colors = new int[n];

        int cnt = 0;
        IntegerList list = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            colors[i] = decompose(in.readInt());
            list.add(colors[i]);
        }
        list.unique();
        out.println(list.size());
        for (int c : colors) {
            out.append(list.binarySearch(c) + 1).append(' ');
        }
        out.println();
    }
}
