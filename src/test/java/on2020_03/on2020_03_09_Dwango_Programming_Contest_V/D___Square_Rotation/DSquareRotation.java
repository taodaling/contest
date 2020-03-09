package on2020_03.on2020_03_09_Dwango_Programming_Contest_V.D___Square_Rotation;



import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerBinaryFunction;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class DSquareRotation {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int d = in.readInt();
        int d2 = d * 2;
        int[][] mat = new int[d2][d2];
        int[][][] sum = new int[3][d2][d2];
        for (int i = 0; i < n; i++) {
            int x = DigitUtils.mod(in.readInt(), d);
            int y = DigitUtils.mod(in.readInt(), d);
            mat[x][y]++;
        }
        for (int i = 0; i < d2; i++) {
            for (int j = 0; j < d2; j++) {
                mat[i][j] = mat[i % d][j % d];
            }
        }

        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                //0
                int a = mid / d;
                int b = mid % d;
                long[] limits = new long[]{(long) a * a, (long) a * (a + 1), (long) (a + 1) * (a + 1)};
                for (int t = 0; t < 3; t++) {
                    for (int i = 0; i < d2; i++) {
                        for (int j = 0; j < d2; j++) {
                            sum[t][i][j] = mat[i][j] > limits[t] ? 1 : 0;
                        }
                    }
                }
                for (int t = 0; t < 3; t++) {
                    for (int i = 0; i < d2; i++) {
                        for (int j = 0; j < d2; j++) {
                            if(j > 0){
                                sum[t][i][j] += sum[t][i][j - 1];
                            }
                        }
                    }
                }
                for (int t = 0; t < 3; t++) {
                    for (int i = 0; i < d2; i++) {
                        for (int j = 0; j < d2; j++) {
                            if(i > 0){
                                sum[t][i][j] += sum[t][i - 1][j];
                            }
                        }
                    }
                }

                for(int i = 0; i < d; i++){
                    for(int j = 0; j < d; j++){
                        int total = interval(sum[2], i, j, i + b - 1, j + b - 1)
                                + interval(sum[1], i + b, j, i + d - 1, j + b - 1)
                                + interval(sum[1], i, j + b, i + b - 1, j + d - 1)
                                + interval(sum[0], i + b, j + b, i + d - 1, j + d - 1);
                        if(total == 0){
                            return true;
                        }
                    }
                }
                return false;
            }
        };

        int ans = ibs.binarySearch(0, (int)1e9) - 1;
        out.println(ans);
    }

    public int interval(int[][] sum, int x1, int y1, int x2, int y2){
        if(x1 > x2 || y1 > y2){
            return 0;
        }
        int ans = sum[x2][y2];
        if(x1 > 0){
            ans -= sum[x1 - 1][y2];
        }
        if(y1 > 0){
            ans -= sum[x2][y1 - 1];
        }
        if(x1 > 0 && y1 > 0){
            ans += sum[x1 - 1][y1 - 1];
        }
        return ans;
    }
}
