package contest;

import template.algo.DoubleTernarySearch;
import template.geometry.geo2.Point2;
import template.rand.RandomWrapper;
import template.rand.SimulatedAnnealing;
import template.utils.Debug;

import java.util.Random;
import java.util.function.DoubleUnaryOperator;

public class SpaceProbes {
    public void normal(double[] pt) {
        double sum = 0;
        for (double x : pt) {
            sum += x * x;
        }
        double len = Math.sqrt(sum);
        for (int i = 0; i < pt.length; i++) {
            pt[i] /= len;
        }
    }

    Debug debug = new Debug(true);
    public double focusDistance(long[] x, long[] y, long[] z) {
        double[] d1 = new double[3];
        d1[0] = x[1] - x[0];
        d1[1] = y[1] - y[0];
        d1[2] = z[1] - z[0];

        double[] d2 = new double[3];
        d2[0] = x[3] - x[2];
        d2[1] = y[3] - y[2];
        d2[2] = z[3] - z[2];

        normal(d1);
        normal(d2);
        debug.debug("d1", d1);
        debug.debug("d2", d2);

        DoubleUnaryOperator eval = new DoubleUnaryOperator() {
            @Override
            public double applyAsDouble(double a) {
                DoubleUnaryOperator eval2 = new DoubleUnaryOperator() {
                    @Override
                    public double applyAsDouble(double b) {
                        double[] status = new double[]{a, b};
                        double x0 = x[0] + d1[0] * status[0];
                        double y0 = y[0] + d1[1] * status[0];
                        double z0 = z[0] + d1[2] * status[0];

                        double x1 = x[2] + d2[0] * status[1];
                        double y1 = y[2] + d2[1] * status[1];
                        double z1 = z[2] + d2[2] * status[1];

                        double middleX = (x0 + x1) / 2;
                        double middleY = (y0 + y1) / 2;
                        double middleZ = (z0 + z1) / 2;


                        double tx = x[4];
                        double ty = y[4];
                        double tz = z[4];

                        double dx = middleX - tx;
                        double dy = middleY - ty;
                        double dz = middleZ - tz;

                        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        return -dist;
                    }
                };
                DoubleTernarySearch dts = new DoubleTernarySearch(eval2, 1e-10,  1e-10);
                double best = dts.find(-1e30, 1e30);
                return eval2.applyAsDouble(best);
            }
        };
        DoubleTernarySearch dts = new DoubleTernarySearch(eval, 1e-10,  1e-10);
        double best = dts.find(-1e30, 1e30);


        double ans = -eval.applyAsDouble(best);
        return ans;
    }
}
