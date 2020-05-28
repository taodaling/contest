package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.TreeSet;

public class BTShirtBuying {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Tshirt[] tshirts = new Tshirt[n];
        for (int i = 0; i < n; i++) {
            tshirts[i] = new Tshirt();
        }
        for (int i = 0; i < n; i++) {
            tshirts[i].p = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            tshirts[i].a = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            tshirts[i].b = in.readInt();
        }

        TreeSet<Tshirt>[] ts = new TreeSet[4];
        for (int i = 1; i <= 3; i++) {
            ts[i] = new TreeSet<>((a, b) -> Integer.compare(a.p, b.p));
        }
        for (int i = 0; i < n; i++) {
            ts[tshirts[i].a].add(tshirts[i]);
            ts[tshirts[i].b].add(tshirts[i]);
        }

        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            int c = in.readInt();
            if (ts[c].isEmpty()) {
                out.println(-1);
                continue;
            }
            Tshirt t = ts[c].pollFirst();
            ts[t.a].remove(t);
            ts[t.b].remove(t);

            out.println(t.p);
        }
    }
}

class Tshirt {
    int p;
    int a;
    int b;
}