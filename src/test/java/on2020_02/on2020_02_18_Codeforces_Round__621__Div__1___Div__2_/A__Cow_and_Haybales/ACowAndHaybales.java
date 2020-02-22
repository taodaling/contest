package on2020_02.on2020_02_18_Codeforces_Round__621__Div__1___Div__2_.A__Cow_and_Haybales;



import template.io.FastInput;
import template.io.FastOutput;

public class ACowAndHaybales {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int d = in.readInt();
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
        }
        int remain = d;
        int ans = a[0];
        for(int i = 1; i < n; i++){
            int unit = i;
            if(remain < unit){
                break;
            }
            int move = Math.min(a[i], remain / unit);
            remain -= move * unit;
            ans += move;
        }

        out.println(ans);
    }
}
