package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.IntegerList;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int n = in.readInt();
        IntegerList list = new IntegerList(n);
        if (k % 2 == 0) {
            list.add(k / 2);
            for (int i = 1; i < n; i++) {
                list.add(k);
            }
            output(out, list);
            return;
        }

        for (int i = 0; i < n; i++) {
            list.add(DigitUtils.ceilDiv(k, 2));
        }

        int offset = n / 2;
        while (offset > 0) {
            offset--;
            int tail = list.pop();
            if (tail > 1) {
                tail--;
                list.add(tail);
                while (list.size() < n) {
                    list.add(k);
                }
            } else {
//                offset--;
//                if (offset < 0) {
//                    list.add(tail);
//                }
            }
        }
        output(out, list);
    }

    public void output(FastOutput out, IntegerList list) {
        for (int i = 0; i < list.size(); i++) {
            out.append(list.get(i)).append(' ');
        }
    }
}
