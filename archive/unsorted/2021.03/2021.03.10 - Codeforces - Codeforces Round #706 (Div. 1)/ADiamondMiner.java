package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahanSummation;
import template.primitve.generated.datastructure.IntegerArrayList;

public class ADiamondMiner {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerArrayList y = new IntegerArrayList(n);
        IntegerArrayList x = new IntegerArrayList(n);
        for(int i = 0; i < 2 * n; i++){
            int a = in.ri();
            int b = in.ri();
            if(a == 0){
                y.add(Math.abs(b));
            }else{
                x.add(Math.abs(a));
            }
        }
        x.sort();
        y.sort();
        KahanSummation sum = new KahanSummation();
        for(int i = 0; i < n; i++){
            sum.add(dist(x.get(i), y.get(i)));
        }
        out.println(sum.sum());
    }

    public double dist(double x, double y){
        return Math.sqrt(x * x + y * y);
    }
}
