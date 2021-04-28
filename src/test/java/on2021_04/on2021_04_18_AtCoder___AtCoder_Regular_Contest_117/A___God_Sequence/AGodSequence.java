package on2021_04.on2021_04_18_AtCoder___AtCoder_Regular_Contest_117.A___God_Sequence;



import template.io.FastInput;
import template.io.FastOutput;

public class AGodSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int A = in.ri();
        int B = in.ri();
        int[] ans;
        boolean rev = false;
        if(A <= B){
            ans = solve(A, B);
        }else{
            ans = solve(B, A);
            rev = true;
        }
        if(rev){
            for(int i = 0; i < ans.length; i++){
                ans[i] = -ans[i];
            }
        }
        for(int x : ans){
            out.append(x).append(' ');
        }
    }

    public int[] solve(int A, int B) {
        assert A <= B;
        int[] ans = new int[A + B];
        int sum = 0;
        for (int i = 0; i < B; i++) {
            ans[i] = -(i + 1);
            sum += ans[i];
        }
        for (int i = B; i < A + B; i++) {
            ans[i] = (i - B) + 1;
            sum += ans[i];
        }
        ans[A + B - 1] -= sum;
        return ans;
    }
}
