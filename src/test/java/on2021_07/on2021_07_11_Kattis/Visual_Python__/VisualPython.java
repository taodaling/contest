package on2021_07.on2021_07_11_Kattis.Visual_Python__;



import template.datastructure.MultiWayDeque;
import template.datastructure.MultiWayStack;
import template.datastructure.SegTree;
import template.datastructure.summary.BracketSum;
import template.datastructure.summary.BracketUpdate;
import template.geometry.geo2.IntegerRect2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.CollectionUtils;
import template.utils.Debug;

import java.util.*;

public class VisualPython {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Corner[] corners = new Corner[n * 2];
        IntegerArrayList ys = new IntegerArrayList(n * 2);
        IntegerArrayList xs = new IntegerArrayList(n * 2);
        for (int i = 0; i < n; i++) {
            corners[i] = new Corner(in.ri(), in.ri(), true);
            corners[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            corners[i + n] = new Corner(in.ri(), in.ri(), false);
            corners[i + n].id = i;
        }
        for (Corner c : corners) {
            c.y *= 2;
            c.x *= 2;
            if (!c.left) {
                c.y++;
                c.x++;
            }
            ys.add(c.y);
            xs.add(c.x);
        }
        ys.unique();
        xs.unique();
        Arrays.sort(corners, Comparator.<Corner>comparingInt(x -> x.x)
                .thenComparingInt(x -> x.y));
        TreeMap<Integer, Corner> map = new TreeMap<>();
        List<Corner[]> rects = new ArrayList<>(n);
        for (Corner c : corners) {
            if (c.left) {
                map.put(c.y, c);
            } else {
                Corner floor = CollectionUtils.floorValue(map, c.y);
                if (floor != null) {
                    map.remove(floor.y);
                    rects.add(new Corner[]{floor, c});
                }
            }
        }
        for (Corner[] pair : rects) {
            debug.debug("pair", pair);
        }
        if (rects.size() < n) {
            out.println("syntax error");
            return;
        }

        rects.sort(Comparator.<Corner[]>comparingLong(x -> (long) x[1].y - x[0].y).reversed());
        int xTotal = xs.size();
        int yTotal = ys.size();
        SegTree<BracketSum, BracketUpdate> st = new SegTree<>(0, yTotal - 1, BracketSum::new,
                BracketUpdate::new, i -> new BracketSum());
        MultiWayDeque<Event> events = new MultiWayDeque<>(xTotal, n * 2);
        for (Corner[] pair : rects) {
            int l = ys.binarySearch(pair[0].y);
            int r = ys.binarySearch(pair[1].y);
            int L = xs.binarySearch(pair[0].x);
            int R = xs.binarySearch(pair[1].x);
            events.addFirst(L, new Event(l, r, true));
            events.addLast(R, new Event(l, r, false));
        }

        BracketSum sum = new BracketSum();
        for (int i = 0; i < xTotal; i++) {
            for (Event e : events.queue(i)) {
                sum.init();
                st.query(e.l + 1, e.r - 1, 0, yTotal - 1, sum);
                if (!sum.isEmpty()) {
                    out.println("syntax error");
                    return;
                }
                if (e.add) {
                    st.update(e.l, e.l, 0, yTotal - 1, BracketUpdate.ofAdd(1));
                    st.update(e.r, e.r, 0, yTotal - 1, BracketUpdate.ofAdd(-1));
                } else {
                    st.update(e.l, e.l, 0, yTotal - 1, BracketUpdate.ofDel(1));
                    st.update(e.r, e.r, 0, yTotal - 1, BracketUpdate.ofDel(-1));
                }
                if (!st.sum.isValid()) {
                    out.println("syntax error");
                    return;
                }
            }
        }

        int[] res = new int[n];
        for (Corner[] pair : rects) {
            res[pair[0].id] = pair[1].id;
        }
        for (int x : res) {
            out.println(x + 1);
        }
    }

    Debug debug = new Debug(false);
}

class Event {
    int l;
    int r;
    boolean add;

    public Event(int l, int r, boolean add) {
        this.l = l;
        this.r = r;
        this.add = add;
    }
}

class Corner {
    int x;
    int y;
    boolean left;
    int id;

    public Corner(int x, int y, boolean left) {
        this.x = x;
        this.y = y;
        this.left = left;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
