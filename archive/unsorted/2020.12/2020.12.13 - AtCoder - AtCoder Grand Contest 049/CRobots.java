package contest;

import template.algo.IntBinarySearch;
import template.datastructure.BinaryTree;
import template.datastructure.LinkedListBeta;
import template.datastructure.Treap;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.LongPreSum;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.*;

public class CRobots {
    IntegerBIT bit;

    public void clear(int i) {
        if (bit.query(i, i) == 1) {
            bit.update(i, -1);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = new long[n];
        long[] b = new long[n];
        in.populate(a);
        in.populate(b);

        Robot[] robots = new Robot[n];
        bit = new IntegerBIT(n);
        TreeSet<Robot> set = new TreeSet<>(Comparator.comparingLong(x -> x.index));
        List<Robot> deleted = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            robots[i] = new Robot();
            robots[i].id = i + 1;
            robots[i].index = (int) a[i];
            robots[i].left = a[i] - b[i];
            if (robots[i].left <= 0) {
                deleted.add(robots[i]);
            }
            bit.update(i + 1, 1);
            set.add(robots[i]);
        }
        {
            Robot dummy = new Robot();
            dummy.index = 0;
            dummy.left = 1;
            deleted.add(dummy);
        }
        PriorityQueue<Robot> pq = new PriorityQueue<>(n, Comparator.comparingLong(x -> -x.left));
        pq.addAll(Arrays.asList(robots));
        long ans = (long) 1e18;
        long now = 0;
        while (!pq.isEmpty() && pq.peek().left > now) {
            Robot head = pq.remove();
            clear(head.id);
            set.remove(head);
            while (true) {
                Robot floor = set.floor(head);
                if (floor == null || floor.index < head.left) {
                    break;
                }
                clear(floor.id);
                set.remove(floor);
            }
        }
        for (Robot robot : deleted) {
            long cost = 1 - robot.left;
            long local = cost + Math.max(0, bit.query(robot.id + 1, n) - cost);
            ans = Math.min(ans, local);
        }
        out.println(ans);

    }
}

class Robot {
    int id;
    int index;
    long left;
}
