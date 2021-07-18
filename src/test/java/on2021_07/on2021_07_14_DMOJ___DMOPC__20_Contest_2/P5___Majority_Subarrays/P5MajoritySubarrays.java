package on2021_07.on2021_07_14_DMOJ___DMOPC__20_Contest_2.P5___Majority_Subarrays;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;

import java.util.Arrays;

public class P5MajoritySubarrays {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        cnts = new int[n + 1];
        for (int x : a) {
            cnts[x]++;
        }
        occur = new int[n + 1];
        long ans = 0;
        for (int b = 1; b < 2 * T; b++) {
            init(b);
            for (int i = 0; i < n; i++) {
                add(a[i], 1);
                if (i - b >= 0) {
                    add(a[i - b], -1);
                }
                if (i >= b - 1 && aboveCnt > 0) {
                    ans++;
                }
            }
        }
        int[] bit = new int[n * 2 + 10];
        int zero = n + 5;
        for (int main = 1; main <= n; main++) {
            if (cnts[main] < T) {
                continue;
            }
            Arrays.fill(bit, 0);
            int ps = zero;
            int lower = 0;
            bit[ps]++;
            for (int j = 0; j < n; j++) {
                if (a[j] == main) {
                    lower += bit[ps];
                    ps++;
                } else {
                    ps--;
                    lower -= bit[ps];
                }
                ans += lower;
                bit[ps]++;
            }
        }

        out.println(ans);
    }

    int[] cnts;
    int T = 350;
    int[] occur;
    int aboveCnt;
    int B;

    void init(int B) {
        this.B = B;
        Arrays.fill(occur, 0);
        aboveCnt = 0;
    }

    void add(int x, int v) {
        if (cnts[x] >= T) {
            return;
        }
        if (occur[x] + occur[x] > B) {
            aboveCnt--;
        }
        occur[x] += v;
        if (occur[x] + occur[x] > B) {
            aboveCnt++;
        }
    }
}

