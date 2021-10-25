package on2021_09.on2021_09_28_CS_Academy___Virtual_Round__43.Diesel_Train;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongIntervalMap;

public class DieselTrain {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int D = in.ri();
        int L = in.ri();
        int n = in.ri();

        LongIntervalMap map = new LongIntervalMap();
        long inf = (long) 1e18;
        map.add(-inf, inf);
        map.remove(-inf, L);
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            map.remove(x, x + L);
        }
        map.remove(D, inf);
        long ans = 0;
        for (LongIntervalMap.Interval interval : map) {
            long len = interval.length();
            ans += len * len;
        }
        double res = ans / 4.0 / D;
        out.println(res);
    }
}
