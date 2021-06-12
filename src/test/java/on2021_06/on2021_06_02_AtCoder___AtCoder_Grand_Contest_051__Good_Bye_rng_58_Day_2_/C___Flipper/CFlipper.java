package on2021_06.on2021_06_02_AtCoder___AtCoder_Grand_Contest_051__Good_Bye_rng_58_Day_2_.C___Flipper;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.*;
import template.utils.Debug;

public class CFlipper {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerHashMap rows = new IntegerHashMap(n, false);
        IntegerHashMap xor = new IntegerHashMap(n, false);
        int[] XYZ = new int[3];
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            int y = in.ri();
            xor.modify(y, 1);
            rows.put(x, rows.getOrDefault(x, 0) ^ (1 << y % 3));
        }
        for (IntegerEntryIterator iterator = xor.iterator(); iterator.hasNext(); ) {
            iterator.next();
            if (iterator.getEntryValue() % 2 == 1) {
                XYZ[iterator.getEntryKey() % 3]++;
            }
        }
        int[] stateCnt = new int[1 << 3];
        int mask = (1 << 3) - 1;
        for (IntegerEntryIterator iterator = rows.iterator(); iterator.hasNext(); ) {
            iterator.next();
            int v = iterator.getEntryValue();
            if (Integer.bitCount(v) > 1) {
                v = v ^ mask;
            }
            stateCnt[v]++;
        }
        int possible = 0;
        for (int i = 0; i < stateCnt.length; i++) {
            if (stateCnt[i] == 0) {
                continue;
            }
            possible |= 1 << i;
        }
        debug.debug("stateCnt", stateCnt);
        debug.debug("XYZ", XYZ);
        int best = n;
        for (int pick = possible + 1; pick > 0; ) {
            pick = (pick - 1) & possible;
            int[] each = new int[3];
            for (int j = 0; j < stateCnt.length; j++) {
                add(each, j, stateCnt[j] - Bits.get(pick, j));
                add(each, mask ^ j, Bits.get(pick, j));
            }
            boolean valid = true;
            for (int j = 0; j < 3; j++) {
                if (each[j] % 2 != XYZ[j] % 2) {
                    valid = false;
                }
            }
            if (!valid) {
                continue;
            }
            IntegerArrayList over = new IntegerArrayList(3);
            for (int i = 0; i < 3; i++) {
                if (each[i] >= XYZ[i]) {
                    over.add(i);
                }
            }
            if (over.size() == 1) {
                int index = over.get(0);
                int able = each[index] - XYZ[index];
                able = Math.min(able, stateCnt[1 << index] - Bits.get(pick, 1 << index));
                for (int j = 0; j < 3; j++) {
                    if (index == j) {
                        continue;
                    }
                    able = Math.min(able, XYZ[j] - each[j]);
                }
                able = (able / 2) * 2;
                add(each, 1 << index, -able);
                add(each, (1 << index) ^ mask, able);
            }

            int contrib = 0;
            for (int j = 0; j < 3; j++) {
                contrib += Math.max(XYZ[j], each[j]);
            }

            best = Math.min(best, contrib);
        }

        out.println(best);
    }

    void add(int[] each, int x, int time) {
        for (int i = 0; i < 3; i++) {
            each[i] += Bits.get(x, i) * time;
        }
    }

    Debug debug = new Debug(false);
}
