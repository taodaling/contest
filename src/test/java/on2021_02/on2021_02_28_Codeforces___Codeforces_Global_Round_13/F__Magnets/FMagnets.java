package on2021_02.on2021_02_28_Codeforces___Codeforces_Global_Round_13.F__Magnets;




import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;

public class FMagnets {
    FastInput in;
    FastOutput out;

    private int ask(int l, int r, int L, int R) {
        out.append("? ").append(r - l + 1).append(' ').append(R - L + 1).println();
        for (int i = l; i <= r; i++) {
            out.append(i + 1).append(' ');
        }
        out.println();
        for (int i = L; i <= R; i++) {
            out.append(i + 1).append(' ');
        }
        out.println();
        out.flush();
        return in.ri();
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int n = in.ri();
        int right = 1;
        while (ask(0, right - 1, right, right) == 0) {
            right++;
        }
        boolean[] ans = new boolean[n];
        ans[right] = true;
        for(int i = right + 1; i < n; i++){
            if(ask(right, right, i, i) != 0){
                ans[i] = true;
            }
        }
        int l = 0;
        int r = right - 1;
        while(l < r){
            int mid = (l + r) / 2;
            if(ask(0, mid, right, right) != 0){
                r = mid;
            }else{
                l = mid + 1;
            }
        }
        ans[l] = true;
        int cnt  = 0;
        for(boolean x : ans){
            if(!x){
                cnt++;
            }
        }
        out.append("! ").append(cnt).append(' ');
        for (int i = 0; i < n; i++) {
            if(!ans[i]) {
                out.append(i + 1).append(' ');
            }
        }
        out.println().flush();
    }
}
