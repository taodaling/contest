package on2021_05.on2021_05_17_AtCoder___KYOCERA_Programming_Contest_2021_AtCoder_Beginner_Contest_200_.F___Minflip_Summation;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow2;
import template.math.DigitUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class FMinflipSummation {
    int mod = (int) 1e9 + 7;
    Debug debug = new Debug(false);

    public Mat mul(Mat a, Mat b) {
        long[][][] way = new long[2][2][2];
        long[][][] sum = new long[2][2][2];
        for (int a0 = 0; a0 < 2; a0++) {
            for (int c0 = 0; c0 < 2; c0++) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        for (int x = 0; x < 2; x++) {
                            for (int y = 0; y < 2; y++) {
                                way[a0][x][y ^ j ^ (i ^ c0)] += a.way[a0][i][j] * b.way[c0][x][y] % mod;
                                sum[a0][x][y ^ j ^ (i ^ c0)] += (a.way[a0][i][j] * b.sum[c0][x][y] + a.sum[a0][i][j] * b.way[c0][x][y]) % mod;
                                if ((y ^ j ^ (i ^ c0)) == 1 && i != c0) {
                                    sum[a0][x][y ^ j ^ (i ^ c0)] += a.way[a0][i][j] * b.way[c0][x][y] % mod;
                                }
                                if(j == 1 && y == 1){
                                    sum[a0][x][y ^ j ^ (i ^ c0)] -= a.way[a0][i][j] * b.way[c0][x][y] % mod;
                                }
                            }
                        }
                    }
                }
            }
        }

        debug.log("mul");
        debug.debug("way", way);
        debug.debug("sum", sum);
        return new Mat(way, sum);
    }

    public Mat pow(Mat x, int n) {
        if (n == 1) {
            return x;
        }
        Mat ans = pow(x, n / 2);
        ans = mul(ans, ans);
        if (n % 2 == 1) {
            ans = mul(ans, x);
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        int n = in.rs(s);
        int k = in.ri();

        long[][][] prevSum = new long[2][2][2];
        long[][][] nextSum = new long[2][2][2];
        long[][][] prevWay = new long[2][2][2];
        long[][][] nextWay = new long[2][2][2];

        if (s[0] != '0') {
            prevWay[1][1][0]++;
        }
        if (s[0] != '1') {
            prevWay[0][0][0]++;
        }

        for (char c : Arrays.copyOfRange(s, 1, n)) {
            debug.debug("c", c);
            debug.debug("prevWay", prevWay);
            debug.debug("prevSum", prevSum);
            SequenceUtils.deepFill(nextSum, 0L);
            SequenceUtils.deepFill(nextWay, 0L);
            for (int a = 0; a < 2; a++) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        prevWay[a][i][j] = DigitUtils.modWithoutDivision(prevWay[a][i][j], mod);
                        prevSum[a][i][j] = DigitUtils.modWithoutDivision(prevSum[a][i][j], mod);

                        //consider this
                        if (c != '1') {
                            //may zero
                            nextWay[a][0][j ^ 0 ^ i] += prevWay[a][i][j];
                            nextSum[a][0][j ^ 0 ^ i] += prevSum[a][i][j];
                            if (i != 0 && (j ^ 0 ^ i) == 1) {
                                nextSum[a][0][j ^ 0 ^ i] += prevWay[a][i][j];
                            }
                        }
                        if (c != '0') {
                            //one
                            nextWay[a][1][j ^ 1 ^ i] += prevWay[a][i][j];
                            nextSum[a][1][j ^ 1 ^ i] += prevSum[a][i][j];
                            if (i != 1 && (j ^ 1 ^ i) == 1) {
                                nextSum[a][1][j ^ 1 ^ i] += prevWay[a][i][j];
                            }
                        }
                    }

                }
            }

            {
                long[][][] tmp = prevWay;
                prevWay = nextWay;
                nextWay = tmp;
            }
            {
                long[][][] tmp = prevSum;
                prevSum = nextSum;
                nextSum = tmp;
            }
        }

        debug.log("sep");
        debug.debug("prevWay", prevWay);
        debug.debug("prevSum", prevSum);
        Mat mat = new Mat(prevWay, prevSum);
        Mat res = pow(mat, k);

        long ans = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int t = 0; t < 2; t++) {
                    ans += res.sum[i][j][t];
                }
            }
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}

class Mat {
    static int mod = (int) 1e9 + 7;
    long[][][] way;
    long[][][] sum;

    public Mat(long[][][] way, long[][][] sum) {
        this.way = way;
        this.sum = sum;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    way[i][j][k] %= mod;
                    sum[i][j][k] %= mod;
                }
            }
        }
    }
}