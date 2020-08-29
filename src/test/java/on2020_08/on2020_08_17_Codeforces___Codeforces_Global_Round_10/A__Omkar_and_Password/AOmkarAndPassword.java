package on2020_08.on2020_08_17_Codeforces___Codeforces_Global_Round_10.A__Omkar_and_Password;



import template.io.FastInput;
import template.io.FastOutput;

public class AOmkarAndPassword {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        for(int i = 1; i < n; i++){
            if(a[i] != a[i - 1]){
                out.println(1);
                return;
            }
        }
        out.println(n);
    }
}
