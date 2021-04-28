package on2021_04.on2021_04_21_Codeforces___Codeforces_Round__201__Div__1_.A__Alice_and_Bob;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;

public class AAliceAndBob {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int g = 0;
        int max = 0;
        for(int x : a){
            max = Math.max(x, max);
            g = GCDs.gcd(g, x);
        }
        int total = max / g;
        int round = total - n;
        if(round % 2 == 0){
            out.println("Bob");
        }else{
            out.println("Alice");
        }
    }
}
