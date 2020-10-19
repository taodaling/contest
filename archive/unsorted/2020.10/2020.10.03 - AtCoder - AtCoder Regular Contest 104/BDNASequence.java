package contest;

import template.io.FastInput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;

import java.io.PrintWriter;

public class BDNASequence {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        String s = in.readString();

        int at = 0;
        int cg = 0;

        long ans = 0;
        LongHashMap map = new LongHashMap(n * 2, false);
        map.put(DigitUtils.asLong(at, cg), 1);
        for (char c : s.toCharArray()) {
            if (c == 'A') {
                at++;
            } else if (c == 'T') {
                at--;
            } else if (c == 'C') {
                cg++;
            } else if (c == 'G') {
                cg--;
            }


            long key = DigitUtils.asLong(at, cg);
            ans += map.get(key);
            map.put(key, map.getOrDefault(key, 0) + 1);
        }

        out.println(ans);
    }
}
