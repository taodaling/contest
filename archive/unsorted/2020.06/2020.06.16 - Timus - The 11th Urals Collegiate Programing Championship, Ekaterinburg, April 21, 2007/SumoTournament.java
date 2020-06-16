package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.HashMap;
import java.util.Map;

public class SumoTournament {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = 1 << n;
        Map<String, Integer> cnt = new HashMap<>();
        for (int i = 0; i < m; i++) {
            String name = in.readString();
            String city = in.readString();
            cnt.put(city, cnt.getOrDefault(city, 0) + 1);
        }

        int max = 0;
        for (Integer num : cnt.values()) {
            max = Math.max(num, max);
        }

        int round = 0;
        while (m >= 2 * max) {
            round++;
            m /= 2;
        }

        out.println(round);
    }
}
