package on2019_12.on2019_12_05_Codeforces_Round__545__Div__1_.D___Cooperative_Game;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskD {
    FastInput in;
    FastOutput out;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;

        while(true){
            move(2);
            if(move(1) == 2){
                break;
            }
        }

        int c = 0;
        while(true){
            c++;
            if(move(1) == 2){
                break;
            }
        }

        int t = 0;
        while(true){
            t++;
            if(move(10) == 1){
                break;
            }
        }

        out.println("done");
        out.flush();
    }

    private int readGroup() {
        int ans = in.readInt();
        for (int i = 0; i < ans; i++) {
            in.readString();
        }
        return ans;
    }

    private int move(int n) {
        out.append("next ");
        for(int i = 0; i < n; i++){
            out.append(i).append(' ');
        }
        out.println();
        out.flush();
        return readGroup();
    }
}
