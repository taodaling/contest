package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerPriorityQueue;

import java.util.Comparator;
import java.util.PriorityQueue;

public class StickDivisions {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        IntegerPriorityQueue pq = new IntegerPriorityQueue(n, IntegerComparator.NATURE_ORDER, i -> a[i]);
        long ans = 0;
        while(pq.size() >= 2){
            int head = pq.pop();
            int head2 = pq.pop();
            ans += head + head2;
            pq.add(head + head2);
        }
        out.println(ans);x
    }
}
