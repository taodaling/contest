package on2021_07.on2021_07_15_DMOJ___DMOPC__20_Contest_1.P1___Victor_Gets_Quizzed;



import template.io.FastInput;
import template.io.FastOutput;

public class P1VictorGetsQuizzed {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.rs();
        int contain = 0;
        if (s.indexOf('M') >= 0) {
            contain++;
        }
        if (s.indexOf('C') >= 0) {
            contain++;
        }
        if (contain == 0) {
            out.println("PASS");
        } else if (contain == 1) {
            out.println("FAIL");
        } else {
            out.println("NEGATIVE MARKS");
        }
    }
}
