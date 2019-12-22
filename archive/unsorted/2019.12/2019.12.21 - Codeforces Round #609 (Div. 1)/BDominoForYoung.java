package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerDequeImpl;

public class BDominoForYoung {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        long ans = 0;
        int first = 0;
        IntegerDequeImpl deque = new IntegerDequeImpl(n);
        for (int i = 0; i < n; i++) {
            while (!deque.isEmpty() && first + deque.peekLast() > a[i]) {
                deque.removeLast();
            }
            if (deque.isEmpty()) {
                ans += a[i] / 2;
                if (a[i] % 2 == 1) {
                    deque.addLast(1);
                    first = 0;
                }
            } else {
                ans += deque.size();
                int remain = a[i] - (first + deque.peekLast());
                ans += remain / 2;
                if (first == 0) {
                    if (remain % 2 == 0) {
                        deque.removeLast();
                    } else {
                    }
                } else {
                    if (remain % 2 == 0) {
                    } else {
                        deque.addLast(deque.peekLast() + first + 1);
                    }
                }
                first = 1 - first;
            }
        }

        out.println(ans);
    }
}
