package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.LongBIT;
import template.utils.Debug;

import java.util.*;

public class CGoodbyeSouvenir {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        Point[] lPoints = new Point[n];
        Point[] rPoints = new Point[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        List<Point> left = new ArrayList<>(10 * (n + m));
        List<Point> right = new ArrayList<>(10 * (n + m));
        List<Query> queries = new ArrayList<>(m);

        TreeSet<Integer>[] maps = new TreeSet[n + 1];
        for (int i = 1; i <= n; i++) {
            maps[i] = new TreeSet<>();
        }
        for (int i = 0; i < n; i++) {
            maps[a[i]].add(i);
        }

        for (int i = 0; i < n; i++) {
            Integer pre = maps[a[i]].floor(i - 1);
            Integer post = maps[a[i]].ceiling(i + 1);

            Point p1 = new Point();
            p1.x = i;
            p1.y = pre == null ? -1 : pre;
            p1.z = 1;
            p1.w = i;
            lPoints[i] = p1;

            Point p2 = new Point();
            p2.x = i;
            p2.y = post == null ? n : post;
            p2.z = 1;
            p2.w = i;
            rPoints[i] = p2;

            left.add(p1);
            right.add(p2);
        }

        for (int i = 2; i < 2 + m; i++) {
            int t = in.readInt();
            if (t == 1) {
                int p = in.readInt() - 1;
                int x = in.readInt();

                {
                    //remove
                    maps[a[p]].remove(p);
                    Integer pre = maps[a[p]].floor(p - 1);
                    Integer post = maps[a[p]].ceiling(p + 1);

                    left.add(lPoints[p].negate(i));
                    right.add(rPoints[p].negate(i));
                    if (pre != null) {
                        right.add(rPoints[pre].negate(i));
                        rPoints[pre] = rPoints[pre].clone(i);
                        rPoints[pre].y = post == null ? n : post;
                        right.add(rPoints[pre]);
                    }
                    if (post != null) {
                        left.add(lPoints[post].negate(i));
                        lPoints[post] = lPoints[post].clone(i);
                        lPoints[post].y = pre == null ? -1 : pre;
                        left.add(lPoints[post]);
                    }
                }

                {
                    //add
                    a[p] = x;

                    maps[a[p]].add(p);
                    Integer pre = maps[a[p]].floor(p - 1);
                    Integer post = maps[a[p]].ceiling(p + 1);

                    lPoints[p] = lPoints[p].clone(i);
                    lPoints[p].y = pre == null ? -1 : pre;
                    rPoints[p] = rPoints[p].clone(i);
                    rPoints[p].y = post == null ? n : post;
                    left.add(lPoints[p]);
                    right.add(rPoints[p]);


                    if (pre != null) {
                        right.add(rPoints[pre].negate(i));
                        rPoints[pre] = rPoints[pre].clone(i);
                        rPoints[pre].y = p;
                        right.add(rPoints[pre]);
                    }
                    if (post != null) {
                        left.add(lPoints[post].negate(i));
                        lPoints[post] = lPoints[post].clone(i);
                        lPoints[post].y = p;
                        left.add(lPoints[post]);
                    }
                }
            } else {
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                Query q = new Query();
                Point lQ = new Point();
                lQ.x = r;
                lQ.y = l - 1;
                lQ.z = i;
                lQ.t = 1;
                q.sub[0] = lQ;
                q.add[0] = lQ.clone(i);
                q.add[0].x = l - 1;


                Point rQ = new Point();
                rQ.x = l;
                rQ.y = r + 1;
                rQ.z = i;
                rQ.t = 1;
                q.add[1] = rQ;
                q.sub[1] = rQ.clone(i);
                q.sub[1].x = r + 1;

                left.add(q.add[0]);
                left.add(q.sub[0]);
                right.add(q.add[1]);
                right.add(q.sub[1]);

                queries.add(q);
            }
        }


        for (Point r : right) {
            r.z = 200000 - r.z;
        }

        debug.debug("left", left);
        debug.debug("right", right);

        left.sort((x, y) -> {
            int cp = x.x - y.x;
            if (cp == 0) {
                cp = x.y - y.y;
            }
            if (cp == 0) {
                cp = x.z - y.z;
            }
            if (cp == 0) {
                cp = x.t - y.t;
            }
            return cp;
        });


        right.sort((x, y) -> {
            int cp = x.x - y.x;
            if (cp == 0) {
                cp = x.y - y.y;
            }
            if (cp == 0) {
                cp = x.z - y.z;
            }
            if (cp == 0) {
                cp = -(x.t - y.t);
            }
            return cp;
        });


        dac1(left.toArray(new Point[0]), 0, left.size() - 1);
        dac2(right.toArray(new Point[0]), 0, right.size() - 1);


        for (int i = 0; i < queries.size(); i++) {
            Query q = queries.get(i);
            long ans = 0;
            for(int j = 0; j < 2; j++){
                ans += q.add[j].sum - q.sub[j].sum;
            }
            out.println(ans);
        }
    }

    LongBIT bit = new LongBIT(200000);

    public void dac1(Point[] pts, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) / 2;
        dac1(pts, l, m);
        dac1(pts, m + 1, r);
        Arrays.sort(pts, l, m + 1, (a, b) -> Integer.compare(a.y, b.y));
        Arrays.sort(pts, m + 1, r + 1, (a, b) -> Integer.compare(a.y, b.y));

        int i = l;
        int j = m + 1;
        while (j <= r) {
            while (i <= m && pts[i].y <= pts[j].y) {
                bit.update(pts[i].z, pts[i].w);
                i++;
            }
            pts[j].sum += bit.query(pts[j].z);
            j++;
        }

        while (i - 1 >= l) {
            i--;
            bit.update(pts[i].z, -pts[i].w);
        }
    }


    public void dac2(Point[] pts, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) / 2;
        dac2(pts, l, m);
        dac2(pts, m + 1, r);
        Arrays.sort(pts, l, m + 1, (a, b) -> -Integer.compare(a.y, b.y));
        Arrays.sort(pts, m + 1, r + 1, (a, b) -> -Integer.compare(a.y, b.y));

        int i = l;
        int j = m + 1;
        while (i <= m) {
            while (j <= r && pts[i].y <= pts[j].y) {
                bit.update(pts[j].z, pts[j].w);
                j++;
            }
            pts[i].sum += bit.query(200000) - bit.query(pts[i].z - 1);
            i++;
        }

        while (j - 1 > m) {
            j--;
            bit.update(pts[j].z, -pts[j].w);
        }
    }
}

class Point implements Cloneable {
    int x;
    int y;
    int z;
    int w;

    int t;
    long sum;

    public Point negate(int z) {
        Point ans = clone(z);
        ans.w = -ans.w;
        return ans;
    }

    public Point clone(int z) {
        try {
            Point pt = (Point) super.clone();
            pt.z = z;
            return pt;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d) = %d", x, y, z, w);
    }
}

class Query {
    Point[] add = new Point[2];
    Point[] sub = new Point[2];
}
