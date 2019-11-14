package on2019_11.on2019_11_14_Tenka1_Programmer_Contest.E___CARtesian_Coodinate;



import java.util.Arrays;

import template.BIT;
import template.CompareUtils;
import template.DoubleBinarySearch;
import template.FastInput;
import template.FastOutput;
import template.GeometryUtils;

public class TaskE {
    double prec = 1e-12;

    public void solve(int testNumber, FastInput in, FastOutput out) {

        int n = in.readInt();
        Line[] lines = new Line[n];
        for (int i = 0; i < n; i++) {
            double a = in.readInt();
            double b = in.readInt();
            double c = in.readInt();
            GeometryUtils.Point2D pt0 = new GeometryUtils.Point2D(0, apply(a, b, c, 0));
            GeometryUtils.Point2D pt1 = new GeometryUtils.Point2D(1, apply(a, b, c, 1));
            GeometryUtils.Line2D line = new GeometryUtils.Line2D(pt0, pt1);
            lines[i] = new Line();
            lines[i].line = line;
            lines[i].ab = -a / b;
            lines[i].ba = b / a;
        }


        Arrays.sort(lines, (a, b) -> Double.compare(a.ba, b.ba));
        for (int i = 0; i < n; i++) {
            lines[i].baIndex = i + 1;
        }
        Arrays.sort(lines, (a, b) -> Double.compare(a.ab, b.ab));
        for (int i = 0; i < n; i++) {
            lines[i].abIndex = i + 1;
        }


        // find x
        BIT bit = new BIT(n);
        int total = n * (n - 1) / 2;

        double xl = new DoubleBinarySearch(prec, prec) {
            @Override
            public boolean check(double mid) {
                int leftCnt = 0;
                bit.clear();

                GeometryUtils.Line2D test =
                        new GeometryUtils.Line2D(new GeometryUtils.Point2D(mid, 0), new GeometryUtils.Point2D(mid, 1));
                for (int i = 0; i < n; i++) {
                    lines[i].intersect = test.intersect(lines[i].line).y;
                }
                Arrays.sort(lines, (a, b) -> Double.compare(a.intersect, b.intersect));
                for (int i = 0; i < n; i++) {
                    int l = i;
                    int r = i;
                    while (r + 1 < n && CompareUtils.compare(lines[r].intersect, lines[r + 1].intersect, prec) == 0) {
                        r++;
                    }
                    i = r;

                    for (int j = l; j <= r; j++) {
                        leftCnt += bit.query(lines[j].abIndex);
                    }
                    for (int j = l; j <= r; j++) {
                        bit.update(lines[j].abIndex, 1);
                    }
                }

                return !(leftCnt < total - leftCnt);
            }
        }.binarySearch(-1e10, 1e10);

        double yl = new DoubleBinarySearch(prec, prec) {
            @Override
            public boolean check(double mid) {
                int leftCnt = 0;
                bit.clear();

                GeometryUtils.Line2D test =
                        new GeometryUtils.Line2D(new GeometryUtils.Point2D(0, mid), new GeometryUtils.Point2D(1, mid));
                for (int i = 0; i < n; i++) {
                    lines[i].intersect = test.intersect(lines[i].line).x;
                }
                Arrays.sort(lines, (a, b) -> Double.compare(a.intersect, b.intersect));
                for (int i = 0; i < n; i++) {
                    int l = i;
                    int r = i;
                    while (r + 1 < n && CompareUtils.compare(lines[r].intersect, lines[r + 1].intersect, prec) == 0) {
                        r++;
                    }
                    i = r;

                    for (int j = l; j <= r; j++) {
                        leftCnt += bit.query(n) - bit.query(lines[j].baIndex);
                    }
                    for (int j = l; j <= r; j++) {
                        bit.update(lines[j].baIndex, 1);
                    }
                }

                return !(leftCnt < total - leftCnt);
            }
        }.binarySearch(-1e10, 1e10);

        out.printf("%.15f %.15f", xl, yl);
    }

    double apply(double a, double b, double c, double x) {
        return (c - a * x) / b;
    }
}


class Line {
    GeometryUtils.Line2D line;
    double intersect;
    double ba;
    double ab;
    int baIndex;
    int abIndex;
}
