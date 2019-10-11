package contest;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import template.DigitUtils;
import template.FastInput;

public class TaskB {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int h = in.readInt();
        int w = in.readInt();
        int n = in.readInt();
        Set<Long> set = new HashSet<>(n);
        for (int i = 0; i < n; i++) {
            set.add(DigitUtils.asLong(in.readInt(), in.readInt()));
        }
        Map<Long, Integer> cntMaps = new HashMap<>(9 * n);

        for (Long xy : set) {
            int x = DigitUtils.highBit(xy);
            int y = DigitUtils.lowBit(xy);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int cnt = 0;
                    int xx = x + i;
                    int yy = y + j;
                    if (cntMaps.containsKey(DigitUtils.asLong(xx, yy))) {
                        continue;
                    }
                    if (xx - 1 < 1 || xx + 1 > h || yy - 1 < 1 || yy + 1 > w) {
                        continue;
                    }
                    for (int k = -1; k <= 1; k++) {
                        for (int t = -1; t <= 1; t++) {
                            cnt += set.contains(DigitUtils.asLong(xx + k, yy + t)) ? 1 : 0;
                        }
                    }
                    cntMaps.put(DigitUtils.asLong(xx, yy), cnt);
                }
            }
        }

        long[] numbers = new long[10];
        for (int c : cntMaps.values()) {
            numbers[c]++;
        }
        numbers[0] = (long) (h - 2) * (w - 2);
        for (int i = 1; i < 10; i++) {
            numbers[0] -= numbers[i];
        }

        for (int i = 0; i < 10; i++) {
            out.println(numbers[i]);
        }
    }


}
