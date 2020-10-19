package contest;

import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerObjectHashMap;
import template.primitve.generated.datastructure.IntegerPreSum;

import java.io.PrintWriter;
import java.util.*;

public class HRainbowTriples {

    int n;

    public int[] newInterval() {
        return new int[]{-1, n + 1};
    }

    public void updateL(int[] lr, int l) {
        lr[0] = Math.max(lr[0], l);
    }

    public void updateR(int[] lr, int r) {
        lr[1] = Math.min(lr[1], r);
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        IntegerPreSum ips = new IntegerPreSum(i -> a[i] == 0 ? 1 : 0,
                n);
        int cnt = ips.prefix(n);
        int half = cnt / 2;
        if (cnt < 2) {
            out.println(0);
            return;
        }

        Map<Integer, int[]> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (a[i] == 0) {
                continue;
            }
            int[] range = map.get(a[i]);
            if (range == null) {
                range = newInterval();
                map.put(a[i], range);
            }
            if (ips.prefix(i) >= half) {
                updateR(range, i);
            }
            if (ips.post(i) >= half) {
                updateL(range, i);
            }
        }

        List<int[]> list = new ArrayList<>(map.values());
        list.sort(Comparator.comparingInt(x -> x[0]));
        int head = 0;

        List<int[]> right = new ArrayList<>(n);
        PriorityQueue<int[]> added = new PriorityQueue<>(n,
                (x, y) -> Integer.compare(x[1], y[1]));
        for (int i = -1; i <= n + 1; i++) {
            int size = Math.min(half, ips.prefix(i));
            while (added.size() < size && head < list.size()) {
                added.add(list.get(head++));
            }
            while (head < list.size() && list.get(head)[0] == i) {
                int[] top = list.get(head++);
                if (!added.isEmpty() && added.peek()[1] < top[1]) {
                    right.add(added.remove());
                    added.add(top);
                } else {
                    right.add(top);
                }
            }
        }

        right.sort((x, y) -> -Integer.compare(x[1], y[1]));
        int curSize = 0;
        head = 0;
        for (int i = n + 1; i >= -1; i--) {
            int size = Math.min(half, ips.post(i));
            while (curSize < size && head < right.size()) {
                head++;
                curSize++;
            }
            while (head < right.size() && right.get(head)[1] == i) {
                head++;
            }
        }

        int ans = curSize + added.size();
        ans = Math.min(ans, half);
        out.println(ans);
    }
}
