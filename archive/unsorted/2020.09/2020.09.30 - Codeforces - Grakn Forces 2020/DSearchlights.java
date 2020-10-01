package contest;

import template.datastructure.MultiWayDeque;
import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.math.DigitUtils;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class DSearchlights {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();

        Pt[] pts = new Pt[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Pt(in.readInt(), in.readInt());
        }

        Pt[] spots = new Pt[m];
        for (int i = 0; i < m; i++) {
            spots[i] = new Pt(in.readInt(), in.readInt());
        }

        spots = dec(spots);

        int limit = (int) 1e6 + 1;

        int[] max = new int[limit + 1];
        for (Pt pt : pts) {
            int last = 0;
            for (int j = 0; j < spots.length; j++) {
                Pt spot = spots[j];
                int event = spot.x + 1 - pt.x;
                if (event <= 0) {
                    continue;
                }
                int l = last;
                int r = event - 1;
                int cost = spot.y + 1 - pt.y;
                max[r] = Math.max(max[r], cost);
                last = r + 1;
            }
        }

        for(int i = max.length - 2; i >= 0; i--){
            max[i] = Math.max(max[i], max[i + 1]);
        }

        int ans = (int)1e9;
        for(int i = 0; i < max.length; i++){
            ans = Math.min(ans, max[i] + i);
        }

        out.println(ans);
    }

    public Pt[] dec(Pt[] spots) {
        Arrays.sort(spots, (a, b) -> Integer.compare(a.x, b.x));
        Deque<Pt> dq = new ArrayDeque<>();
        for (Pt pt : spots) {
            while (!dq.isEmpty() && dq.peekLast().y < pt.y) {
                dq.removeLast();
            }
            if (!dq.isEmpty() && dq.peekLast().x == pt.x) {
                continue;
            }
            dq.addLast(pt);
        }
        return dq.toArray(new Pt[0]);
    }
}

class Pt {
    int x;
    int y;


    public Pt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}

