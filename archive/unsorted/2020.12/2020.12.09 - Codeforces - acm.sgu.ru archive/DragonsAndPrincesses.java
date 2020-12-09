package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class DragonsAndPrincesses {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        PriorityQueue<Dragon> pq = new PriorityQueue<>(n, Comparator.comparingInt(x -> x.w));
        for (int i = 0; i < n - 2; i++) {
            char c = in.rc();
            if (c == 'd') {
                pq.add(new Dragon(i, in.ri()));
            } else {
                int b = in.ri();
                while (pq.size() >= b) {
                    pq.remove();
                }
            }
        }
        in.rc();
        int req = in.ri();
        if (req > pq.size()) {
            out.println(-1);
            return;
        }
        List<Dragon> list = new ArrayList<>(pq);
        list.sort(Comparator.comparingInt(x -> x.index));
        out.println(list.stream().mapToLong(x -> x.w).sum());
        out.println(list.size());
        for(Dragon d : list){
            out.append(d.index + 2).append(' ');
        }
    }
}

class Dragon {
    int index;
    int w;

    public Dragon(int index, int w) {
        this.index = index;
        this.w = w;
    }
}
