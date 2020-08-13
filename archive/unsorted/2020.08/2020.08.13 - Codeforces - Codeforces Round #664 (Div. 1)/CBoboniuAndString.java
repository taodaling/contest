package contest;

import template.algo.IntBinarySearch;
import template.datastructure.Range2DequeAdapter;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerMinQueue;

import java.util.Arrays;

public class CBoboniuAndString {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        pts = new int[n][2];
        char[] s = new char[(int) 1e6];
        for (int i = 0; i < n; i++) {
            int len = in.readString(s, 0);
            for (int j = 0; j < len; j++) {
                if (s[j] == 'B') {
                    pts[i][0]++;
                } else {
                    pts[i][1]++;
                }
            }
        }
        Arrays.sort(pts, (a, b) -> Integer.compare(a[0], b[0]));

        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return CBoboniuAndString.this.check(mid) != null;
            }
        };

        int ans = ibs.binarySearch(0, limit);
        check(ans);
        out.println(ans);
        int[] t = check(ans);

        for (int i = 0; i < t[0]; i++) {
            out.append('B');
        }
        for (int i = 0; i < t[1]; i++) {
            out.append('N');
        }
    }

    int[][] pts;
    int limit = (int) 5e5;

    int inf = (int) 1e8;
    IntegerMinQueue topSelfMinQueue = new IntegerMinQueue(limit + 1, IntegerComparator.NATURE_ORDER);
    IntegerMinQueue topSolidMinQueue = new IntegerMinQueue(limit + 1, IntegerComparator.NATURE_ORDER);
    IntegerMinQueue botSelfMinQueue = new IntegerMinQueue(limit + 1, IntegerComparator.REVERSE_ORDER);
    IntegerMinQueue botSolidMinQueue = new IntegerMinQueue(limit + 1, IntegerComparator.REVERSE_ORDER);

    public int[] check(int mid) {
        topSelfMinQueue.reset();
        topSolidMinQueue.reset();
        botSelfMinQueue.reset();
        botSolidMinQueue.reset();

        int l = pts[pts.length - 1][0] - mid;
        int r = pts[0][0] + mid;

        Range2DequeAdapter<int[]> dq1 = new Range2DequeAdapter<>(i -> pts[i], 0, pts.length - 1);
        Range2DequeAdapter<int[]> dq2 = new Range2DequeAdapter<>(i -> pts[i], 0, pts.length - 1);
        for (int i = 0; i <= limit; i++) {
            while (!dq1.isEmpty() && dq1.peekFirst()[0] - mid <= i) {
                int[] head = dq1.removeFirst();
                int y = head[1] + mid;
                int x = head[0];
                topSelfMinQueue.addLast(y - x);
                botSolidMinQueue.addLast(head[1] - mid);
            }
            while (!dq2.isEmpty() && dq2.peekFirst()[0] <= i) {
                topSelfMinQueue.removeFirst();
                botSolidMinQueue.removeFirst();

                int[] head = dq2.removeFirst();
                int y = head[1] - mid;
                int x = head[0];
                botSelfMinQueue.addLast(y - x);
                topSolidMinQueue.addLast(head[1] + mid);
            }
            if (l <= i && i <= r) {
                int top = Math.min(topSelfMinQueue.min() + i, topSolidMinQueue.min());
                int bot = Math.max(botSelfMinQueue.min() + i, botSolidMinQueue.min());
                bot = Math.max(bot, 0);
                if (bot <= top && (i + top > 0)) {
                    return new int[]{i, top};
                }
            }
        }

        return null;
    }
}
