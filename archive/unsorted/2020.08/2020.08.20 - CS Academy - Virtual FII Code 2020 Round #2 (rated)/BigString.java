package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BigString {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        for (int i = 0; i < s.length; i++) {
            s[i] -= 'a';
        }
        int k = in.readInt();
        if (s.length == 1) {
            long[] count = count(s, 0, 1);
            output(count, out);
            return;
        }

        State state = new State();
        state.a = s[0];
        state.b = s[s.length - 1];
        state.cnts = count(s, 1, s.length - 2);

        for (int i = 0; i < k; i++) {
            state = next(state);
        }

        long[] cnts = state.cnts;
        cnts[state.a]++;
        cnts[state.b]++;
        output(cnts, out);
    }

    public State next(State s) {
        State ans = new State();
        ans.cnts = new long[charset];
        ans.a = s.a;
        ans.b = s.a;
        ans.cnts[s.b]++;
        for (int i = 0; i < charset; i++) {
            ans.cnts[i] += s.cnts[i] * 2;
        }
        return ans;
    }

    public void output(long[] s, FastOutput out) {
        for (int i = 0; i < s.length; i++) {
            out.append(s[i]).append(' ');
        }
    }

    int charset = 'z' - 'a' + 1;

    public long[] count(char[] s, int l, int r) {
        long[] cnts = new long[charset];
        for (int i = l; i <= r; i++) {
            cnts[s[i]]++;
        }
        return cnts;
    }
}

class State {
    int a;
    int b;
    long[] cnts;
}