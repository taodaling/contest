package on2020_07.on2020_07_04_Codeforces___Codeforces_Global_Round_9.C__Element_Extermination;



import template.io.FastInput;
import template.io.FastOutput;

public class CElementExtermination {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        if(a[0] < a[n - 1]){
            out.println("YES");
        }else{
            out.println("NO");
        }
    }
}
