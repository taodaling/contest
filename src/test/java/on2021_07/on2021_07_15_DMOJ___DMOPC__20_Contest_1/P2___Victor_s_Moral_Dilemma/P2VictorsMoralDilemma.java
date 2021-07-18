package on2021_07.on2021_07_15_DMOJ___DMOPC__20_Contest_1.P2___Victor_s_Moral_Dilemma;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;

public class P2VictorsMoralDilemma {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int d = in.ri();
        int[] a = in.ri(n);
        IntegerPreSum ps = new IntegerPreSum(i -> a[i], n);
        int l = 0;
        int r = n - 1;
        for (int i = 0; i < d; i++) {
            int size = in.ri();
            int left = ps.intervalSum(l, l + size - 1);
            int right = ps.intervalSum(l + size, r);
            if(left >= right){
                l = l + size;
            }else{
                r = l + size - 1;
            }
            out.println(Math.max(left, right));
        }
    }
}
