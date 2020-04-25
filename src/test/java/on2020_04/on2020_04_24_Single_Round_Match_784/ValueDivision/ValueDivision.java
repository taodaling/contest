package on2020_04.on2020_04_24_Single_Round_Match_784.ValueDivision;



import java.util.ArrayList;
import java.util.List;

public class ValueDivision {
    public int[] getArray(int[] A) {
        int lastMax = Integer.MAX_VALUE;
        int n = A.length;
        while (true) {
            int localMax = -1;
            int index = -1;
            for (int i = 0; i < n; i++) {
                if (A[i] < lastMax) {
                    localMax = Math.max(localMax, A[i]);

                    if (A[i] == localMax) {
                        index = i;
                    }
                }
            }

            if(localMax <= 1){
                break;
            }

            for (int i = index - 1; i >= 0; i--) {
                if (A[i] == localMax) {
                    A[i]--;
                }
            }

            lastMax = localMax;

            if (index == -1) {
                break;
            }
        }

        return A;
    }
}
