package contest;

import template.*;

import java.util.*;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Deque<Long> deque = new ArrayDeque<>(n);
        long max = 0;
        for (int i = 0; i < n; i++) {
            deque.addLast(in.readLong());
            max = Math.max(max, deque.peekLast());
        }

        long time = 0;
        boolean stable = false;
        long[] queue = new long[n];
        int point = 0;

        Query[] qs = new Query[m];
        LongList ask = new LongList(m);
        for (int i = 0; i < m; i++) {
            qs[i] = new Query();
            qs[i].time = in.readLong();
            ask.add(qs[i].time);
        }


        ask.unique();
        Map<Long, long[]> ans = new HashMap<>();
        for (int i = 0; i < ask.size(); i++) {
            long t = ask.get(i);
            long elapse = t - time;
            time = t;

            if (!stable) {
                while (elapse > 1 && deque.peekFirst() != max) {
                    elapse--;
                    long a = deque.removeFirst();
                    long b = deque.removeFirst();
                    deque.addLast(Math.min(a, b));
                    deque.addFirst(Math.max(a, b));
                }
                if (deque.peekFirst() == max) {
                    for (int j = 0; j < n; j++) {
                        queue[j] = deque.removeFirst();
                    }
                    point = 0;
                    stable = true;
                } else {
                    long a = deque.removeFirst();
                    long b = deque.removeFirst();
                    deque.addLast(Math.min(a, b));
                    deque.addFirst(Math.max(a, b));
                    ans.put(t, SequenceUtils.wrapArray(a, b));
                    continue;
                }
            }

            point = (int) ((point + elapse - 1) % (n - 1));
            ans.put(t, SequenceUtils.wrapArray(queue[0], queue[point + 1]));
            point = (int) ((point + 1) % (n - 1));
        }

        for (Query q : qs) {
            long[] val = ans.get(q.time);
            out.append(val[0]).append(' ').append(val[1]).append('\n');
        }
    }
}

class Query {
    long time;
    long a;
    long b;
}