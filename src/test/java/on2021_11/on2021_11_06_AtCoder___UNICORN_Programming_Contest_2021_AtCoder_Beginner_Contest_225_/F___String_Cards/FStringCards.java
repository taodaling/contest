package on2021_11.on2021_11_06_AtCoder___UNICORN_Programming_Contest_2021_AtCoder_Beginner_Contest_225_.F___String_Cards;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FStringCards {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        String[] Ss = new String[n];
        for (int i = 0; i < n; i++) {
            Ss[i] = in.rs();
        }
        Arrays.sort(Ss, (a, b) -> (a + b).compareTo(b + a));

        State[] prev = new State[k + 1];
        State[] next = new State[k + 1];
        State buf = new State();
        for (int i = 0; i <= k; i++) {
            prev[i] = new State();
            next[i] = new State();
        }

//        HashData[] hds = HashData.doubleHashData(2500);
//        IntRangeHash hash = new IntRangeHash(hds[0], hds[1], 2500);
        for (int index = 0; index < Ss.length; index++) {
            String s = Ss[index];
            char[] cs = s.toCharArray();
//            hash.populate(i -> cs[i], cs.length);
//            long sHash = hash.hash(0, cs.length - 1);
            for (int j = 0; j <= k; j++) {
                next[j].init();
            }
            for (int j = 0; j <= k; j++) {
                //not use
                State state = prev[j];
//                hash.populate(t -> state.s[t], state.len);
                chmax(next[j], state);
                //use
                if (j + 1 <= k && state.len > 0) {
                    buf.init();
//                    boolean optimized = false;
                    System.arraycopy(state.s, 0, buf.s, 0, state.len);
                    System.arraycopy(cs, 0, buf.s, state.len, cs.length);
                    buf.len = state.len + cs.length;
                    buf.leaf[buf.len - 1] = true;
                    for (int t = 0; t < state.len - 1 && state.s[t] == buf.s[t]; t++) {
//                        if (!optimized) {
//                            if (t + cs.length <= state.len && hash.hash(t, t + cs.length - 1) == sHash) {
//                                buf.leaf[t + cs.length - 1] = true;
//                                buf.len = t + cs.length;
//                            } else {
                        if (state.leaf[t]) {
                            append(buf, cs, t + 1);
                        }
//                            }
//                        }
                    }
                    chmax(next[j + 1], buf);
                }
            }


            buf.init();
            append(buf, cs, 0);
            chmax(next[1], buf);
            debug.debug("s", s);
            for (int i = 0; i <= k; i++) {
                debug.debug("i", i);
                debug.debug("state", next[i]);
            }
            State[] tmp = prev;
            prev = next;
            next = tmp;
        }

        State ans = prev[k];
        int first = 0;
        while (!ans.leaf[first]) {
            first++;
        }
        String res = new String(ans.s, 0, first + 1);
        out.println(res);
    }

    Debug debug = new Debug(false);

    public void append(State a, char[] b, int start) {
        int pos = 0;
        while (pos + start < a.len && pos < b.length && a.s[pos + start] == b[pos]) {
            pos++;
        }
        if (pos + start >= a.len || pos < b.length && a.s[pos + start] > b[pos]) {
            while (pos < b.length) {
                a.s[pos + start] = b[pos];
                a.leaf[pos + start] = false;
                pos++;
            }
            pos += start;
            a.leaf[pos - 1] = true;
            while (pos < a.len) {
                a.leaf[pos] = false;
                pos++;
            }
        } else if(pos == b.length){
            a.leaf[start + pos - 1] = true;
        }
        a.calcLen(Math.max(a.len, start + b.length));
    }

    public void chmax(State a, State b) {
        int pos = 0;
        while (pos < a.len && pos < b.len && a.s[pos] == b.s[pos]) {
            a.leaf[pos] = a.leaf[pos] || b.leaf[pos];
            pos++;
        }
        if (pos >= a.len || pos < b.len && a.s[pos] > b.s[pos]) {
            while (pos < b.len) {
                a.s[pos] = b.s[pos];
                a.leaf[pos] = b.leaf[pos];
                pos++;
            }
            while (pos < a.len) {
                a.leaf[pos] = false;
                pos++;
            }
        }
        a.calcLen(Math.max(a.len, b.len));
    }
}

class State {
    char[] s;
    boolean[] leaf;
    int len;

    public void init() {
        while (len > 0) {
            len--;
            leaf[len] = false;
        }
    }

    public void calcLen() {
        calcLen(s.length);
    }

    public void calcLen(int start) {
        len = start - 1;
        while (len >= 0 && !leaf[len]) {
            len--;
        }
        len++;
    }

    public State() {
        s = new char[2500];
        leaf = new boolean[2500];
    }

    @Override
    public String toString() {
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(s[i]);
            if (leaf[i]) {
                res.add(sb.toString());
                sb.setLength(0);
            }
        }
        return res.toString();
    }
}