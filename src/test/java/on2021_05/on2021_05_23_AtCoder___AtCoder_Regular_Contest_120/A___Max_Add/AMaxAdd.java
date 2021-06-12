package on2021_05.on2021_05_23_AtCoder___AtCoder_Regular_Contest_120.A___Max_Add;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;

public class AMaxAdd {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        LongPreSum ps = new LongPreSum(i -> a[i], n);
        long max = 0;
        long sum = 0;
        for(int i = 0; i < n; i++){
            max = Math.max(max, a[i]);
            sum += ps.prefix(i);
            out.println(max * (i + 1) + sum);
        }
    }
}
