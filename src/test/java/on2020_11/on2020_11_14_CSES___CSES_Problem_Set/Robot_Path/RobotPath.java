package on2020_11.on2020_11_14_CSES___CSES_Problem_Set.Robot_Path;



import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class RobotPath {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Line[] lines = new Line[n];
        long curX = 0;
        long curY = 0;

        int[] inv = new int[128];
        inv['U'] = 'D';
        inv['D'] = 'U';
        inv['L'] = 'R';
        inv['R'] = 'L';
        List<Line> hs = new ArrayList<>(n);
        List<Line> vs = new ArrayList<>(n);
        LongArrayList xs = new LongArrayList(n * 2);
        LongArrayList ys = new LongArrayList(n * 2);
        long sum = 0;
        boolean rev = false;
        int last = 0;
        for (int i = 0; i < n; i++) {
            char c = in.readChar();
            int d = in.readInt();
            Line line = new Line();
            line.x1 = curX;
            line.y1 = curY;
            if (c == 'U') {
                line.y1++;
                line.x2 = curX;
                line.y2 = curY + d;
                vs.add(line);
            } else if (c == 'D') {
                line.y1--;
                line.x2 = curX;
                line.y2 = curY - d;
                vs.add(line);
            } else if (c == 'L') {
                line.x1--;
                line.x2 = curX - d;
                line.y2 = curY;
                hs.add(line);
            } else if (c == 'R') {
                line.x1++;
                line.x2 = curX + d;
                line.y2 = curY;
                hs.add(line);
            }
            if (i == 0) {
                line.x1 = curX;
                line.y1 = curY;
            }
            if (inv[c] == last) {
                rev = true;
            }
            last = c;
            if (!rev) {
                sum += d;
            }
            line.id = i;
            lines[i] = line;
            curX = line.x2;
            curY = line.y2;
            xs.add(line.x1);
            xs.add(line.x2);
            ys.add(line.y1);
            ys.add(line.y2);
        }

        xs.unique();
        ys.unique();
        for (Line line : lines) {
            line.x1 = xs.binarySearch(line.x1);
            line.x2 = xs.binarySearch(line.x2);
            line.y1 = ys.binarySearch(line.y1);
            line.y2 = ys.binarySearch(line.y2);

            line.recalc();
        }

        int m = Math.max(xs.size(), ys.size());
        TreeSet<Line>[] pqByLine = new TreeSet[m];
        for (int i = 0; i < m; i++) {
            pqByLine[i] = new TreeSet<>(Line.sortById);
        }
        List<Line> hsSortByL = hs;
        List<Line> hsSortByR = new ArrayList<>(hs);
        hsSortByL.sort((a, b) -> Long.compare(a.left, b.left));
        hsSortByR.sort((a, b) -> Long.compare(a.right, b.right));
        vs.sort((a, b) -> Long.compare(a.x1, b.x1));

        Line smallest = null;
        Segment seg = new Segment(0, m);
        SimplifiedDeque<Line> dqHL = new Range2DequeAdapter<>(i -> hsSortByL.get(i), 0, hsSortByL.size() - 1);
        SimplifiedDeque<Line> dqHR = new Range2DequeAdapter<>(i -> hsSortByR.get(i), 0, hsSortByR.size() - 1);
        SimplifiedDeque<Line> dqV = new Range2DequeAdapter<>(i -> vs.get(i), 0, vs.size() - 1);
        for (int x = 0; x < m; x++) {
            while (!dqHR.isEmpty() && dqHR.peekFirst().right < x) {
                //delete
                Line line = dqHR.removeFirst();
                pqByLine[(int)line.y1].remove(line);
                seg.update((int)line.y1, (int)line.y1, 0, m, pqByLine[(int)line.y1].isEmpty() ? Segment.inf : pqByLine[(int)line.y1].first().id);
            }
            while (!dqHL.isEmpty() && dqHL.peekFirst().left <= x) {
                //add
                Line line = dqHL.removeFirst();
                if (!pqByLine[(int)line.y1].isEmpty()) {
                    Line cand = pqByLine[(int)line.y1].first();
                    Line end = cand.id > line.id ? cand : line;
                    if (smallest == null || smallest.id > end.id) {
                        smallest = end;
                    }
                }
                pqByLine[(int)line.y1].add(line);
                seg.update((int)line.y1, (int)line.y1, 0, m, pqByLine[(int)line.y1].first().id);
            }

            while (!dqV.isEmpty() && dqV.peekFirst().left == x) {
                Line line = dqV.removeFirst();
                int id = seg.query((int)line.bot, (int)line.top, 0, m);
                if (id < n) {
                    //find
                    Line cand = lines[id];
                    Line end = cand.id > line.id ? cand : line;
                    if (smallest == null || smallest.id > end.id) {
                        smallest = end;
                    }
                }
            }
        }

        for (int i = 0; i < m; i++) {
            pqByLine[i].clear();
        }
        List<Line> vsSortByBot = vs;
        List<Line> vsSortByTop = new ArrayList<>(vs);
        vsSortByBot.sort((a, b) -> Long.compare(a.bot, b.bot));
        vsSortByTop.sort((a, b) -> Long.compare(a.top, b.top));
        SimplifiedDeque<Line> dqVB = new Range2DequeAdapter<>(i -> vsSortByBot.get(i), 0, vsSortByBot.size() - 1);
        SimplifiedDeque<Line> dqVT = new Range2DequeAdapter<>(i -> vsSortByTop.get(i), 0, vsSortByTop.size() - 1);
        for (int y = 0; y < m; y++) {

            while (!dqVT.isEmpty() && dqVT.peekFirst().top < y) {
                //delete
                Line line = dqVT.removeFirst();
                pqByLine[(int)line.x1].remove(line);
            }
            while (!dqVB.isEmpty() && dqVB.peekFirst().bot <= y) {
                //add
                Line line = dqVB.removeFirst();
                if (!pqByLine[(int)line.x1].isEmpty()) {
                    Line cand = pqByLine[(int)line.x1].first();
                    Line end = cand.id > line.id ? cand : line;
                    if (smallest == null || smallest.id > end.id) {
                        smallest = end;
                    }
                }
                pqByLine[(int)line.x1].add(line);
            }
        }

        for (Line line : lines) {
            line.x1 = xs.get((int)line.x1);
            line.x2 = xs.get((int)line.x2);
            line.y1 = ys.get((int)line.y1);
            line.y2 = ys.get((int)line.y2);
        }
        debug.debug("smallest", smallest);
        long ans = -1;
        if (smallest == null) {
            for (Line line : lines) {
                ans += line.length();
            }
            out.println(ans);
            return;
        }

        if (smallest.left == smallest.right) {
            for (Line line : lines) {
                {
                    long tmp = line.x1;
                    line.x1 = line.y1;
                    line.y1 = tmp;
                }
                {
                    long tmp = line.x2;
                    line.x2 = line.y2;
                    line.y2 = tmp;
                }
            }
        }
        if (smallest.x1 > smallest.x2) {
            for (Line line : lines) {
                line.x1 = -line.x1;
                line.x2 = -line.x2;
            }
        }
        for(Line line : lines){
            line.recalc();
        }

        long retain = smallest.length();
        for (Line line : lines) {
            if (line.id >= smallest.id) {
                break;
            }
            ans += line.length();
            if (line.left == line.right) {
                //v
                if (line.bot <= smallest.bot && line.top >= smallest.top &&
                        line.left >= smallest.left && line.right <= smallest.right) {
                    retain = Math.min(retain, Line.dist(smallest.x1, smallest.y1, line.x1, smallest.y1));
                }
            } else {
                //h
                if (line.top == smallest.top && smallest.x2 >= line.left && line.left >= smallest.x1) {
                    retain = Math.min(retain, Line.dist(smallest.x1, smallest.y1, line.left, smallest.y1));
                }
            }
        }

        ans += retain;
        debug.debug("retain", retain);
        out.println(Math.min(sum, ans));
    }
}

class Line {
    long x1;
    long y1;
    long x2;
    long y2;
    int id;
    long left;
    long right;
    long bot;
    long top;
    
    public void recalc(){
        left = Math.min(x1, x2);
        right = Math.max(x1, x2);
        top = Math.max(y1, y2);
        bot = Math.min(y1, y2);
    }
    

    public static long dist(long x1, long y1, long x2, long y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2) + 1;
    }

    public long length() {
        return dist(x1, y1, x2, y2);
    }
    
    static Comparator<Line> sortById = (a, b) -> Integer.compare(a.id, b.id);

    @Override
    public String toString() {
        return String.format("%d[(%d, %d) - (%d, %d)]", id, x1, y1, x2, y2);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int min;
    static int inf = (int) 1e9;

    private void modify(int x) {
        min = x;
    }


    public void pushUp() {
        min = Math.min(left.min, right.min);
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            min = inf;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }


    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return inf;
        }
        if (covered(ll, rr, l, r)) {
            return min;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.query(ll, rr, l, m),
                right.query(ll, rr, m + 1, r));
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(min).append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
