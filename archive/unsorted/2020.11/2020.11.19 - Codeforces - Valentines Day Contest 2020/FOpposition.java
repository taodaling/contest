package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitCount;
import template.math.DigitUtils;
import template.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FOpposition {
    FastInput in;
    FastOutput out;
    int remain;
    char[] s = new char[(int) 2e5];
    int n;
    List<Integer>[] goods = new List[4];
    String target = "LOVE";
    String[] pattern = new String[]{
            "?OVE", "L?VE", "LO?E", "LOV?"
    };
    int scan;

    {
        for (int i = 0; i < 4; i++) {
            goods[i] = new ArrayList<>();
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            if (s[i] != '?') {
                continue;
            }
            remain++;
            for (int j = 0; j < 4; j++) {
                if (equal(i - j, pattern[j])) {
                    goods[j].add(i);
                }
            }
        }
        int t = in.readInt();
        this.in = in;
        this.out = out;

        if (t == 1) {
            first();
        } else {
            second();
        }
    }

    public boolean equal(int i, String cand) {
        for (int j = 0; j < cand.length(); j++) {
            if (i + j < 0 || i + j >= n || cand.charAt(j) != s[i + j]) {
                return false;
            }
        }
        return true;
    }

    public int next() {
        while (s[scan] != '?') {
            scan++;
        }
        return scan++;
    }

    public void apply(int p, char x) {
        assert s[p] == '?';
        s[p] = x;
        remain--;

        for (int i = p - 3; i <= p + 3; i++) {
            if (i < 0 || i >= n || s[i] != '?') {
                continue;
            }
            for (int j = 0; j < 4; j++) {
                if (equal(i - j, pattern[j])) {
                    goods[j].add(i);
                }
            }
        }

    }

    public void operate(int p, char x) {
        apply(p, x);
        out.append(p + 1).append(' ').append(x).println().flush();
        respond();
    }

    public void respond() {
        if (remain > 0) {
            int t = in.ri() - 1;
            char v = in.rc();
            apply(t, v);
        }
    }

    public void first() {
        while (remain > 0) {
            boolean find = false;
            for (int i = 0; i < 4; i++) {
                while (!goods[i].isEmpty() && s[CollectionUtils.peek(goods[i])] != '?') {
                    CollectionUtils.pop(goods[i]);
                }
                if (goods[i].isEmpty()) {
                    continue;
                }
                find = true;
                operate(CollectionUtils.pop(goods[i]), target.charAt(i));
                break;
            }
            if (!find) {
                operate(next(), 'L');
            }
        }

    }

    public void second() {
        respond();
        while (remain > 0) {
            boolean find = false;
            for (int i = 0; i < 4; i++) {
                while (!goods[i].isEmpty() && s[CollectionUtils.peek(goods[i])] != '?') {
                    CollectionUtils.pop(goods[i]);
                }
                if (goods[i].isEmpty()) {
                    continue;
                }
                find = true;
                operate(CollectionUtils.pop(goods[i]), target.charAt(i) == 'V' ? 'E' : 'V');
                break;
            }
            if (!find) {
                operate(next(), 'E');
            }
        }
    }

}
