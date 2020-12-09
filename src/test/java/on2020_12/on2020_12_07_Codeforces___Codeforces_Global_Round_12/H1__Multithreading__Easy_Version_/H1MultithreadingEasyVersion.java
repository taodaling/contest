package on2020_12.on2020_12_07_Codeforces___Codeforces_Global_Round_12.H1__Multithreading__Easy_Version_;



import template.datastructure.ModPreSum;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Power;

public class H1MultithreadingEasyVersion {

    int mod = 998244353;
    Combination comb = new Combination((int) 1e6, mod);
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();

        int[] cnts = new int[2];
        int[] marks = new int[2];

        for (int i = 0; i < n; i++) {
            char c = in.rc();
            if (c == 'b') {
                cnts[i & 1]++;
            }
            if (c == '?') {
                marks[i & 1]++;
            }
        }
        long[] way = new long[n + 1];
        long[] waySum = new long[n + 1];
        for (int i = 0; i <= n; i++) {
            if (i < cnts[1]) {
                way[i] = 0;
                waySum[i] = 0;
                continue;
            }
            int req = i - cnts[1];
            way[i] = comb.combination(marks[1], req);
            waySum[i] = way[i] * i % mod;
        }
        ModPreSum[] wayPs = new ModPreSum[2];
        ModPreSum[] waySumPs = new ModPreSum[2];
        for (int i = 0; i < 2; i++) {
            wayPs[i] = new ModPreSum(n + 1, mod);
            waySumPs[i] = new ModPreSum(n + 1, mod);
            int finalI = i;
            wayPs[i].populate(j -> (j & 1) == finalI ? (int) way[j] : 0, n + 1);
            waySumPs[i].populate(j -> (j & 1) == finalI ? (int) waySum[j] : 0, n + 1);
        }
        long sum = 0;
        for (int i = 0; i <= n; i++) {
            if (i < cnts[0]) {
                continue;
            }
            int sign = i & 1;
            int req = i - cnts[0];
            long local = 0;
            local += i * wayPs[sign].prefix(i) - waySumPs[sign].prefix(i);
            local += -i * wayPs[sign].post(i + 1) + waySumPs[sign].post(i + 1);
            local = local % mod * comb.combination(marks[0], req) % mod;
            sum += local;
        }
        sum = sum % mod * pow.inversePower(2, marks[0] + marks[1]);
        sum = DigitUtils.mod(sum, mod);
        out.println(sum);
    }
}
