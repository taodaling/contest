package contest;


import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import template.DigitUtils;
import template.FastInput;
import template.PermutationUtils;
import template.SequenceUtils;

public class AntsonaCircle {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        long l = in.readInt();
        long t = in.readInt();
        l *= 4;
        t = t * 4 + 1;

        Ant[] ants = new Ant[n];
        for (int i = 0; i < n; i++) {
            ants[i] = new Ant();
            ants[i].x = in.readLong() * 4;
            ants[i].w = in.readInt();
            ants[i].id = i;
            ants[i].label = i;
        }

        boolean allSameDirection = true;
        for (int i = 1; i < n; i++) {
            allSameDirection = allSameDirection && ants[i].w == ants[i - 1].w;
        }

        for (int i = 0; i < n; i++) {
            long x = ants[i].x;
            if (ants[i].w == 1) {
                x += t;
            } else {
                x -= t;
            }
            ants[i].y = DigitUtils.mod(x, l);
        }

        int diffIndex = n;
        for (int i = 1; i < n; i++) {
            if (ants[i].w == 2 && ants[i - 1].w == 1) {
                diffIndex = i;
                break;
            }
        }
        if (diffIndex == n) {
            diffIndex = 0;
        }

        Deque<Ant> cw = new ArrayDeque<>(2 * n);
        Deque<Ant> ccw = new ArrayDeque<>(2 * n);
        for (int i = 0; i < n; i++) {
            Ant ant = ants[DigitUtils.mod(diffIndex + i, n)];
            if (ant.w == 2) {
                ccw.addLast(ant);
            }
        }

        for (int i = 0; i < n; i++) {
            Ant ant = ants[DigitUtils.mod(diffIndex - i, n)];
            if (ant.w == 1) {
                cw.addLast(ant);
            }
        }

        int antId = antOn(new ArrayDeque<>(ccw), new ArrayDeque<>(cw), l, l).id;
        Ant mod = antOn(new ArrayDeque<>(ccw), new ArrayDeque<>(cw), t % l, l);
        Arrays.sort(ants, (a, b) -> Long.compare(a.y, b.y));
        int diffLabel = DigitUtils.mod(diffIndex + (diffIndex - antId) * (t / l), n);
        int index = SequenceUtils.indexOf(ants, 0, n - 1, mod);

        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            int label = DigitUtils.mod((i - index) + diffLabel, n);
            ans[label] = position(ants[i].y, ants[i].w, l);
        }

        for (int i = 0; i < n; i++) {
            out.println(ans[i]);
        }
    }

    public int position(long y, int w, long l) {
        if (w == 1) {
            y -= w;
        } else {
            y += w;
        }
        y /= 4;
        return (int) DigitUtils.mod(y, l / 4);
    }

    public Ant antOn(Deque<Ant> ccw, Deque<Ant> cw, long t, long l) {
        long totalMove = t * 2;
        while (!ccw.isEmpty() && !cw.isEmpty()) {
            long distBetween = DigitUtils.mod(ccw.peekFirst().x - (t * 2 - totalMove) - cw.peekFirst().x, l);
            if (distBetween == 0) {
                distBetween = l;
            }
            if (distBetween > totalMove) {
                return ccw.removeFirst();
            }
            totalMove -= distBetween;
            ccw.addLast(ccw.removeFirst());

            distBetween = DigitUtils.mod(ccw.peekFirst().x - (t * 2 - totalMove) - cw.peekFirst().x, l);
            if (distBetween == 0) {
                distBetween = l;
            }
            if (distBetween > totalMove) {
                return cw.removeFirst();
            }
            cw.addLast(cw.removeFirst());
            totalMove -= distBetween;
        }

        return ccw.isEmpty() ? cw.peekFirst() : ccw.peekFirst();
    }
}


class Ant {
    int id;
    long x;
    int w;
    int label;
    long y;
}
