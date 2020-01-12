package on2020_01.on2020_01_12_Mail_Ru_Cup_2018_Round_1.F__Electric_Scheme;



import template.graph.BipartiteMatching;
import template.graph.KMAlgo;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.IntegerList;
import template.primitve.generated.LongHashSet;
import template.rand.Randomized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FElectricScheme {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            pts[i] = new Point(x, y);
        }

        Map<Integer, List<Point>> ptsGroupByX = Arrays.stream(pts).collect(Collectors.groupingBy(x -> x.x));
        Map<Integer, List<Point>> ptsGroupByY = Arrays.stream(pts).collect(Collectors.groupingBy(x -> x.y));

        List<XLine> xLines = new ArrayList<>(n);
        List<YLine> yLines = new ArrayList<>(n);
        for (Map.Entry<Integer, List<Point>> entry : ptsGroupByY.entrySet()) {
            int y = entry.getKey();
            List<Point> list = entry.getValue();
            list.sort((a, b) -> a.x - b.x);
            for (int i = 1; i < list.size(); i++) {
                Point last = list.get(i - 1);
                Point next = list.get(i);
                if (last.x + 1 < next.x) {
                    XLine line = new XLine(last.x + 1, next.x - 1, y);
                    xLines.add(line);
                }
            }
        }

        for (Map.Entry<Integer, List<Point>> entry : ptsGroupByX.entrySet()) {
            int x = entry.getKey();
            List<Point> list = entry.getValue();
            list.sort((a, b) -> a.y - b.y);
            for (int i = 1; i < list.size(); i++) {
                Point last = list.get(i - 1);
                Point next = list.get(i);
                if (last.y + 1 < next.y) {
                    YLine line = new YLine(last.y + 1, next.y - 1, x);
                    yLines.add(line);
                }
            }
        }


        int xCnt = xLines.size();
        int yCnt = yLines.size();
        List<int[]> edges = new ArrayList<>(xCnt * yCnt);
        for (int i = 0; i < xCnt; i++) {
            for (int j = 0; j < yCnt; j++) {
                XLine xLine = xLines.get(i);
                YLine yLine = yLines.get(j);
                if (between(xLine.y, yLine.b, yLine.t) &&
                        between(yLine.x, xLine.l, xLine.r)) {
                    edges.add(new int[]{i, j});
                }
            }
        }

        BipartiteMatching match = new BipartiteMatching(xLines.size(), yLines.size(), edges.size());
        for (int[] e : edges) {
            match.addEdge(e[0], e[1]);
        }

        boolean[][] minVertexCover = match.minVertexCover();
        List<XLine> ansXLines = new ArrayList<>(n);
        List<YLine> ansYLines = new ArrayList<>(n);

        int cnt = 0;
        for (Map.Entry<Integer, List<Point>> entry : ptsGroupByY.entrySet()) {
            int y = entry.getKey();
            List<Point> list = entry.getValue();
            list.sort((a, b) -> a.x - b.x);
            int l, r;
            l = r = list.get(0).x;
            for (int i = 1; i < list.size(); i++) {
                Point last = list.get(i - 1);
                Point next = list.get(i);
                if (last.x + 1 < next.x) {
                    if (minVertexCover[0][cnt]) {
                        ansXLines.add(new XLine(l, r, y));
                        l = r = next.x;
                    } else {
                        r = next.x;
                    }
                    cnt++;
                } else {
                    r = next.x;
                }
            }
            ansXLines.add(new XLine(l, r, y));
        }

        cnt = 0;
        for (Map.Entry<Integer, List<Point>> entry : ptsGroupByX.entrySet()) {
            int x = entry.getKey();
            List<Point> list = entry.getValue();
            list.sort((a, b) -> a.y - b.y);
            int b, t;
            b = t = list.get(0).y;
            for (int i = 1; i < list.size(); i++) {
                Point last = list.get(i - 1);
                Point next = list.get(i);
                if (last.y + 1 < next.y) {
                    if (minVertexCover[1][cnt]) {
                        ansYLines.add(new YLine(b, t, x));
                        b = t = next.y;
                    } else {
                        t = next.y;
                    }
                    cnt++;
                }
                else{
                    t = next.y;
                }
            }
            ansYLines.add(new YLine(b, t, x));
        }

        out.println(ansXLines.size());
        for (XLine line : ansXLines) {
            out.append(line.l).append(' ').append(line.y).append(' ').append(line.r).append(' ').append(line.y).println();
        }
        out.println(ansYLines.size());
        for (YLine line : ansYLines) {
            out.append(line.x).append(' ').append(line.b).append(' ').append(line.x).append(' ').append(line.t).println();
        }
    }

    public boolean between(int a, int l, int r) {
        return l <= a && a <= r;
    }
}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class XLine {
    int l;
    int r;
    int y;

    public XLine(int l, int r, int y) {
        this.l = l;
        this.r = r;
        this.y = y;
    }

}

class YLine {
    int t;
    int b;
    int x;

    public YLine(int b, int t, int x) {
        this.t = t;
        this.b = b;
        this.x = x;
    }

}
