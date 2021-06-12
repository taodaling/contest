package on2021_05.on2021_05_30_AtCoder___Caddi_Programming_Contest_2021_AtCoder_Beginner_Contest_193_.E___Oversleeping;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ExtCRT;

public class EOversleeping {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long x = in.ri();
        long y = in.ri();
        long p = in.ri();
        long q = in.ri();
        ExtCRT crt = new ExtCRT();
        long best = Long.MAX_VALUE;
        for(long i = x; i < x + y; i++){
            for(long j = p; j < p + q; j++){
                crt.clear();
                crt.add(i, 2 * (x + y));
                crt.add(j, p + q);
                if(crt.valid()){
                    best = Math.min(crt.getValue(), best);
                }
            }
        }

        if(best == Long.MAX_VALUE){
            out.println("infinity");
        }else{
            out.println(best);
        }
    }
}
