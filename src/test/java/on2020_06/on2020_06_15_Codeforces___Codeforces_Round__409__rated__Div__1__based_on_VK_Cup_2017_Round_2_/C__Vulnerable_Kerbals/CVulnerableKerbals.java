package on2020_06.on2020_06_15_Codeforces___Codeforces_Round__409__rated__Div__1__based_on_VK_Cup_2017_Round_2_.C__Vulnerable_Kerbals;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

public class CVulnerableKerbals {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        boolean[] forbidden = new boolean[m];
        int[] gcds = new int[m + 1];
        IntegerMultiWayStack ims = new IntegerMultiWayStack(m + 1, m);
        for (int i = 0; i < n; i++) {
            forbidden[in.readInt()] = true;
        }
        for (int i = 0; i < m; i++) {
            if (forbidden[i]) {
                continue;
            }
            int g = GCDs.gcd(i, m);
            gcds[g]++;
            ims.addLast(g, i);
        }

        int[] dp = new int[m + 1];
        int[] next = new int[m + 1];
        for (int i = m; i >= 1; i--) {
            next[i] = -1;
            for (int j = i + i; j <= m; j += i) {
                if (dp[j] > dp[i]) {
                    dp[i] = dp[j];
                    next[i] = j;
                }
            }
            dp[i] += gcds[i];
        }

        int ans = dp[1];
        out.println(ans);
        IntegerArrayList list = new IntegerArrayList(ans);
        for (int i = 1; i != -1; i = next[i]) {
            list.addAll(ims.iterator(i));
        }

        IntExtGCDObject extgcd = new IntExtGCDObject();
        for (int i = 0; i < list.size(); i++) {
            int x = list.get(i);
            if (i == 0) {
                out.append(x).append(' ');
                continue;
            }
            int prev = list.get(i - 1);
            //prev * t = x
            int g = extgcd.extgcd(prev, m);
            int t = DigitUtils.modmul(x / g, extgcd.getX(), m);
            out.append(t).append(' ');
        }
    }
}
