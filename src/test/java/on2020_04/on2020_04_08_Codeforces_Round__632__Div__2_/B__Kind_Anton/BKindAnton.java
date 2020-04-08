package on2020_04.on2020_04_08_Codeforces_Round__632__Div__2_.B__Kind_Anton;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class BKindAnton {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n + 1];
        int[] min = new int[n + 1];
        int[] max = new int[n + 1];

        for(int i = 1; i <= n; i++){
            a[i] = in.readInt();
            min[i] = Math.min(min[i - 1], a[i]);
            max[i] = Math.max(max[i - 1], a[i]);
        }

        int[] b = new int[n + 1];
        for(int i = 1; i <= n; i++){
            b[i] = in.readInt();
        }


        debug.debug("min", min);
        debug.debug("max", max);
        for(int i = 1; i <= n; i++){
            if(a[i] < b[i] && max[i - 1] <= 0 || a[i] > b[i] && min[i - 1] >= 0){
                out.println("NO");
                return;
            }
        }


        out.println("YES");
    }
}
