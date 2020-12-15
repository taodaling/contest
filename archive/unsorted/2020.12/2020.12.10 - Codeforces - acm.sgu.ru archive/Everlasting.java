package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorial;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerArrayList;

public class Everlasting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        if(calc(a) > calc(b)){
            out.println('a');
        }else{
            out.println('b');
        }
    }

    public int calc(int x){
        IntegerArrayList list = Factorization.factorizeNumberPrime(x);
        int sum = 0;
        int max = 0;
        for(int t : list.toArray()){
            max = Math.max(t, max);
            sum += t;
        }
        return max - (sum - max);
    }
}
