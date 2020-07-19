package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.Deque;

public class ENegativeDoubling {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] A = new long[n];
        in.populate(A);

        long[] tail = dp(A);
        SequenceUtils.reverse(A);
        long[] head = dp(A);
        SequenceUtils.reverse(head);

        long ans = (long)1e18;
        for(int i = 0; i <= n; i++){
            long req = 0;
            req += i == 0 ? 0 : head[i - 1] * 2 + i;
            req += i == n ? 0 : tail[i] * 2;
            ans = Math.min(ans, req);
        }

        out.println(ans);
    }

    public long[] dp(long[] A) {
        Deque<Block> dq = new ArrayDeque<>(A.length);
        long[] ans = new long[A.length];
        int n = A.length;
        ans[A.length - 1] = 0;
        dq.addFirst(new Block(A[n - 1], A[n - 1], 1));
        for (int i = A.length - 2; i >= 0; i--) {
            ans[i] = ans[i + 1];
            while (dq.peekFirst().head < A[i]) {
                Block first = dq.removeFirst();
                first.inc();
                ans[i] += first.size;
                if (!dq.isEmpty() && first.tail * 4 > dq.peekFirst().head) {
                    first = Block.merge(first, dq.removeFirst());
                }
                dq.addFirst(first);
            }
            Block added = new Block(A[i], A[i], 1);
            if (A[i] * 4 > dq.peekFirst().head) {
                added = Block.merge(added, dq.removeFirst());
            }
            dq.addFirst(added);
        }
        return ans;
    }
}

class Block {
    long head;
    long tail;
    int size;

    public Block(long head, long tail, int size) {
        this.head = head;
        this.tail = tail;
        this.size = size;
    }

    public void inc() {
        head *= 4;
        tail *= 4;
    }

    public static Block merge(Block a, Block b) {
        Block ans = new Block(a.head, b.tail, a.size + b.size);
        return ans;
    }
}
