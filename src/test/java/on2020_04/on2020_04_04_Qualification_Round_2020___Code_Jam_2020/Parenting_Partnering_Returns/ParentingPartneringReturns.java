package on2020_04.on2020_04_04_Qualification_Round_2020___Code_Jam_2020.Parenting_Partnering_Returns;



import template.graph.TwoSatBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class ParentingPartneringReturns {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        int n = in.readInt();
        int[][] times = new int[n][2];
        for (int i = 0; i < n; i++) {
            times[i][0] = in.readInt();
            times[i][1] = in.readInt() - 1;
        }
        TwoSatBeta ts = new TwoSatBeta(n, n * n * 2);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (collide(times[i], times[j])) {
                    ts.xor(ts.elementId(i), ts.elementId(j));
                    debug.debug("i", i);
                    debug.debug("j", j);
                }
            }
        }
        if(!ts.solve(true)){
            out.println("IMPOSSIBLE");
            return;
        }

        debug.debug("times", times);
        debug.debug("ts", ts);

        for(int i = 0; i < n; i++){
            if(ts.valueOf(i)){
                out.append('C');
            }else{
                out.append('J');
            }
        }
        out.println();
    }

    public boolean collide(int[] a, int[] b) {
        if(Math.max(a[0], b[0]) <= Math.min(a[1], b[1])){
            return true;
        }
        return false;
    }
}
