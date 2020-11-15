package on2020_11.on2020_11_14_CSES___CSES_Problem_Set.Robot_Path0;




import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongArrayList;
import template.utils.CollectionUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        int[] dx = new int[128];
        int[] dy = new int[128];
        dx['L'] = -1;
        dx['R'] = 1;
        dy['U'] = 1;
        dy['D'] = -1;
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
            line.x2 = line.x1 = curX;
            line.y2 = line.y1 = curY;
            if (i > 0) {
                line.x1 += dx[c];
                line.y1 += dy[c];
            }
            line.x2 += dx[c] * d;
            line.y2 += dy[c] * d;
            if (inv[c] == last) {
                rev = true;
            }
            last = c;
            if (!rev) {
                sum += d;
            }
            if(c == 'L' || c == 'R'){
                hs.add(line);
            }else {
                vs.add(line);
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
        TreeSet<Integer>[] pqByLine = new TreeSet[m];
        for (int i = 0; i < m; i++) {
            pqByLine[i] = new TreeSet<>();
        }
        List<Line> hsSortByL = new ArrayList<>(hs);
        List<Line> hsSortByR = new ArrayList<>(hs);
        hsSortByL.sort((a, b) -> -Integer.compare(a.left, b.left));
        hsSortByR.sort((a, b) -> -Integer.compare(a.right, b.right));
        List<Line> vsSortByL = new ArrayList<>(vs);
        vsSortByL.sort((a, b) -> -Integer.compare(a.left, b.left));

        int smallest = n;
        Segment seg = new Segment(0, m);
        for (int x = 0; x < m; x++) {
            while (!hsSortByR.isEmpty() && CollectionUtils.peek(hsSortByR).right < x) {
                //delete
                Line line = CollectionUtils.pop(hsSortByR);
                pqByLine[ line.top].remove(line.id);
                seg.update( line.top,  line.top, 0, m, pqByLine[ line.top].isEmpty() ? Segment.inf : pqByLine[(int) line.y1].first());
            }
            while (!hsSortByL.isEmpty() && CollectionUtils.peek(hsSortByL).left <= x) {
                //add
                Line line = CollectionUtils.pop(hsSortByL);
                if (!pqByLine[ line.top].isEmpty()) {
                    int cand = pqByLine[ line.top].first();
                    smallest = Math.min(smallest, Math.max(cand, line.id));
                }
                pqByLine[(int) line.top].add(line.id);
                seg.update((int) line.top, (int) line.top, 0, m, pqByLine[(int) line.top].first());
            }

            while (!vsSortByL.isEmpty() && CollectionUtils.peek(vsSortByL).left == x) {
                Line line = CollectionUtils.pop(vsSortByL);
                int cand = seg.query((int) line.bot, (int) line.top, 0, m);
                smallest = Math.min(smallest, Math.max(cand, line.id));
            }
        }

        for (int i = 0; i < m; i++) {
            pqByLine[i].clear();
        }
        List<Line> vsSortByBot = new ArrayList<>(vs);
        List<Line> vsSortByTop = new ArrayList<>(vs);
        vsSortByBot.sort((a, b) -> -Integer.compare(a.bot, b.bot));
        vsSortByTop.sort((a, b) -> -Integer.compare(a.top, b.top));
        for (int y = 0; y < m; y++) {

            while (!vsSortByTop.isEmpty() && CollectionUtils.peek(vsSortByTop).top < y) {
                //delete
                Line line = CollectionUtils.pop(vsSortByTop);
                pqByLine[ line.left].remove(line.id);
            }
            while (!vsSortByBot.isEmpty() && CollectionUtils.peek(vsSortByBot).bot <= y) {
                //add
                Line line = CollectionUtils.pop(vsSortByBot);
                if (!pqByLine[ line.left].isEmpty()) {
                    int cand = pqByLine[ line.left].first();
                    smallest = Math.min(smallest, Math.max(cand, line.id));
                }
                pqByLine[ line.left].add(line.id);
            }
        }

        for (Line line : lines) {
            line.x1 = xs.get((int) line.x1);
            line.x2 = xs.get((int) line.x2);
            line.y1 = ys.get((int) line.y1);
            line.y2 = ys.get((int) line.y2);
        }
        debug.debug("smallest", smallest);
        long ans = -1;
        if (smallest >= n) {
            for (Line line : lines) {
                ans += line.length();
            }
            out.println(ans);
            return;
        }

        Line smallestLine = lines[smallest];
        if (smallestLine.left == smallestLine.right) {
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
                {
                    int tmp = line.right;
                    line.right = line.top;
                    line.top = tmp;
                }
                {
                    int tmp = line.left;
                    line.left = line.bot;
                    line.bot = tmp;
                }
            }
        }
        if (smallestLine.x1 > smallestLine.x2) {
            for (Line line : lines) {
                line.x1 = -line.x1;
                line.x2 = -line.x2;

                int tmp = line.left;
                line.left = -line.right;
                line.right = -tmp;
            }
        }
        for (Line line : lines) {
            line.recalc();
        }

        long retain = smallestLine.length();
        for (Line line : lines) {
            if (line.id >= smallestLine.id) {
                break;
            }
            ans += line.length();
            if (line.left == line.right) {
                //v
                if (line.bot <= smallestLine.bot && line.top >= smallestLine.top &&
                        line.left >= smallestLine.left && line.right <= smallestLine.right) {
                    retain = Math.min(retain, Line.dist(smallestLine.x1, smallestLine.y1, line.x1, smallestLine.y1));
                }
            } else {
                //h
                if (line.top == smallestLine.top && smallestLine.x2 >= line.left && line.left >= smallestLine.x1) {
                    retain = Math.min(retain, Line.dist(smallestLine.x1, smallestLine.y1, Math.min(line.x1, line.x2), smallestLine.y1));
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
    int left;
    int right;
    int bot;
    int top;

    public void recalc() {
        left = (int) Math.min(x1, x2);
        right = (int) Math.max(x1, x2);
        top = (int) Math.max(y1, y2);
        bot = (int) Math.min(y1, y2);
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
