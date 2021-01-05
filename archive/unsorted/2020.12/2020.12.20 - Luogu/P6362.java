package contest;

import template.datastructure.DSU;
import template.geometry.geo2.Point2;
import template.geometry.geo2.Triangulation;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.KahamSummation;
import template.primitve.generated.datastructure.LongEntryIterator;
import template.primitve.generated.datastructure.LongObjectEntryIterator;
import template.primitve.generated.datastructure.LongObjectHashMap;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.*;

public class P6362 {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Point2Ext[] pts = new Point2Ext[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2Ext(in.ri(), in.ri());
            pts[i].id = i;
        }
        Randomized.shuffle(pts);
        Triangulation tri = new Triangulation(n, 1e8);
        for (Point2 pt : pts) {
            tri.addPoint(pt);
        }
        LongObjectHashMap<Edge> map = new LongObjectHashMap<>(0, true);
        tri.forEach(triangle -> {
            for (int i = 0; i < 3; i++) {
                if (triangle.p[i].getClass() == Point2Ext.class && triangle.p[(i + 1) % 3].getClass() == Point2Ext.class) {
                    Point2Ext a = (Point2Ext) triangle.p[i];
                    Point2Ext b = (Point2Ext) triangle.p[(i + 1) % 3];
                    long sig = edgeId(a.id, b.id);
                    if (map.containKey(sig)) {
                        continue;
                    }
                    map.put(sig, new Edge(a.id, b.id, Point2.dist(a, b)));
                }
            }
        });
        List<Edge> edges = new ArrayList<>(map.size());
        for(LongObjectEntryIterator<Edge> iterator = map.iterator(); iterator.hasNext();){
            iterator.next();
            edges.add(iterator.getEntryValue());
        }
        edges.sort(Comparator.comparing(x -> x.dist));
        DSU dsu = new DSU(n);
        dsu.init();
        KahamSummation sum = new KahamSummation();
        for (Edge e : edges) {
            if (dsu.find(e.a) != dsu.find(e.b)) {
                sum.add(e.dist);
                dsu.merge(e.a, e.b);
            }
        }
        out.println(sum.sum());
        debug.summary();
    }

    public long edgeId(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }
}

class Point2Ext extends Point2 {
    int id;

    public Point2Ext(double x, double y) {
        super(x, y);
    }
}

class Edge {
    int a;
    int b;
    double dist;

    public Edge(int a, int b, double dist) {
        this.a = a;
        this.b = b;
        this.dist = dist;
    }
}