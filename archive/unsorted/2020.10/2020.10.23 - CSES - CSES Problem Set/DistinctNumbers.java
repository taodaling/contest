package contest;

import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.io.PrintWriter;

public class DistinctNumbers {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        IntegerArrayList list = new IntegerArrayList(a);
        list.unique();
        out.println(list.size());
    }
}
