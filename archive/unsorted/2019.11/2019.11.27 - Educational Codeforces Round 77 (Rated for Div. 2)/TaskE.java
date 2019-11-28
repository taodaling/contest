package contest;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Log2;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Log2 log2 = new Log2();
        int log = log2.ceilLog(n);
        Boxer[] a = new Boxer[n];
        int friend = -1;
        for (int i = 0; i < n; i++) {
            a[i] = new Boxer();
            a[i].salary = in.readInt();
            a[i].s = i;
            if (a[i].salary == -1) {
                friend = i;
            }
        }
        for (int i = 0; i < friend; i++) {
            a[i].salary = 0;
        }

        List<Boxer> boxers = new ArrayList<>(Arrays.asList(a));
        boxers.remove(friend);

        int r = boxers.size() - 1;
        PriorityQueue<Boxer> pq = new PriorityQueue<>((x, y) -> x.salary - y.salary);
        long total = 0;
        for (int i = 0; i < log; i++) {
            pq.add(boxers.get(r--));
            int canWin = 1 << (log - i - 1);
            canWin--;
            total += pq.remove().salary;

            for(int j = 0; j < canWin; j++){
                pq.add(boxers.get(r--));
            }
        }

        out.println(total);
    }
}

class Boxer {
    int salary;
    int s;
}
