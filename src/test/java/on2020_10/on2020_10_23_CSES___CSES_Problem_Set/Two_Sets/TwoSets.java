package on2020_10.on2020_10_23_CSES___CSES_Problem_Set.Two_Sets;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.io.PrintWriter;
import java.util.List;

public class TwoSets {
    public void add(IntegerArrayList a, IntegerArrayList b, int l, int r) {
        IntegerArrayList next = a;
        while (l < r) {
            next.add(l);
            next.add(r);
            l++;
            r--;
            next = next == a ? b : a;
        }
    }

    public void output(PrintWriter out, IntegerArrayList list) {
        out.println(list.size());
        for (int x : list.toArray()) {
            out.print(x);
            out.append(' ');
        }
        out.println();
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        IntegerArrayList a = new IntegerArrayList(n);
        IntegerArrayList b = new IntegerArrayList(n);
        if (n % 2 == 0) {
            if(n / 2 % 2 == 1){
                out.println("NO");
                return;
            }
            add(a, b, 1, n);
        } else {
            if (DigitUtils.ceilDiv(n, 2) % 2 == 1) {
                out.println("NO");
                return;
            }
            b.add(n);
            add(a, b, 1, n - 1);
        }
        out.println("YES");
        output(out, a);
        output(out, b);
    }
}
