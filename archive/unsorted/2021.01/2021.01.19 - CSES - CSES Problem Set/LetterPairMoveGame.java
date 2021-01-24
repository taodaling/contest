package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LetterPairMoveGame {
    char[] s;

    public boolean sorted() {
        boolean occur = false;
        for (char c : s) {
            if (c == 'B') {
                occur = true;
            }
            if (occur && c == 'A') {
                return false;
            }
        }
        return true;
    }

    List<char[]> op = new ArrayList<>();


    int indexOfDot() {
        return SequenceUtils.indexOf(s, 0, s.length - 1, '.');
    }

    Debug debug = new Debug(true);

    void apply(int i) {
        int index = indexOfDot();
        SequenceUtils.swap(s, index, i);
        SequenceUtils.swap(s, index + 1, i + 1);
        op.add(s.clone());
        //debug.debug("s", new String(s));
    }

    public boolean solveL(int l, int r) {
        if (sorted()) {
            return true;
        }
        if (r - l + 1 <= 2) {
            return false;
        }
        if (s[l] == 'A') {
            return solveL(l + 1, r);
        }
        int nextA = SequenceUtils.indexOf(s, l + 2, r, 'A');
        if (nextA == -1) {
            return false;
        }
        if (nextA == r) {
            //special
            apply(l);
            return solveR(l + 2, r);
        }
        //rotate
        apply(nextA);
        apply(l);
        apply(r + 1);
        return solveL(l + 1, r);
    }

    public boolean solveR(int l, int r) {
        if (sorted()) {
            return true;
        }
        if (r - l + 1 <= 2) {
            return false;
        }
        if (s[r] == 'B') {
            return solveR(l, r - 1);
        }
        int lastB = SequenceUtils.lastIndexOf(s, l, r - 2, 'B');
        if (lastB == -1) {
            return false;
        }
        if (lastB == l) {
            //special
            apply(r - 1);
            return solveR(l, r - 2);
        }
        //rotate
        apply(lastB - 1);
        apply(r - 1);
        apply(l - 2);
        return solveR(l, r - 1);
    }

    public boolean tryCase() {
        int indexOfDot = indexOfDot();
        if (indexOfDot > 0 && indexOfDot < s.length - 2) {
            if (indexOfDot >= 2) {
                apply(0);
            } else {
                apply(s.length - 2);
            }
        }
        boolean ans;
        indexOfDot = indexOfDot();
        if (indexOfDot == 0) {
            ans = solveR(2, s.length - 1);
        } else {
            ans = solveL(0, s.length - 3);
        }
        if (!ans) {
            return false;
        }
        return true;

    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[2 * n];
        in.rs(s);
        this.s = s.clone();
        if (sorted()) {
            out.println(0);
            return;
        }
        if (n == 2) {
            out.println(-1);
            return;
        }

        for (int i = 0; i < 100; i++) {
            op.clear();
            this.s = s.clone();
            if (i > 0) {
                for (int j = 0; j < 100; j++) {
                    int index = Randomized.nextInt(0, n - 2);
                    if(this.s[index] == '.' || this.s[index + 1] == '.'){
                        continue;
                    }
                    apply(index);
                }
            }
            if(!tryCase()){
                continue;
            }
            out.println(op.size());
            for (char[] seq : op) {
                out.println(seq);
            }
            return;
        }
        out.println(-1);
    }
}
