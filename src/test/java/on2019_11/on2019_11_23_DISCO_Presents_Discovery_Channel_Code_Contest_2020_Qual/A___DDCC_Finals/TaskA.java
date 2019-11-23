package on2019_11.on2019_11_23_DISCO_Presents_Discovery_Channel_Code_Contest_2020_Qual.A___DDCC_Finals;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] profits = new int[]{0, 300000, 200000, 100000};
        int a = in.readInt();
        int b = in.readInt();
        int earn = 0;
        if(a <= 3){
            earn += profits[a];
        }
        if(b <= 3){
            earn += profits[b];
        }
        if(a == 1 && b == 1){
            earn += 400000;
        }

        out.println(earn);
    }
}
