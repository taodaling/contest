package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;

public class FOrchestra {

    Debug debug = new Debug(true);
    long sum;

    void contribute(Point pt, int sign) {
        sum += sign * pt.weight * pt.target.y;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int ptCnt = in.ri();
        int k = in.ri();
        Point[] pts = new Point[ptCnt];
        for (int i = 0; i < ptCnt; i++) {
            pts[i] = new Point();
            pts[i].x = in.ri() - 1;
            pts[i].y = in.ri() - 1;
        }
        Arrays.sort(pts, Comparator.comparingInt(x -> x.y));
        Point dummyHead = new Point();
        Point dummyEnd = new Point();
        dummyHead.y = -1;
        dummyEnd.y = m;
        Point[] ptsSortedByX = pts.clone();
        Arrays.sort(ptsSortedByX, Comparator.comparingInt(x -> x.x));
        long ans = 0;
        for (int low = 0; low < n; low++) {
            long contrib = 0;
            sum = 0;
            Point head = dummyHead;
            Point end = dummyEnd;
            head.next = end;
            end.prev = head;
            end.next = end;
            head.weight = end.weight = 0;
            for (Point pt : pts) {
                pt.next = pt.prev = pt.target = null;
                pt.weight = 0;
                if (pt.x < low) {
                    continue;
                }
                pt.prev = end.prev;
                pt.prev.next = pt;
                pt.next = end;
                end.prev = pt;
            }
            head.target = head;
            for (int i = 1; i < k; i++) {
                head.target = head.target.next;
            }
            for (Point node = head.next; ; node = node.next) {
                node.target = node.prev.target.next;
                node.weight = node.y - node.prev.y;
                if (node == end) {
                    node.weight--;
                }
                contribute(node, 1);
                if (node == end) {
                    break;
                }
            }
            SimplifiedDeque<Point> dq = new Range2DequeAdapter<>(i -> ptsSortedByX[i], 0, ptsSortedByX.length - 1);
            for (int high = n - 1; high >= low; high--) {
                while (!dq.isEmpty() && dq.peekLast().x > high) {
                    Point pt = dq.removeLast();
                    if(k > 1) {
                        for (Point node = pt.prev; node != null; node = node.prev) {
                            Point prevEnd = node.target;
                            contribute(node, -1);
                            if (prevEnd == pt) {
                                break;
                            }
                        }
                    }
                    contribute(pt.next, -1);
                    contribute(pt, -1);
                    if(k > 1) {
                        for (Point node = pt.prev; node != null; node = node.prev) {
                            Point prevEnd = node.target;
                            node.target = prevEnd.next;
                            contribute(node, 1);
                            if (prevEnd == pt) {
                                break;
                            }
                        }
                    }
                    pt.next.weight += pt.weight;
                    contribute(pt.next, 1);
                    pt.next.prev = pt.prev;
                    pt.prev.next = pt.next;
                }
                contrib += (long) m * m - sum;
                debug.debug("low", low);
                debug.debug("high", high);
                debug.debug("contrib", m * m - sum);
            }
            ans += contrib;
        }
        out.println(ans);
    }
}

class Point {
    int x;
    int y;
    long weight;

    Point prev;
    Point next;
    Point target;

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}