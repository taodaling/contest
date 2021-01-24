package on2021_01.on2021_01_19_Codeforces___Educational_Codeforces_Round_102__Rated_for_Div__2_.C__No_More_Inversions;



import template.datastructure.Treap;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class CNoMoreInversions {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int low = k - (n - k);
        for(int i = 1; i < low; i++){
            out.append(i).append(' ');
        }
        for(int i = k; i >= low; i--){
            out.append(i).append(' ');
        }
        out.println();
    }

}
