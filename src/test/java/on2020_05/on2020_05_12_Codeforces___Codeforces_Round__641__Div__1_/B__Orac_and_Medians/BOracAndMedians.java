package on2020_05.on2020_05_12_Codeforces___Codeforces_Round__641__Div__1_.B__Orac_and_Medians;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerPreSum;

public class BOracAndMedians {
    String yes = "yes";
    String no = "no";

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        IntegerList zero = new IntegerList(n);

        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            if (a[i] == k) {
                zero.add(i);
            }
            a[i] = a[i] < k ? -1 : 1;
        }

        if (zero.isEmpty()) {
            out.println(no);
            return;
        }
        if(zero.size() == n){
            out.println(yes);
            return;
        }

        IntegerPreSum ips = new IntegerPreSum(i -> a[i], n);
        int min = (int) 1e9;
        for (int i = 0; i < n; i++) {
            if (ips.prefix(i) - min > 0) {
                out.println(yes);
                return;
            }
            min = Math.min(min, ips.prefix(i - 1));
        }

        out.println(no);
    }
}
