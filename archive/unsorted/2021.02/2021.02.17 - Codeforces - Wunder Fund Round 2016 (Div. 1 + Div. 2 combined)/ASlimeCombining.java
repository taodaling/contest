package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class ASlimeCombining {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerArrayList list = new IntegerArrayList();
        for (int i = 30; i >= 0; i--) {
            if (Bits.get(n, i) == 1) {
                list.add(i);
            }
        }
        for (int x : list.toArray()) {
            out.append(x + 1).append(' ');
        }
    }
}
