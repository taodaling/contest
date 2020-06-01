package contest;

import geometry.CircleOperations;
import template.geometry.GeoConstant;
import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.HighPrecision;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ABarcelonianDistance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readInt();
        long b = in.readInt();
        long c = in.readInt();

        long x1 = in.readInt();
        long y1 = in.readInt();
        long x2 = in.readInt();
        long y2 = in.readInt();

        double ans = Math.abs(x1 - x2) + Math.abs(y1 - y2);
        for (double[] pt1 : possible(x1, y1, a, b, c)) {
            for (double[] pt2 : possible(x2, y2, a, b, c)) {
                double dx = pt1[0] - pt2[0];
                double dy = pt1[1] - pt2[1];
                BigDecimal bd = hp.plus(hp.pow(BigDecimal.valueOf(dx), 2),
                        hp.pow(BigDecimal.valueOf(dy), 2));
                bd = hp.sqrt(bd, 1000);
                double val = bd.doubleValue() + Math.abs(pt1[0] - x1) +
                        Math.abs(pt1[1] - y1) + Math.abs(pt2[0] - x2) +
                        Math.abs(pt2[1] - y2);
                ans = Math.min(ans, val);
            }
        }

        out.println(ans);
    }

    HighPrecision hp = new HighPrecision();

    //ax + by + c = 0
    public List<double[]> possible(long x, long y, long a, long b, long c) {
        if (a * x + b * y + c == 0) {
            return Arrays.asList(new double[]{x, y});
        }
        List<double[]> ans = new ArrayList<>();
        if (b != 0) {
            ans.add(new double[]{x, -(c + a * x) / (double) b});
        }
        if (a != 0) {
            ans.add(new double[]{-(c + b * y) / (double) a, y});
        }
        return ans;
    }
}
