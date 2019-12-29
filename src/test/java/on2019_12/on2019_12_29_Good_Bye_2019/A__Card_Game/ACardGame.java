package on2019_12.on2019_12_29_Good_Bye_2019.A__Card_Game;



import template.io.FastInput;
import template.io.FastOutput;

public class ACardGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        int maxA = 0;
        int maxB = 0;
        for(int i = 0; i < a; i++){
            maxA = Math.max(maxA, in.readInt());
        }
        for(int i = 0; i < b; i++){
            maxB = Math.max(maxB, in.readInt());
        }
        out.println(maxA > maxB ? "YES" : "NO");
    }
}
