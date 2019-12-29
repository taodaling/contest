package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerArray2IntegerDequeAdapter;
import template.primitve.generated.IntegerHashMap;
import template.utils.SequenceUtils;

public class CStackOfPresents {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        int[] b = new int[m];
        boolean[] sent = new boolean[m];
        int[] giftToBIndex = new int[n + 1];
        SequenceUtils.deepFill(giftToBIndex, -1);
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < m; i++) {
            b[i] = in.readInt();
            giftToBIndex[b[i]] = i;
        }

        int sum = 0;
        long total = 0;
        IntegerArray2IntegerDequeAdapter deque = new IntegerArray2IntegerDequeAdapter(a, 0, n - 1);
        for (int i = 0; i < m; i++) {
            if (sent[i]) {
                sum--;
                continue;
            }
            while (deque.peekFirst() != b[i]) {
                sum++;
                int index = giftToBIndex[deque.removeFirst()];
                if(index != -1) {
                    total++;
                    sent[index] = true;
                }
            }
            deque.removeFirst();
            total += sum * 2 + 1;
            sent[i] = true;
        }

        out.println(total);
    }
}
