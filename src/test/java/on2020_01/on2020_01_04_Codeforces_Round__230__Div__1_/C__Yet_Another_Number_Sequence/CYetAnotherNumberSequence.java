package on2020_01.on2020_01_04_Codeforces_Round__230__Div__1_.C__Yet_Another_Number_Sequence;




import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModLinearRecurrenceSolver;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerArrayList;

public class CYetAnotherNumberSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        int k = in.readInt();

        int mod = 1000000007;
        Power power = new Power(mod);

        int items = 200;
        int[] fib = new int[items];
        fib[0] = 1;
        fib[1] = 2;
        int[] a = new int[items];
        for (int i = 2; i < items; i++) {
            fib[i] = (fib[i - 1] + fib[i - 2]) % mod;
        }
        for (int i = 0; i < items; i++) {
            long sum = (long)fib[i] * power.pow(i + 1, k);
            if (i > 0) {
                sum += a[i - 1];
            }
            a[i] = (int) (sum % mod);
        }

        IntegerArrayList seq = new IntegerArrayList();
        seq.addAll(a);
        ModLinearRecurrenceSolver solver = ModLinearRecurrenceSolver.newSolverByAutoDetectLinearRecurrence(seq, mod);
        int ans = solver.solve(n - 1, seq);

        out.println(ans);
    }
}
