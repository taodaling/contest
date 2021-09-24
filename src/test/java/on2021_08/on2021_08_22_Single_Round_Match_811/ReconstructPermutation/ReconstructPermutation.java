package on2021_08.on2021_08_22_Single_Round_Match_811.ReconstructPermutation;



import java.util.Arrays;

public class ReconstructPermutation {
    public int[] reconstruct(int N, int[] partial) {
        boolean[] del = new boolean[N];
        Arrays.fill(del, true);
        for (int x : partial) {
            del[x] = false;
        }
        int[] ans = new int[N];
        int wpos = 0;
        int iter = 0;
        for (int x : partial) {
            while (iter < x) {
                if(del[iter]){
                    ans[wpos++] = iter;
                }
                iter++;
            }
            ans[wpos++] = x;
        }
        while(iter < N){
            if(del[iter]){
                ans[wpos++] = iter;
            }
            iter++;
        }
        return ans;
    }
}
