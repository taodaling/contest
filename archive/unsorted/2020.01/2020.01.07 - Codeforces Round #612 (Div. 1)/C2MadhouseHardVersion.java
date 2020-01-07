package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Hash;
import template.utils.SequenceUtils;

import java.util.*;

public class C2MadhouseHardVersion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;

        int n = in.readInt();
        if (n == 1) {
            answer(actual(1));
            return;
        }

        int m = (n + 1) / 2;
        String left = actual(m);
        Map<Summary, Integer> total = ask(1, n);

        char[] ans = new char[n];
        Arrays.fill(ans, 'a');
        for (int i = 0; i < m; i++) {
            ans[i] = left.charAt(i);
        }

        Summary all = null;
        for (Summary s : total.keySet()) {
            if (s.sum == n) {
                all = s;
                total.remove(all);
                break;
            }
        }

        Summary last = all;
        for (int i = n - 1; i >= m; i--) {
            Summary trace = new Summary(all);
            int j;
            for (j = 0; i + j - 1 < n - 1; j++) {
                trace.dec(ans[j] - 'a');
            }
            for (; j >= 1; j--) {
                remove(total, trace);
                trace.inc(ans[j - 1] - 'a');
                trace.dec(ans[i + j - 1] - 'a');
            }
            Summary now = null;
            for (Summary s : total.keySet()) {
                if (s.sum == i) {
                    now = s;
                    total.remove(now);
                    break;
                }
            }

            ans[i] = (char) (last.differIndex(now) + 'a');
            last = now;
        }

        answer(new String(ans));
    }

    public void remove(Map<Summary, Integer> total, Summary s) {
        int remain = total.get(s) - 1;
        if (remain == 0) {
            total.remove(s);
        } else {
            total.put(s, remain);
        }
    }

    public String actual(int m) {
        if (m == 1) {
            Map<Summary, Integer> map = ask(1, 1);
            String s = "" + (char) (map.keySet().iterator().next().differIndex(new Summary("")) + 'a');
            return s;
        }
        Map<Summary, Integer> h1 = ask(1, m);
        Map<Summary, Integer> h2 = ask(2, m);
        for (Summary s : h2.keySet()) {
            int remain = h1.get(s) - h2.get(s);
            if (remain == 0) {
                h1.remove(s);
            } else {
                h1.put(s, remain);
            }
        }

        List<Summary> list = new ArrayList<>(h1.keySet());
        list.sort((a, b) -> a.sum - b.sum);
        Summary last = new Summary("");
        StringBuilder ans = new StringBuilder(m);
        for (Summary s : list) {
            ans.append((char) (s.differIndex(last) + 'a'));
            last = s;
        }
        return ans.toString();
    }

    FastInput in;
    FastOutput out;

    void answer(String ans) {
        out.printf("! %s", ans).println().flush();
    }

    Map<Summary, Integer> ask(int l, int r) {
        int n = r - l + 1;
        int m = n * (n + 1) / 2;
        Map<Summary, Integer> map = new HashMap<>(m);
        out.printf("? %d %d", l, r).println().flush();
        for (int i = 0; i < m; i++) {
            Summary s = new Summary(in.readString());
            map.put(s, map.getOrDefault(s, 0) + 1);
        }
        return map;
    }
}

class Summary {
    int[] cnts = new int['z' - 'a' + 1];
    int sum;

    public void dec(int i) {
        cnts[i]--;
        sum--;
    }

    public void inc(int i) {
        cnts[i]++;
        sum++;
    }

    public Summary(Summary model) {
        this.cnts = model.cnts.clone();
        this.sum = model.sum;
    }

    public Summary(String s) {
        sum = s.length();
        for (char c : s.toCharArray()) {
            cnts[c - 'a']++;
        }
    }

    public int differIndex(Summary s) {
        for (int i = 0; i < cnts.length; i++) {
            if (cnts[i] != s.cnts[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        for (int i = 0; i < cnts.length; i++) {
            hash = hash * 31 + cnts[i];
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Summary)) {
            return false;
        }
        Summary other = (Summary) obj;
        return SequenceUtils.equal(cnts, 0, cnts.length - 1,
                other.cnts, 0, cnts.length - 1);
    }

}
