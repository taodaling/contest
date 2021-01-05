package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDeque;

import java.util.ArrayDeque;
import java.util.Deque;

public class AGennadyTheDentist {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Child[] children = new Child[n];
        for (int i = 0; i < n; i++) {
            children[i] = new Child();
            children[i].v = in.ri();
            children[i].d = in.ri();
            children[i].p = in.ri();
        }
        Deque<Integer> dq = new ArrayDeque<>(n);
        IntegerArrayList ans = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            if (children[i].p < 0) {
                continue;
            }
            ans.add(i);
            children[i].p = -1;
            long v = children[i].v;
            for (int j = i + 1; j < n; j++) {
                if (children[j].p < 0 && v > 0) {
                    continue;
                }
                children[j].p -= v;
                v--;
                if (children[j].p < 0) {
                    dq.add(j);
                }

                while (!dq.isEmpty()) {
                    int head = dq.removeFirst();
                    for (int t = head + 1; t < n; t++) {
                        if (children[t].p < 0) {
                            continue;
                        }
                        long dec = children[head].d;
                        children[t].p -= dec;
                        if (children[t].p < 0) {
                            dq.add(t);
                        }
                    }
                }
            }
        }

        out.println(ans.size());
        for(int x : ans.toArray()){
            out.append(x + 1).append(' ');
        }
    }
}

class Child {
    long v;
    long d;
    long p;
}
