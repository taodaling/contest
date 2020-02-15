package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerList;

public class ADisplayTheNumber {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        IntegerList list = new IntegerList(n);
        while (n >= 2) {
            if (n == 3) {
                list.add(7);
                n -= 3;
            } else {
                n -= 2;
                list.add(1);
            }
        }

        list.reverse();
        for (int i = 0; i < list.size(); i++) {
            out.append(list.get(i));
        }
        out.println();
    }
}
