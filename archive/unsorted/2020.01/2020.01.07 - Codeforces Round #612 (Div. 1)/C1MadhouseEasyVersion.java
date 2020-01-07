package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class C1MadhouseEasyVersion {
    FastInput in;
    FastOutput out;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;

        int n = in.readInt();
        if (n == 1) {
            Map<String, Integer> map = read(1, 1);
            String s = map.keySet().iterator().next();
            answer(s);
            return;
        }

        Map<String, Integer> a = read(1, n);
        Map<String, Integer> b = read(2, n);

        for (String key : b.keySet()) {
            int remain = a.get(key) - b.get(key);
            if (remain == 0) {
                a.remove(key);
            } else {
                a.put(key, remain);
            }
        }

        List<String> prefix = new ArrayList<>(a.keySet());
        prefix.sort((x, y) -> x.length() - y.length());

        StringBuilder ans = new StringBuilder(n);
        String last = "";
        for (String s : prefix) {
            ans.append(differ(last, s));
            last = s;
        }

        answer(ans.toString());
    }

    public void answer(String s){
        out.printf("! %s", s).println().flush();
    }

    public char differ(String shorter, String longer) {
        int differ = 0;
        while (shorter.length() > differ && shorter.charAt(differ) == longer.charAt(differ)) {
            differ++;
        }
        return longer.charAt(differ);
    }

    public Map<String, Integer> read(int l, int r) {
        int n = (r - l + 1);
        int m = (n + 1) * n / 2;
        Map<String, Integer> map = new HashMap<>(m);
        out.printf("? %d %d", l, r).println().flush();
        for (int i = 0; i < m; i++) {
            String s = arrange(in.readString());
            map.put(s, map.getOrDefault(s, 0) + 1);
        }
        return map;
    }

    String arrange(String x) {
        char[] s = x.toCharArray();
        Randomized.shuffle(s);
        Arrays.sort(s);
        return new String(s);
    }
}
