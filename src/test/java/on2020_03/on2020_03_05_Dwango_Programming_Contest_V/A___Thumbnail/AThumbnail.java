package on2020_03.on2020_03_05_Dwango_Programming_Contest_V.A___Thumbnail;



import template.io.FastInput;
import template.io.FastOutput;

public class AThumbnail {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            sum += a[i];
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (Math.abs(sum - n * a[ans]) > Math.abs(sum - n * a[i])) {
                ans = i;
            }
        }
        out.println(ans);
    }
}