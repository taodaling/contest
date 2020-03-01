package on2020_02.on2020_02_27_Kotlin_Heroes__Episode_3.C__Dream_Team;



import template.io.FastInput;
import template.io.FastOutput;

public class CDreamTeam {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int sum = 0;
        int[] data = new int[n];
        int[] status = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = in.readInt();
            if (data[i] > 0) {
                status[i] = 1;
                sum += data[i];
            }
        }

        int minimumPosIndex = -1;
        int maximumNegIndex = -1;
        for (int i = 0; i < n; i++) {
            if (data[i] > 0 && (minimumPosIndex == -1 || data[minimumPosIndex] > data[i])) {
                minimumPosIndex = i;
            }
            if (data[i] < 0 && (maximumNegIndex == -1 || data[maximumNegIndex] < data[i])) {
                maximumNegIndex = i;
            }
        }

        if (maximumNegIndex != -1 && sum + data[maximumNegIndex] >
                sum - data[minimumPosIndex]) {
            sum += data[maximumNegIndex];
            status[maximumNegIndex] = 1;
        }else{
            sum -= data[minimumPosIndex];
            status[minimumPosIndex] = 0;
        }

        out.println(sum);
        for(int i = 0; i < n; i++){
            out.append(status[i]);
        }
        out.println();
    }
}
