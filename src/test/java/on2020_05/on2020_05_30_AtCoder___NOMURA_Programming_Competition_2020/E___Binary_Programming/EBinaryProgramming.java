package on2020_05.on2020_05_30_AtCoder___NOMURA_Programming_Competition_2020.E___Binary_Programming;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerPreSum;

public class EBinaryProgramming {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] s = new int[(int) 2e5];
        int n = in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }

        IntegerPreSum[] ips = new IntegerPreSum[2];
        ips[0] = new IntegerPreSum(i -> i % 2 == 0 && s[i] == 1 ? 1 : 0, n);
        ips[1] = new IntegerPreSum(i -> i % 2 == 1 && s[i] == 1 ? 1 : 0, n);
        long ans = 0;
        boolean[] used = new boolean[n];
        boolean[] star = new boolean[n];
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == 1) {
                used[i] = true;
                star[i] = true;
                cnt++;
                ans += cnt;
                if (i + 1 < n) {
                    used[i + 1] = true;
                    ans += cnt;
                    i++;
                }
            }
        }
        IntegerPreSum occur = new IntegerPreSum(i -> used[i] ? 1 : 0, n);

        for (int i = n - 1; i >= 0; i--) {
            if (used[i]) {
                if (star[i]) {
                    cnt--;
                }
                continue;
            }
            ans += cnt;
            if (occur.prefix(i - 1) % 2 == 0) {
                ans += ips[i % 2].post(i);
            } else {
                ans += ips[1 - i % 2].post(i);
            }
        }

        out.println(ans);
    }
}
