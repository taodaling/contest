package on2020_02.on2020_02_29_Educational_Codeforces_Round_79__Rated_for_Div__2_.F__New_Year_and_Handle_Change;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.IntervalPickProblem;

public class FNewYearAndHandleChange {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int l = in.readInt();
        IntervalPickProblem.Interval[] intervals = new IntervalPickProblem.Interval[n - l + 1];
        char[] s = new char[n];
        in.readString(s, 0);
        long[] data = new long[n];
        int lowCnt = 0;
        int upperCnt = 0;
        for (int i = 0; i < n; i++) {
            if (Character.isUpperCase(s[i])) {
                data[i] = 1;
                upperCnt++;
            } else {
                data[i] = 0;
                lowCnt++;
            }
        }
        for(int i = 0; i < intervals.length; i++){
            intervals[i] = new IntervalPickProblem.Interval(i, i + l - 1);
        }
        long ans1 = upperCnt - IntervalPickProblem.solveNonNegative(data, intervals, k);
        for(int i = 0; i < n; i++){
            data[i] = 1 - data[i];
        }
        long ans2 = lowCnt - IntervalPickProblem.solveNonNegative(data, intervals, k);
        out.println(Math.min(ans1, ans2));
    }
}
