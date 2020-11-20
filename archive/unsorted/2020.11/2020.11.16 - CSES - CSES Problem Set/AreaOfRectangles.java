package contest;

import template.datastructure.ActiveSegment;
import template.datastructure.MultiWayStack;
import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.utils.Debug;

import java.util.Arrays;

public class AreaOfRectangles {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Rect[] rects = new Rect[n];
        IntegerArrayList ys = new IntegerArrayList(n * 2);
        // IntegerArrayList xs = new IntegerArrayList(n * 2);
        for (int i = 0; i < n; i++) {
            rects[i] = new Rect();
            rects[i].x1 = in.readInt();
            rects[i].y1 = in.readInt();
            rects[i].x2 = in.readInt();
            rects[i].y2 = in.readInt();
            ys.add(rects[i].y1);
            ys.add(rects[i].y2);
            // xs.add(rects[i].x1);
            //xs.add(rects[i].x2);
        }
        debug.elapse("read");

        //xs.unique();
        ys.unique();
//        debug.debug("ys", ys);
        for (Rect r : rects) {
            r.y1 = ys.binarySearch(r.y1);
            r.y2 = ys.binarySearch(r.y2);
        }
        debug.elapse("discrete");
        int minX = (int) -1e6;
        int maxX = (int) 1e6;
        MultiWayStack<Rect> left = new MultiWayStack<>(maxX - minX + 1, n);
        MultiWayStack<Rect> right = new MultiWayStack<>(maxX - minX + 1, n);
//        Rect[] sortByL = rects.clone();
//        Rect[] sortByR = rects.clone();
//        Arrays.sort(sortByL, (a, b) -> Integer.compare(a.x1, b.x1));
//        Arrays.sort(sortByR, (a, b) -> Integer.compare(a.x2, b.x2));
        for (Rect r : rects) {
            left.addLast(r.x1 - minX, r);
            right.addLast(r.x2 - minX, r);
        }
        debug.elapse("sort");
        long ans = 0;
        int L = 0;
        int R = ys.size() - 1;
        ActiveSegment as = new ActiveSegment(L, R);
        as.init(L, R, i -> i == R ? 0 : ys.get(i + 1) - ys.get(i));
        long total = as.query(L, R, L, R);
        debug.elapse("init segment");
        for (int x = 0; x <= maxX - minX; x++) {
            long contrib = (total - as.queryAll());
            ans += contrib;
            for (Rect head : left.getStack(x)) {
                //add
                as.update(head.y1, head.y2 - 1, L, R, 1);
//                debug.debug("add", head);
            }
            for (Rect head : right.getStack(x)) {
                //add
                as.update(head.y1, head.y2 - 1, L, R, -1);
//                debug.debug("add", head);
            }
//            debug.debug("x", x);
//            debug.debug("d", d);
//            debug.debug("seg", as);
//            debug.debug("contrib", contrib);
        }

        debug.elapse("solve");
        out.println(ans);
    }
}

class Rect {
    int x1;
    int y1;
    int x2;
    int y2;

    @Override
    public String toString() {
        return String.format("(%d, %d), (%d, %d)", x1, y1, x2, y2);
    }
}