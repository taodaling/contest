package contest;

import com.sun.org.apache.xpath.internal.operations.Mod;
import template.math.ModLinearFunction;
import template.math.Power;
import template.string.AhoCorasick;

import java.util.Arrays;

public class RollMe {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

    public int solve(int[] die, String goal) {
        int sum = Arrays.stream(die).sum();
        long invSum = pow.inverse(sum);
        int n = die.length;
        long[] prob = new long[n];
        for (int i = 0; i < n; i++) {
            prob[i] = die[i] * invSum % mod;
        }
        char[] s = goal.toCharArray();
        AhoCorasick ac = new AhoCorasick(0, n - 1, s.length);
        ac.prepareBuild();
        for (int i = 0; i < s.length; i++) {
            ac.build(s[i] - '0');
        }
        int[] topo = ac.endBuild();
        int m = topo.length;
        ModLinearFunction[] func = new ModLinearFunction[m];
        func[0] = new ModLinearFunction(1, 0);
        for (int i = 0; i < m - 1; i++) {
            long next = 0;
            ModLinearFunction left = new ModLinearFunction(func[i].a, func[i].b);
            left = ModLinearFunction.subtract(left, 0, 1, mod);
            for (int j = 0; j < n; j++) {
                int to = ac.next[j][i];
                if (to == i + 1) {
                    next += prob[j];
                    continue;
                }
                ModLinearFunction f = ModLinearFunction.mul(func[to], (int) prob[j], mod);
                left = ModLinearFunction.subtract(left, f, mod);
            }
            next %= mod;
            func[i + 1] = ModLinearFunction.mul(left, pow.inverse((int) next), mod);
        }
        long a = func[m - 1].a;
        long b = func[m - 1].b;
        long x = (mod - b) * pow.inverse((int) a) % mod;
        return (int)x;
    }
}
