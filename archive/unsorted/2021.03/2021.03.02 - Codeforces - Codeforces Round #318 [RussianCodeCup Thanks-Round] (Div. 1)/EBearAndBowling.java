package contest;

import template.algo.BlockSplit;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;

public class EBearAndBowling {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        Sum sum = new Sum();
        Update upd = new Update();
        BlockSplit<Sum, Update> bs = new BlockSplit<>(n, (int) Math.sqrt(n), (l, r) -> {
            Line[] lines = new Line[r - l + 1];
            for (int i = l; i <= r; i++) {
                Line line = new Line();
                line.a = a[i];
                line.b = a[i];
                line.id = i;
                lines[i - l] = line;
            }
            return new BlockImpl(lines);
        });
        long best = 0;
        long cur = 0;
        for (int i = 0; i < n; i++) {
            sum.clear();
            bs.query(0, n - 1, sum);
            debug.debug("sum", sum);
            cur += sum.maxVal;
            best = Math.max(best, cur);
            upd.clear();
            upd.del = true;
            bs.update(sum.index, upd);
            upd.clear();
            upd.modX = 1;
            bs.update(sum.index + 1, n - 1, upd);
            upd.clear();
            upd.modB = a[sum.index];
            bs.update(0, sum.index - 1, upd);
        }

        out.println(best);
    }
}


class Sum {
    int index;
    long maxVal;

    void clear() {
        index = -1;
        maxVal = Long.MIN_VALUE;
    }

    @Override
    public String toString() {
        return "(" + index + "," + maxVal + ")";
    }
}

class Update {
    long modX;
    long modB;
    boolean del;

    void clear() {
        modX = modB = 0;
        del = false;
    }
}

class Line {
    long a;
    long b;
    int id;
    boolean del;

    public long apply(long x) {
        return a * x + b;
    }
}

class BlockImpl implements BlockSplit.Block<Sum, Update> {
    Line[] lines;
    Line[] sorted;
    Deque<Line> choice;
    int x;
    long bDelta;

    boolean isUpConvex(Line a, Line b, Line c) {
        long bx = b.a - a.a;
        long by = b.b - a.b;
        long cx = c.a - a.a;
        long cy = c.b - a.b;
        return cx * by - bx * cy > 0;
    }

    void removeHead() {
        while (choice.size() >= 2) {
            Line first = choice.removeFirst();
            Line second = choice.peekFirst();
            if (first.apply(x) > second.apply(x)) {
                choice.addFirst(first);
                break;
            }
        }
    }

    void repopulate() {
        choice.clear();
        for (int i = 0; i < sorted.length; i++) {
            if (sorted[i].del) {
                continue;
            }
            int to = i;
            Line best = sorted[i];
            while (to + 1 < sorted.length && sorted[to + 1].a == sorted[i].a) {
                to++;
                if (!sorted[to].del && sorted[to].b > best.b) {
                    best = sorted[to];
                }
            }
            i = to;
            while (!choice.isEmpty() && choice.peekLast().b <= best.b) {
                choice.removeLast();
            }
            while (choice.size() >= 2) {
                Line second = choice.removeLast();
                Line first = choice.peekLast();
                if (isUpConvex(first, second, best)) {
                    choice.addLast(second);
                    break;
                }
            }
            choice.addLast(best);
        }

        removeHead();
    }

    public BlockImpl(Line[] lines) {
        this.lines = lines;
        this.sorted = lines.clone();
        Arrays.sort(sorted, Comparator.comparingLong(x -> x.a));
        choice = new ArrayDeque<>(lines.length);
        repopulate();
    }


    @Override
    public void fullyUpdate(Update upd) {
        if (upd.del) {
            partialUpdate(0, upd);
            return;
        }
        x += upd.modX;
        bDelta += upd.modB;
        removeHead();
    }

    @Override
    public void partialUpdate(int index, Update upd) {
        lines[index].b += upd.modB + upd.modX * lines[index].a;
        if (upd.del) {
            lines[index].del = true;
        }
    }

    @Override
    public void fullyQuery(Sum sum) {
        if (choice.isEmpty()) {
            return;
        }
        Line best = choice.peekFirst();
        long maxVal = best.apply(x) + bDelta;
        if (sum.maxVal < maxVal) {
            sum.maxVal = maxVal;
            sum.index = best.id;
        }
    }

    @Override
    public void partialQuery(int index, Sum sum) {
        if (lines[index].del) {
            return;
        }
        Line best = lines[index];
        long maxVal = best.apply(x) + bDelta;
        if (sum.maxVal < maxVal) {
            sum.maxVal = maxVal;
            sum.index = best.id;
        }
    }

    @Override
    public void afterPartialUpdate() {
        repopulate();
    }
}