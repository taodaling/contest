package on2021_04.on2021_04_25_Codeforces___Codeforces_Round__200__Div__1_.B__Alternating_Current;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Deque;

public class BAlternatingCurrent {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[100000];
        int n = in.rs(s);
        String no = "No";
        String yes = "Yes";
        if (n % 2 != 0) {
            out.println(no);
            return;
        }
        Deque<Character> dq = new ArrayDeque<>(n);
        for (int i = 0; i < n; i++) {
            if (!dq.isEmpty() && dq.peekLast() == s[i]) {
                dq.removeLast();
            } else {
                dq.addLast(s[i]);
            }
        }
        if(dq.isEmpty()) {
            out.println(yes);
        }else{
            out.println(no);
        }
    }
}
