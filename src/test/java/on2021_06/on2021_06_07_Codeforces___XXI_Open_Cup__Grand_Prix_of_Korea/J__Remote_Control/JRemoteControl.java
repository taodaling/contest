package on2021_06.on2021_06_07_Codeforces___XXI_Open_Cup__Grand_Prix_of_Korea.J__Remote_Control;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongHashSet;

public class JRemoteControl {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        LongHashMap map = new LongHashMap(n, false);
        int[] dx = new int[n];
        int[] dy = new int[n];
        for (int i = 0; i < n; i++) {
            char c = in.rc();
            if (c == 'L') {
                dx[i]--;
            } else if (c == 'R') {
                dx[i]++;
            } else if (c == 'U') {
                dy[i]++;
            } else {
                dy[i]--;
            }
        }
        IntegerPreSum psDX = new IntegerPreSum(i -> dx[i], n);
        IntegerPreSum psDY = new IntegerPreSum(i -> dy[i], n);
        int[][] ends = new int[n][2];
        for (int i = n - 1; i >= 0; i--) {
            int targetX = psDX.prefix(i) + dx[i];
            int targetY = psDY.prefix(i) + dy[i];
            int nextEntry = (int) map.getOrDefault(DigitUtils.asLong(targetX, targetY), -1);
            if (nextEntry == -1) {
                ends[i][0] = psDX.post(i + 1) - dx[i];
                ends[i][1] = psDY.post(i + 1) - dy[i];
            } else {
                ends[i] = ends[nextEntry].clone();
            }
            long key = DigitUtils.asLong(psDX.prefix(i), psDY.prefix(i));
            map.put(key, i);
        }
        int q = in.ri();
        for (int i = 0; i < q; i++) {
            int x = in.ri();
            int y = in.ri();
            int ex;
            int ey;
            int targetX = -x;
            int targetY = -y;
            int nextEntry = (int) map.getOrDefault(DigitUtils.asLong(targetX, targetY), -1);
            if(nextEntry == -1){
                ex = x + psDX.post(0);
                ey = y + psDY.post(0);
            }else{
                ex = ends[nextEntry][0];
                ey = ends[nextEntry][1];
            }
            out.append(ex).append(' ').append(ey).println();
        }
    }
}
