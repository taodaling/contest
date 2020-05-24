package contest;

import template.geometry.ConvexHullTrick;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerList;
import template.problem.ConvexHullAssignProblem;
import template.utils.ArrayIndex;
import template.utils.CompareUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ERandomPawn {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        long[] b = new long[n];

        in.populate(a);
        in.populate(b);

        int maxIndex = 0;
        for (int i = 0; i < n; i++) {
            if (a[i] > a[maxIndex]) {
                maxIndex = i;
            }
        }

        SequenceUtils.rotate(a, 0, n - 1, n - maxIndex);
        SequenceUtils.rotate(b, 0, n - 1, n - maxIndex);

        a = Arrays.copyOf(a, n + 1);
        b = Arrays.copyOf(b, n + 1);

        a[n] = a[0];
        b[n] = b[0];

        long[] c = new long[n + 1];
        c[0] = 0;
        c[1] = 0;
        for (int i = 2; i <= n; i++) {
            c[i] = 2 * c[i - 1] - c[i - 2] + 2 * b[i - 1];
        }

        debug.debug("a", a);
        debug.debug("b", b);
        debug.debug("c", c);

        double[] heights = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            heights[i] = a[i] - c[i];
        }

        debug.debug("heights", heights);

        double[] y = ConvexHullAssignProblem.solve(heights);


        debug.debug("y", y);
        double[] x = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            x[i] = y[i] + c[i];
        }

//        for (int i = 0; i <= n; i++) {
//            if (i == 0 || i == n) {
//                if(!isZero(y[i] - heights[i])){
//                    throw new RuntimeException();
//                }
//            }else{
//                if(!isZero(y[i] - Math.max(heights[i], (y[i - 1] + y[i + 1]) / 2))){
//                    throw new RuntimeException();
//                }
//            }
//        }


        debug.debug("x", x);
//        for (int i = 0; i <= n; i++) {
//            if (i == 0 || i == n) {
//                if(!isZero(x[i] - a[i])){
//                    throw new RuntimeException();
//                }
//            }else{
//                if(!isZero(x[i] - Math.max(a[i], (x[i - 1] + x[i + 1]) / 2 - b[i]))){
//                    throw new RuntimeException();
//                }
//            }
//        }

        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += x[i] / n;
        }

        out.println(sum);
    }

    public boolean isZero(double x){
        return x > -1e-8 && x < 1e-8;
    }
}
