package on2020_04.on2020_04_12_TopCoder_SRM__783.RecursiveTournament0;



import template.binary.Bits;
import template.math.Modular;
import template.math.Power;
import template.utils.Debug;

public class RecursiveTournament {
    public int count(String[] graph, int k) {
        int n = graph.length;
        int[] edges = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                edges[i] = Bits.setBit(edges[i], j, graph[i].charAt(j) == 'Y');
            }
            edges[i] = Bits.setBit(edges[i], i, true);
        }
        int mask = (1 << n) - 1;
        int[] scc = new int[mask + 1];
        scc[0] = mask;
        for (int i = 1; i <= mask; i++) {
            for (int j = 0; j < n; j++) {
                if (Bits.bitAt(i, j) == 0) {
                    continue;
                }
                scc[i] = Bits.setBit(scc[i], j, (scc[i - (1 << j)] & edges[j]) != 0);
            }
        }
        int[] cnt = new int[n + 1];
        for (int i = 1; i <= mask; i++) {
            if (scc[i] == i) {
                cnt[Integer.bitCount(i)]++;
            }
        }

        Modular mod = new Modular(998244353);
        Power power = new Power(mod);
        Modular powMod = mod.getModularForPowerComputation();
        Power powModPower = new Power(powMod);

        int ans = power.pow(n, k);
        for (int i = k; i >= 1; i--) {
            int way = power.pow(n, k - i);
            int children = powModPower.pow(n, i - 1);
            int nonEmptySubset = mod.subtract(power.pow(2, children), 1);
            int local = 0;
            for (int j = 2; j <= n; j++) {
                int contrib = mod.mul(cnt[j], power.pow(nonEmptySubset, j));
                local = mod.plus(local, contrib);
            }
            ans = mod.plus(ans, mod.mul(local, way));
            //debug.debug("contrib", "contrib " + i + " is " + mod.mul(local, way));
        }

        return ans;
    }
}
