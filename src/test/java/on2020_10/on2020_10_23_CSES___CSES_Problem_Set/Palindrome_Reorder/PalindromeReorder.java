package on2020_10.on2020_10_23_CSES___CSES_Problem_Set.Palindrome_Reorder;



import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerIterator;

import java.io.PrintWriter;

public class PalindromeReorder {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        char[] s = new char[(int) 1e6];
        int n = in.readString(s, 0);
        int[] cnts = new int[128];
        for (int i = 0; i < n; i++) {
            cnts[s[i]]++;
        }
        int oddCnt = 0;
        for (int i = 0; i < 128; i++) {
            oddCnt += cnts[i] & 1;
        }
        if (oddCnt > 1) {
            out.println("NO SOLUTION");
            return;
        }
        IntegerDeque dq = new IntegerDequeImpl(n);
        for (int i = 0; i < 128; i++) {
            if (cnts[i] % 2 == 1) {
                dq.addLast(i);
            }
        }
        for (int i = 0; i < 128; i++) {
            for (int j = 1; j * 2 <= cnts[i]; j++) {
                dq.addFirst(i);
                dq.addLast(i);
            }
        }
        for (IntegerIterator iterator = dq.iterator(); iterator.hasNext(); ) {
            out.append((char) iterator.next());
        }
    }
}
