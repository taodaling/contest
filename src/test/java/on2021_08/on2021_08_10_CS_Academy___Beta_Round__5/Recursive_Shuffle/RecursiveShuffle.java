package on2021_08.on2021_08_10_CS_Academy___Beta_Round__5.Recursive_Shuffle;



import template.io.FastInput;
import template.io.FastOutput;

public class RecursiveShuffle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] prev = new int[m];
        int[] size = new int[m];
        int[] pos = new int[m];
        for (int i = 0; i < m; i++) {
            pos[i] = in.ri();
            size[i] = m;
            prev[i] = 0;
        }

    }
}
