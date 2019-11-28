package on2019_11.on2019_11_27_Educational_Codeforces_Round_77__Rated_for_Div__2_.B___Obtain_Two_Zeroes;





import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        if((2 * a - b) % 3 != 0 || (2 * a - b) < 0 ||
                (2 * b - a) % 3 != 0 || (2 * b - a) < 0){
            out.println("NO");
        }else{
            out.println("YES");
        }
    }
}
