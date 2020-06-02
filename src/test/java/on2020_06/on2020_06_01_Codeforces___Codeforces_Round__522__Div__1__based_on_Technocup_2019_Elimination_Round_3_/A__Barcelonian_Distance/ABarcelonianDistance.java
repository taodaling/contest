package on2020_06.on2020_06_01_Codeforces___Codeforces_Round__522__Div__1__based_on_Technocup_2019_Elimination_Round_3_.A__Barcelonian_Distance;




import com.sun.org.apache.bcel.internal.generic.LDIV;
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
        for (BigDecimal[] pt1 : possible(x1, y1, a, b, c)) {
            for (BigDecimal[] pt2 : possible(x2, y2, a, b, c)) {
                BigDecimal dx = hp.subtract(pt1[0], pt2[0]);
                BigDecimal dy = hp.subtract(pt1[1], pt2[1]);
                BigDecimal bd = hp.plus(hp.pow(dx, 2),
                        hp.pow(dy, 2));
                bd = hp.sqrt(bd, BigDecimal.valueOf(1e-7));
                double val = bd.doubleValue() + Math.abs(pt1[0].doubleValue() - x1) +
                        Math.abs(pt1[1].doubleValue() - y1) + Math.abs(pt2[0].doubleValue() - x2) +
                        Math.abs(pt2[1].doubleValue() - y2);
                ans = Math.min(ans, val);
            }
        }

        out.println(ans);
    }

    HighPrecision hp = new HighPrecision();

    //ax + by + c = 0
    public List<BigDecimal[]> possible(long x, long y, long a, long b, long c) {
        if (a * x + b * y + c == 0) {
            BigDecimal[] ans = new BigDecimal[]{BigDecimal.valueOf(x), BigDecimal.valueOf(y)};
            return Arrays.asList(new BigDecimal[][]{ans});
        }
        List<BigDecimal[]> ans = new ArrayList<>();
        if (b != 0) {
            ans.add(new BigDecimal[]{BigDecimal.valueOf(x), hp.divide(BigDecimal.valueOf(-(c + a * x)), BigDecimal.valueOf(b))});
        }
        if (a != 0) {
            ans.add(new BigDecimal[]{hp.divide(BigDecimal.valueOf(-(c + b * y)), BigDecimal.valueOf(a)), BigDecimal.valueOf(y)});
        }
        return ans;
    }
}
