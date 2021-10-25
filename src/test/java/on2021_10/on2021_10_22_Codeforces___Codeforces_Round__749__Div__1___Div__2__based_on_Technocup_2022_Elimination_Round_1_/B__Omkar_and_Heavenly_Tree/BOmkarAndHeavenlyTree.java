package on2021_10.on2021_10_22_Codeforces___Codeforces_Round__749__Div__1___Div__2__based_on_Technocup_2022_Elimination_Round_1_.B__Omkar_and_Heavenly_Tree;



import template.io.FastInput;
import template.io.FastOutput;

public class BOmkarAndHeavenlyTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        boolean[] occur = new boolean[n];
        for(int i = 0; i < m; i++){
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int c = in.ri() - 1;
            occur[b] = true;
        }
        int firstNotOccur = -1;
        for(int i = 0; i < n; i++){
            if(!occur[i]){
                firstNotOccur = i;
                break;
            }
        }
        if(firstNotOccur == -1){
            while(true){}
        }
        for(int i = 0; i < n; i++){
            if(i == firstNotOccur){
                continue;
            }
            out.append(firstNotOccur + 1).append(' ').append(i + 1).println();
        }
    }
}
