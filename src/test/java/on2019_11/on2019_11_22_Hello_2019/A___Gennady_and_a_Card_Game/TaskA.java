package on2019_11.on2019_11_22_Hello_2019.A___Gennady_and_a_Card_Game;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char a = in.readChar();
        char b = in.readChar();
        for(int i = 0; i < 5; i++){
            if(a == in.readChar() || b == in.readChar()){
                out.println("YES");
                return;
            }
        }
        out.println("NO");
    }
}
