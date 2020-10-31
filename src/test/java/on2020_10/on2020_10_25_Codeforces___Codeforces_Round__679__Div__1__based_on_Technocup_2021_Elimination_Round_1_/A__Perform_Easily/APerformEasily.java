package on2020_10.on2020_10_25_Codeforces___Codeforces_Round__679__Div__1__based_on_Technocup_2021_Elimination_Round_1_.A__Perform_Easily;



import template.datastructure.MultiSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;
import java.util.PriorityQueue;

public class APerformEasily {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] a = new int[6];
        in.populate(a);
        Arrays.sort(a);
        int n = in.readInt();
        int[] b = new int[n];
        in.populate(b);

        IntegerArrayList list = new IntegerArrayList(n * 6);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 6; j++) {
                list.add(b[i] - a[j]);
            }
        }
        list.unique();
        int[] indices = new int[n];
        MultiSet<Integer> set = new MultiSet<>();
        PriorityQueue<Event> pq = new PriorityQueue<>(n, (x, y) -> -Integer.compare(x.d, y.d));
        for (int i = 0; i < n; i++) {
            set.add(b[i] - a[0]);
            pq.add(new Event(b[i] - a[0], i));
        }
        long ans = (long) 1e18;
        boolean valid = true;
        for (int i = list.size() - 1; i >= 0 && valid; i--) {
            int threshold = list.get(i);
            while (valid && !pq.isEmpty() && pq.peek().d > threshold) {
                Event head = pq.remove();
                set.remove(b[head.index] - a[indices[head.index]]);
                indices[head.index]++;
                if (indices[head.index] >= 6) {
                    valid = false;
                } else {
                    head.d = b[head.index] - a[indices[head.index]];
                    set.add(head.d);
                    pq.add(head);
                }
            }
            if (valid) {
                ans = Math.min(ans, threshold - set.first());
            }
        }
        out.println(ans);
    }
}

class Event {
    int d;
    int index;

    public Event(int d, int index) {
        this.d = d;
        this.index = index;
    }
}