package on2021_07.on2021_07_24_DMOJ___CPC__21_Contest_1.P0___AQT_and_Alphabet;



import template.io.FastInput;
import template.io.FastOutput;

public class P0AQTAndAlphabet {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        int[] cnts = new int[128];
        for (char c : s) {
            cnts[c]++;
        }
        for (int i = 'a'; ; i++) {
            if (cnts[i] == 0) {
                out.append((char) i);
                return;
            }
        }
    }
}
