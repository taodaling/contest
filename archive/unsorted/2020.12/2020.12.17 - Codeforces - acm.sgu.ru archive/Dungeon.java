package contest;

import template.geometry.geo3.Line3;
import template.geometry.geo3.Point3;
import template.geometry.geo3.Sphere;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Sphere[] spheres = new Sphere[n];
        for (int i = 0; i < n; i++) {
            spheres[i] = new Sphere(new Point3(in.ri(), in.ri(), in.ri()), in.ri());
        }
        Point3 a = new Point3(in.ri(), in.ri(), in.ri());
        Point3 b = Point3.plus(a, new Point3(in.ri(), in.ri(), in.ri()));
        if (Point3.dist(a, b) < 1e-8) {
            return;
        }
        List<Integer> seq = new ArrayList<>();
        List<Point3> buf = new ArrayList<>(3);
        int last = -1;
        while (seq.size() < 11) {
            Line3 line = new Line3(a, b);
            Point3 nearest = null;
            int belong = -1;
            for (int i = 0; i < n; i++) {
                if (i == last) {
                    continue;
                }
                buf.clear();
                if (spheres[i].sphereLine(line, buf) == 0) {
                    continue;
                }
                for (Point3 pt : buf) {
                    if (nearest == null || Point3.dist2(a, nearest) > Point3.dist2(a, pt)) {
                        if(Point3.dist2(a, pt) < 1e-8){
                            continue;
                        }
                        nearest = pt;
                        belong = i;
                    }
                }
            }
            if (belong == -1) {
                break;
            }
            seq.add(belong);
            Line3 alpha = new Line3(spheres[belong].o, nearest);
            b = alpha.reflect(a);
            a = nearest;
            last = belong;
        }
        for (int i = 0; i < seq.size() && i < 10; i++) {
            out.append(seq.get(i) + 1).append(' ');
        }
        if (seq.size() > 10) {
            out.append("etc.");
        }
    }
}

class Cand {
    Point3 pt;
    int belong;
    double dist;
}