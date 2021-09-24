package on2021_08.on2021_08_19_Yandex___Algorithm_2018__Final.A__Smart_Vending;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.EuclidLikeFunction;

public class ASmartVending {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long b = in.ri();
        long c = in.ri();
        long r = in.rl();
        long d = in.rl();
        long million = (long) 1e6;
        long remainder = r % million;

        long inf = (long) 1e18;
        long maxStep = inf;
        if (d + c < million) {
            //(d + c) - x < million - remainder
            long left = (d + c) - (million - remainder) + 1;
            //c < remainder
            long right = remainder - 1;
            assert left <= right;

            long first = EuclidLikeFunction.firstOccurResidue(-remainder, c, million, left, right);
            if (first != -1) {
                maxStep = first;
            }
        }

        maxStep = Math.min(maxStep, (b * million + c) / r);
        out.println(maxStep);
    }
}
