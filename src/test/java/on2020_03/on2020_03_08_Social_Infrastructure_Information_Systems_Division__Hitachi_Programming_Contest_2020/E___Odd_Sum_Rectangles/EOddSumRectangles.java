package on2020_03.on2020_03_08_Social_Infrastructure_Information_Systems_Division__Hitachi_Programming_Contest_2020.E___Odd_Sum_Rectangles;



import template.io.FastInput;
import template.io.FastOutput;

public class EOddSumRectangles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        for(int i = 1; i < (1 << n); i++){
            for(int j = 1; j < (1 << m); j++){
                out.append(1);
            }
            out.println();
        }
    }
}
