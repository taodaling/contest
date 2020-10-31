package on2020_10.on2020_10_26_AtCoder___AtCoder_Beginner_Contest_157.F___Yakiniku_Optimization_Problem;



import template.algo.DoubleBinarySearch;
import template.geometry.Point2D;
import template.geometry.geo2.CircleCoverDetect;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.DoubleArrayList;
import template.primitve.generated.datastructure.DoubleIntervalMap;

import java.util.ArrayList;
import java.util.List;

public class FYakinikuOptimizationProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Meat[] meats = new Meat[n];
        for (int i = 0; i < n; i++) {
            meats[i] = new Meat(in.readInt(), in.readInt());
            meats[i].c = in.readInt();
        }


        DoubleArrayList dal = new DoubleArrayList();
        List<double[]> list = new ArrayList<>();
        CircleCoverDetect ccd = new CircleCoverDetect(1e-10);
        ccd.setConsumer((l, r) -> list.add(new double[]{l, r}));
        int[] ps = new int[10000];
        DoubleBinarySearch dbs = new DoubleBinarySearch(1e-10, 1e-10) {
            @Override
            public boolean check(double mid) {
                for (int i = 0; i < n; i++) {
                    list.clear();
                    ccd.reset(meats[i], mid / meats[i].c);

                    for (int j = 0; j < n; j++) {
                        double r = mid / meats[j].c;
                        ccd.addCircle(meats[j], r);
                    }
                    dal.clear();
                    dal.add(0);
                    dal.add(Math.PI * 2);
                    for (double[] lr : list) {
                        dal.add(lr[0]);
                        dal.add(lr[1]);
                    }
                    dal.unique();
                    int m = dal.size();
                    for (int j = 0; j <= m; j++) {
                        ps[j] = 0;
                    }
                    for (double[] lr : list) {
                        ps[dal.binarySearch(lr[0])]++;
                        ps[dal.binarySearch(lr[1])]--;
                    }
                    for (int j = 0; j <= m; j++) {
                        if (j > 0) {
                            ps[j] += ps[j - 1];
                        }
                        if (ps[j] + 1 >= k) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        //dbs.check(3);
        double ans = dbs.binarySearch(0, 1e20);
        out.println(ans);
    }
}

class Meat extends Point2 {
    int c;

    public Meat(double x, double y) {
        super(x, y);
    }
}