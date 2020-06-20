package on2020_06.on2020_06_20_TopCoder_SRM__755.OneHandRoadPainting;



import template.math.DigitUtils;

public class OneHandRoadPainting {
    public long fastest(int[] dStart, int[] dEnd, int power) {
        long ans = 0;
        long remain = 0;
       // int max = dEnd[dStart.length - 1];
        for (int i = dStart.length - 1; i >= 0; i--) {
            long used = Math.min(remain, dEnd[i] - dStart[i]);
            dEnd[i] -= used;
            remain -= used;
            if (dEnd[i] == dStart[i]) {
                continue;
            }
            //remain == 0
            int l = dStart[i];
            int r = dEnd[i];
            //r + (r - power) + (r - 2 * pow) + ...
            //r - k * p > l => k * p < r - l
            long k = DigitUtils.maximumIntegerLessThanDiv(r - l, power);
            ans += (k + 1) * r - sum(k) * power;

            remain = power - (r - k * power - l);
        }
        ans *= 2;
        //ans -= max;

        return ans;
    }

    //1 + .. + x
    public long sum(long x) {
        return (x + 1) * x / 2;
    }
}
