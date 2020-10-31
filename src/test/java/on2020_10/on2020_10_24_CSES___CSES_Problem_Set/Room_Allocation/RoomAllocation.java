package on2020_10.on2020_10_24_CSES___CSES_Problem_Set.Room_Allocation;



import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.utils.CompareUtils;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class RoomAllocation {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        Customer[] customers = new Customer[n];
        for (int i = 0; i < n; i++) {
            customers[i] = new Customer();
            customers[i].l = in.readInt();
            customers[i].r = in.readInt();
        }
        Customer[] enter = customers.clone();
        Customer[] leave = customers.clone();
        CompareUtils.radixSortIntObject(enter, 0, n - 1, x -> x.l);
        CompareUtils.radixSortIntObject(leave, 0, n - 1, x -> x.r);
        SimplifiedDeque<Customer> enterDq = new Range2DequeAdapter<>(i -> enter[i], 0, n - 1);
        SimplifiedDeque<Customer> leaveDq = new Range2DequeAdapter<>(i -> leave[i], 0, n - 1);
        IntegerDequeImpl dq = new IntegerDequeImpl(n);
        int roomId = 1;
        while (!enterDq.isEmpty() || !leaveDq.isEmpty()) {
            if (!enterDq.isEmpty() && enterDq.peekFirst().l <= leaveDq.peekFirst().r) {
                Customer head = enterDq.removeFirst();
                if (dq.isEmpty()) {
                    dq.addLast(roomId++);
                }
                head.room = dq.removeLast();
            } else {
                dq.addLast(leaveDq.removeFirst().room);
            }
        }
        out.println(roomId - 1);
        for (Customer c : customers) {
            out.println(c.room);
        }
    }
}

class Customer {
    int l;
    int r;
    int room;
}