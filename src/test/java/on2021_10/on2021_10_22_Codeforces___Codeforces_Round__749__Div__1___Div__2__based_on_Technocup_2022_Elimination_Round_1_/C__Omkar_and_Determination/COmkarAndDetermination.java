package on2021_10.on2021_10_22_Codeforces___Codeforces_Round__749__Div__1___Div__2__based_on_Technocup_2022_Elimination_Round_1_.C__Omkar_and_Determination;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;

public class COmkarAndDetermination {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] a = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                a[i][j] = in.rc() == 'X' ? 1 : 0;
            }
        }
        int[] events = new int[m - 1];
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < m - 1; j++) {
                if (a[i][j] == 1 && a[i - 1][j + 1] == 1) {
                    events[j]++;
                }
            }
        }
        LongPreSum ps = new LongPreSum(i -> events[i], m - 1);
        int q = in.ri();
        for(int i = 0; i < q; i++){
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            long sum = ps.intervalSum(l, r - 1);
            if(sum > 0){
                out.println("NO");
            }else{
                out.println("YES");
            }
        }
    }
}
