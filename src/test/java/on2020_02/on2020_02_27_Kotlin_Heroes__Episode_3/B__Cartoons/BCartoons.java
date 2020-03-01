package on2020_02.on2020_02_27_Kotlin_Heroes__Episode_3.B__Cartoons;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

public class BCartoons {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] lr = new int[n][2];
        int[] list = new int[n * 6];
        for (int i = 0; i < n; i++) {
            lr[i][0] = in.readInt();
            lr[i][1] = in.readInt();
            list[i * 6 + 0] = (lr[i][0] - 1);
            list[i * 6 + 1] = (lr[i][0]);
            list[i * 6 + 2] = (lr[i][0] + 1);
            list[i * 6 + 3] = (lr[i][1] - 1);
            list[i * 6 + 4] = (lr[i][1]);
            list[i * 6 + 5] = (lr[i][1] + 1);
        }
        for (int i = list.length - 1; i >= 0; i--) {
            int v = list[i];
            int cnt = 0;
            for (int[] interval : lr) {
                if (interval[0] <= v && interval[1] >= v) {
                    cnt++;
                }
            }
            if (cnt == 1) {
                out.println(v);
                return;
            }
        }
        out.println(-1);
    }
}
