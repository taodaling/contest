package on2020_09.on2020_09_18_Codeforces___Divide_by_Zero_2017_and_Codeforces_Round__399__Div__1___Div__2__combined_.E__Game_of_Stones;



import template.binary.Bits;
import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashMap;

public class EGameOfStones {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long state = 0;
        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            state ^= 1L << in.readInt();
        }

        long sg = 0;
        for (int i = 1; i <= 60; i++) {
            if (Bits.get(state, i) == 0) {
                continue;
            }
            map.clear();
            sg ^= solve(0, i);
        }

        out.println(sg == 0 ? "YES" : "NO");
    }

    LongHashMap map = new LongHashMap((int) 1e6, false);

    public long solve(long cur, int remain) {
        long ans = map.getOrDefault(cur, -1);
        if (ans == -1) {
            long next = 0;
            for (int i = 1; i <= remain; i++) {
                if (Bits.get(cur, i) == 1) {
                    continue;
                }
                next |= 1L << solve(Bits.set(cur, i), remain - i);
            }
            ans = Log2.floorLog(Long.lowestOneBit(~next));
            map.put(cur, ans);
        }
        return ans;
    }
}
