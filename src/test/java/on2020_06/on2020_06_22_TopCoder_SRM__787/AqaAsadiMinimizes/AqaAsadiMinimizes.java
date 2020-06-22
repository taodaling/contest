package on2020_06.on2020_06_22_TopCoder_SRM__787.AqaAsadiMinimizes;



import java.util.Arrays;

public class AqaAsadiMinimizes {
    public double getMin(int[] P, int B0, int X, int Y, int N) {
        int[] A = new int[N];
        for (int i = 0; i <= N - 1; i++) {
            if (i < P.length) {
                A[i] = P[i];
            }
            if (i == P.length) {
                A[i] = B0;
            }
            if (i > P.length) {
                A[i] = (int) (((long) A[i - 1] * X + Y) % 1000000007);
            }
        }
        int[][] pt = new int[N][2];
        for(int i = 0; i < N; i++){
            pt[i][0] = i;
            pt[i][1] = A[i];
        }
        Arrays.sort(pt, (a, b) -> Integer.compare(a[1], b[1]));
        double ans = 1e50;
        for(int i = 0; i + 1 < N; i++){
            int dx = pt[i][0] - pt[i + 1][0];
            int dy = pt[i][1] - pt[i + 1][1];
            ans = Math.min(ans, (double)Math.abs(dy) / Math.abs(dx));
        }
        return ans;
    }
}
