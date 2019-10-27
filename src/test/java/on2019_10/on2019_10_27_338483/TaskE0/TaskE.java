package on2019_10.on2019_10_27_338483.TaskE0;




import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long total = 0;
        for (int i = 1; i <= 9; i += 1) {
            total += count(new int[i], n, 0, i * 2);
            total += count(new int[i + 1], n, 0, i * 2 + 1);
        }
        out.println(total);
    }


    public long count(int[] diff, long sum, int i, int digitNum) {
        if (i == diff.length) {
            if (sum != 0) {
                return 0;
            }
            long ways = 1;
            for (int j = 0; j < diff.length; j++) {
                long localWay = 0;
                for (int k = j == 0 ? 1 : 0; k < 10; k++) {
                    int t = k + diff[j];
                    if (t < 0 || t >= 10) {
                        continue;
                    }
                    localWay++;
                }
                ways *= localWay;
            }
            return ways;
        }

        int v;
        if (sum >= 0) {
            v = 10 - DigitUtils.digitOn(sum, i);
        } else {
            v = DigitUtils.digitOn(sum, i);
        }
        long ans = 0;
        diff[i] = v;
        ans += count(diff, sum + DigitUtils.setDigitOn(0, i, diff[i]) - DigitUtils.setDigitOn(0, digitNum - 1 - i, diff[i]),
                i + 1, digitNum);
        diff[i] = v - 10;
        ans += count(diff, sum + DigitUtils.setDigitOn(0, i, diff[i]) - DigitUtils.setDigitOn(0, digitNum - 1 - i, diff[i]),
                i + 1, digitNum);
        return ans;
    }
}
