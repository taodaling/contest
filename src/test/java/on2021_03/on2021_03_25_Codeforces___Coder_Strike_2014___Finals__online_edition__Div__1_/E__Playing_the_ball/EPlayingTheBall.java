package on2021_03.on2021_03_25_Codeforces___Coder_Strike_2014___Finals__online_edition__Div__1_.E__Playing_the_ball;



import template.geometry.geo2.Circle2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.GeoConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EPlayingTheBall {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int d = in.ri();
        List<double[]> range = new ArrayList<>(n * 40);
        for (int i = 0; i < n; i++) {
            Point2 c = new Point2(in.ri(), in.ri());
            double theta = GeoConstant.theta(c);
            int r = in.ri();
            double dist = c.abs();
            for (int j = (int) Math.floor(dist) - r; j <= dist + r; j++) {
                if (j % d != 0 || j <= 0) {
                    continue;
                }
                double angle = GeoConstant.triangleAngle(r, j, dist);
                double left = theta - angle;
                double right = theta + angle;
                if (left < 0) {
                  //  assert left + Math.PI * 2 <= Math.PI * 2 - GeoConstant.PREC;
                    range.add(new double[]{left + Math.PI * 2, Math.PI * 2 - GeoConstant.PREC});
                    left = 0;
                }
                if (right > Math.PI * 2) {
                 //   assert 0 <= right - Math.PI * 2;
                    range.add(new double[]{0, right - Math.PI * 2});
                    right = Math.PI * 2 - GeoConstant.PREC;
                }
               // assert left <= right;
                range.add(new double[]{left, right});
            }
        }
        range = range.stream().filter(x -> x[0] <= x[1]).collect(Collectors.toList());

        int m = range.size();
        double[][] all = range.toArray(new double[0][]);
        double[][] sortByL = all.clone();
        double[][] sortByR = all.clone();
        Arrays.sort(sortByL, Comparator.comparingDouble(x -> x[0]));
        Arrays.sort(sortByR, Comparator.comparingDouble(x -> x[1]));
        int cnt = 0;
        int sortByLHead = 0;
        int sortByRHead = 0;
        int ans = 0;
        while (sortByLHead < m) {
            assert sortByRHead < m;
            if (sortByL[sortByLHead][0] <= sortByR[sortByRHead][1]) {
                cnt++;
                sortByLHead++;
            } else {
                cnt--;
                sortByRHead++;
            }
            ans = Math.max(ans, cnt);
        }

        out.println(ans);
    }
}
