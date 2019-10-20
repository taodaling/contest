package on2019_10.on2019_10_20_AGC006.TaskD;



import template.FastInput;
import template.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] data = new int[n * 2];
        int[] buf = data.clone();
        for (int i = 1; i < n * 2; i++) {
            data[i] = in.readInt();
        }

        int l = 1;
        int r = data.length - 1;
        while(l < r){
            int m = (l + r + 1) / 2;
            if(check(data, m, buf)){
                l = m;
            }else{
                r = m - 1;
            }
        }
        out.println(l);
    }

    public boolean check(int[] data, int m, int[] buf) {
        for (int i = 1; i < data.length; i++) {
            buf[i] = data[i] >= m ? 1 : 0;
        }
        int center = data.length / 2;
        int l = center;
        int r = center;
        while(l > 1 && buf[l] != buf[l - 1]){
            l--;
        }
        while(r < data.length - 1 && buf[r] != buf[r + 1]){
            r++;
        }
        if(buf[l] == buf[r]){
            return buf[l] == 1;
        }
        int distL = center - l;
        int distR = r - center;
        if(distL < distR){
            return buf[l] == 1;
        }
        return buf[r] == 1;
    }
}
