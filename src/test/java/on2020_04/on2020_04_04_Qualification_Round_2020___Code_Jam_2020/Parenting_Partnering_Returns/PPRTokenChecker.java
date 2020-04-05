package on2020_04.on2020_04_04_Qualification_Round_2020___Code_Jam_2020.Parenting_Partnering_Returns;



import com.sun.corba.se.spi.orbutil.fsm.FSM;
import net.egork.chelper.tester.StringInputStream;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.datastructure.IntervalBooleanMap;
import template.io.FastInput;

public class PPRTokenChecker implements Checker {
    public PPRTokenChecker(String parameters) {
    }

    public Verdict check(String input, String expectedOutput, String actualOutput) {
        FastInput stdin = new FastInput(new StringInputStream(input));
        FastInput sol = new FastInput(new StringInputStream(actualOutput.substring("Case #1: ".length())));
        stdin.readInt();
        int n = stdin.readInt();
        int[][] times = new int[n][2];
        for (int i = 0; i < n; i++) {
            times[i][0] = stdin.readInt();
            times[i][1] = stdin.readInt() - 1;
        }
        String s = sol.readString();
        if (s.equals("IMPOSSIBLE")) {
            return Verdict.UNDECIDED;
        }

        IntervalBooleanMap m1 = new IntervalBooleanMap();
        IntervalBooleanMap m2 = new IntervalBooleanMap();

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            IntervalBooleanMap map = m1;
            if (c == 'J') {
                map = m2;
            }
            if (map.or(times[i][0], times[i][1])) {
                return Verdict.WA;
            }
            map.setTrue(times[i][0], times[i][1]);
        }
        return Verdict.OK;
    }
}
