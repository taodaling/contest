package on2019_12.on2019_12_18_Codeforces_Global_Round_6.E__Spaceship_Solitaire;





import template.datastructure.LongHashMap;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class ESpaceshipSolitaire {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] as = new int[n];
        int[] tricks = new int[n];
        long sum = 0;

        for (int i = 0; i < n; i++) {
            as[i] = in.readInt();
            sum += as[i];
        }

        int q = in.readInt();
        LongHashMap map = new LongHashMap(q, true);
        for (int i = 0; i < q; i++) {
            int s = in.readInt() - 1;
            int t = in.readInt();
            int u = in.readInt() - 1;

            long key = DigitUtils.asLong(s, t);
            if (map.containKey(key)) {
                int r = (int) map.get(key);
                map.remove(key);
                tricks[r]--;
                if (tricks[r] < as[r]) {
                    sum++;
                }
            }

            if (u >= 0) {
                map.put(key, u);
                if(tricks[u] < as[u]){
                    sum--;
                }
                tricks[u]++;
            }

            out.println(sum);
        }
    }
}
