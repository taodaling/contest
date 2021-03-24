package on2021_03.on2021_03_21_AtCoder___AtCoder_Regular_Contest_115.TaskC;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerMultiWayStack stack = Factorization.factorizeRangePrime(n);
        IntegerArrayList list = new IntegerArrayList(n);
        for (int i = 1; i <= n; i++) {
            int pow = 0;
            list.clear();
            list.addAll(stack.iterator(i));
            for (int x : list.toArray()) {
                int y = i;
                while (y % x == 0) {
                    y /= x;
                    pow++;
                }
            }
            out.append(pow + 1).append(' ');
        }
    }
}
