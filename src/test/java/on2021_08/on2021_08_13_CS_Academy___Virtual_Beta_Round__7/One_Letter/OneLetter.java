package on2021_08.on2021_08_13_CS_Academy___Virtual_Beta_Round__7.One_Letter;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class OneLetter {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[(int)1e5];
        IntegerArrayList list = new IntegerArrayList(n);
        for(int i = 0; i < n; i++){
            int m = in.rs(s);
            int minChar = 128;
            for(int j = 0; j < m; j++){
                minChar = Math.min(minChar, s[j]);
            }
            list.add(minChar);
        }
        list.sort();
        for(int x : list.toArray()){
            out.append((char)x);
        }
    }
}
