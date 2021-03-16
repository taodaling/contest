package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayDeque;
import java.util.Deque;

public class DMOPC20Contest4P4JavelinThrowing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int cnt = 0;
        IntegerArrayList seq = new IntegerArrayList(n);
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += cnt;
            while (cnt < a[i]) {
                cnt++;
                int last = seq.pop();
                sum += (i - last + 1);
            }
            seq.add(i);
            if (cnt < a[i] + 1) {
                cnt++;
                int last = seq.pop();
                sum += (i - last + 1);
            }
            sum -= 1 + a[i];
            out.append(sum);
            if(i + 1 < n){
                out.append(' ');
            }
        }
        out.println();
    }
}
