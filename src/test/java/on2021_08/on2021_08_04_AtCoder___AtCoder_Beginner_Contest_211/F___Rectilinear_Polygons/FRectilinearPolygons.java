package on2021_08.on2021_08_04_AtCoder___AtCoder_Beginner_Contest_211.F___Rectilinear_Polygons;



import template.datastructure.XorBIT;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FRectilinearPolygons {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        List<Line> lineList = new ArrayList<>((int) 4e5);
        int n = in.ri();
        List<Line> inner = new ArrayList<>((int) 4e5);
        int L = (int) 2e5;
        XorBIT xorbit = new XorBIT(L);
        IntegerBIT coverBit = new IntegerBIT(L);
        for (int i = 0; i < n; i++) {
            inner.clear();
            int m = in.ri();
            int[] last = new int[]{in.ri(), in.ri()};
            int[] first = last;
            for (int j = 1; j < m; j++) {
                int[] cur = new int[]{in.ri(), in.ri()};
                if (cur[1] == last[1]) {
                    inner.add(new Line(cur[0], last[0], cur[1]));
                }
                last = cur;
            }
            int[] cur = first;
            if (cur[1] == last[1]) {
                inner.add(new Line(cur[0], last[0], cur[1]));
            }
            inner.sort(Comparator.comparingInt(x -> x.h));
            for (Line l : inner) {
                if (xorbit.query(l.l) == 0) {
                    l.v = 1;
                } else {
                    l.v = -1;
                }
                xorbit.update(l.l, l.r, 1);
            }

            for (Line l : inner) {
                xorbit.update(l.l, l.r, 1);
            }
            lineList.addAll(inner);

        }
        debug.debug("lineList", lineList);
        int q = in.ri();
        Pt[] pts = new Pt[q];
        for (int i = 0; i < q; i++) {
            pts[i] = new Pt(in.ri() + 1, in.ri());
        }
        Pt[] ptsSortedByY = pts.clone();
        Arrays.sort(ptsSortedByY, Comparator.comparingInt(x -> x.y));
        Line[] lines = lineList.toArray(new Line[0]);
        Arrays.sort(lines, Comparator.comparingInt(x -> x.h));
        int lIter = 0;
        for (Pt pt : ptsSortedByY) {
            while (lIter < lines.length && lines[lIter].h <= pt.y) {
                Line head = lines[lIter++];
                coverBit.update(head.l, head.r, head.v);
            }
            pt.ans = coverBit.query(pt.x);
        }
        for (Pt pt : pts) {
            out.println(pt.ans);
        }
    }

    Debug debug = new Debug(false);
}


class Pt {
    int x;
    int y;
    int ans;

    public Pt(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Line {
    int l;
    int r;
    int h;
    int v;

    public Line(int l, int r, int h) {
        if (l > r) {
            int tmp = l;
            l = r;
            r = tmp;
        }
        this.l = l + 1;
        this.r = r;
        this.h = h;
    }

    @Override
    public String toString() {
        return "Line{" +
                "l=" + l +
                ", r=" + r +
                ", h=" + h +
                ", v=" + v +
                '}';
    }
}
