package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

public class BCopyCopyCopyCopyCopy {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        IntegerList list = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            list.add(in.readInt());
        }
        list.unique();
        out.println(list.size());
    }
}
