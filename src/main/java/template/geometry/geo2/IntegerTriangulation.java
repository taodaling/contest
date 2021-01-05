package template.geometry.geo2;

import java.util.function.Consumer;

public class IntegerTriangulation {
    public IntegerTriangulation() {
        this((long) 1e6);
    }

    /**
     * @param LOTS 顶点xy数据范围
     */
    public IntegerTriangulation(long LOTS) {
        theRoot = newTriangle(new Triangle(new IntegerPoint2(-LOTS, -LOTS), new IntegerPoint2(+LOTS, -LOTS), new IntegerPoint2(0, +LOTS)));
    }


    static long sqr(long x) {
        return x * x;
    }

    static long distSqr(IntegerPoint2 a, IntegerPoint2 b) {
        return sqr(a.x - b.x) + sqr(a.y - b.y);
    }


    boolean inCircumcircle(IntegerPoint2 p1, IntegerPoint2 p2, IntegerPoint2 p3, IntegerPoint2 p4) {
        //return IntegerPoint2.inCicle(p1, p2, p3, p4);
        long u11 = p1.x - p4.x, u21 = p2.x - p4.x, u31 = p3.x - p4.x;
        long u12 = p1.y - p4.y, u22 = p2.y - p4.y, u32 = p3.y - p4.y;
        long u13 = sqr(p1.x) - sqr(p4.x) + sqr(p1.y) - sqr(p4.y);
        long u23 = sqr(p2.x) - sqr(p4.x) + sqr(p2.y) - sqr(p4.y);
        long u33 = sqr(p3.x) - sqr(p4.x) + sqr(p3.y) - sqr(p4.y);
        long det = -u13 * u22 * u31 + u12 * u23 * u31 + u13 * u21 * u32 - u11 * u23 * u32 - u12 * u21 * u33 + u11 * u22 * u33;
        return det > 0;
    }

    static long side(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 p) {
        return (b.x - a.x) * (p.y - a.y) - (b.y - a.y) * (p.x - a.x);
    }

    public void forEach(Consumer<Triangle> consumer) {
        version++;
        dfs(consumer, theRoot);
    }

    int version;

    private void dfs(Consumer<Triangle> consumer, Triangle root) {
        if (root.version == version) {
            return;
        }
        root.version = version;
        if (!root.hasChildren()) {
            consumer.accept(root);
            return;
        } else for (int i = 0; i < 3 && root.children[i] != null; ++i)
            dfs(consumer, root.children[i]);
    }

    public static class Edge {
        public Triangle tri;
        public int side;

        Edge() {
            this(null, 0);
        }

        Edge(Triangle tri, int side) {
            this.tri = tri;
            this.side = side;
        }
    }

    public static class Triangle {
        public IntegerPoint2[] p = new IntegerPoint2[3];
        public Edge[] edge = new Edge[3];
        public Triangle[] children = new Triangle[3];
        private int version;

        Triangle() {
            this(new IntegerPoint2(), new IntegerPoint2(), new IntegerPoint2());
        }

        Triangle(IntegerPoint2 p0, IntegerPoint2 p1, IntegerPoint2 p2) {
            p[0] = p0;
            p[1] = p1;
            p[2] = p2;
            for (int i = 0; i < 3; i++) {
                edge[i] = new Edge();
            }
            children[0] = children[1] = children[2] = null;
        }

        public boolean hasChildren() {
            return children[0] != null;
        }

        public int numChildren() {
            return children[0] == null ? 0
                    : children[1] == null ? 1
                    : children[2] == null ? 2 : 3;
        }

        public boolean contains(IntegerPoint2 q) {
            long a = side(p[0], p[1], q), b = side(p[1], p[2], q), c = side(p[2], p[0], q);
            return a >= 0 && b >= 0 && c >= 0;
        }
    }


    public Triangle newTriangle(Triangle t) {
        return t;
    }

    void setEdge(Edge a, Edge b) {
        if (a.tri != null) a.tri.edge[a.side] = b;
        if (b.tri != null) b.tri.edge[b.side] = a;
    }


    public Triangle find(IntegerPoint2 p) {
        version++;
        return find(theRoot, p);
    }

    public void addPoint(IntegerPoint2 p) {
        addPoint(find(p), p);
    }

    public Triangle theRoot = new Triangle();

    Triangle find(Triangle root, IntegerPoint2 p) {
        for (; ; ) {
            if (root.version == version) {
                throw new RuntimeException();
            }
            root.version = version;
            if (!root.hasChildren()) return root;
            else for (int i = 0; i < 3 && root.children[i] != null; ++i)
                if (root.children[i].contains(p)) {
                    root = root.children[i];
                    break;
                }
        }
    }

    void addPoint(Triangle root, IntegerPoint2 p) {
        Triangle tab, tbc, tca;
        tab = newTriangle(new Triangle(root.p[0], root.p[1], p));
        tbc = newTriangle(new Triangle(root.p[1], root.p[2], p));
        tca = newTriangle(new Triangle(root.p[2], root.p[0], p));
        setEdge(new Edge(tab, 0), new Edge(tbc, 1));
        setEdge(new Edge(tbc, 0), new Edge(tca, 1));
        setEdge(new Edge(tca, 0), new Edge(tab, 1));
        setEdge(new Edge(tab, 2), root.edge[2]);
        setEdge(new Edge(tbc, 2), root.edge[0]);
        setEdge(new Edge(tca, 2), root.edge[1]);
        root.children[0] = tab;
        root.children[1] = tbc;
        root.children[2] = tca;
        flip(tab, 2);
        flip(tbc, 2);
        flip(tca, 2);
    }

    void flip(Triangle tri, int pi) {
        Triangle trj = tri.edge[pi].tri;
        int pj = tri.edge[pi].side;
        if (trj == null || !inCircumcircle(tri.p[0], tri.p[1], tri.p[2], trj.p[pj])) return;
        Triangle trk = newTriangle(new Triangle(tri.p[(pi + 1) % 3], trj.p[pj], tri.p[pi]));
        Triangle trl = newTriangle(new Triangle(trj.p[(pj + 1) % 3], tri.p[pi], trj.p[pj]));
        setEdge(new Edge(trk, 0), new Edge(trl, 0));
        setEdge(new Edge(trk, 1), tri.edge[(pi + 2) % 3]);
        setEdge(new Edge(trk, 2), trj.edge[(pj + 1) % 3]);
        setEdge(new Edge(trl, 1), trj.edge[(pj + 2) % 3]);
        setEdge(new Edge(trl, 2), tri.edge[(pi + 1) % 3]);
        tri.children[0] = trk;
        tri.children[1] = trl;
        tri.children[2] = null;
        trj.children[0] = trk;
        trj.children[1] = trl;
        trj.children[2] = null;
        flip(trk, 1);
        flip(trk, 2);
        flip(trl, 1);
        flip(trl, 2);
    }
}
