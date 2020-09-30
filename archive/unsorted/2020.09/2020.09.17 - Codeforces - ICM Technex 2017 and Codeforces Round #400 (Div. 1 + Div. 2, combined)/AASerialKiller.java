package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AASerialKiller {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String[] names = new String[2];
        names[0] = in.readString();
        names[1] = in.readString();
        int n = in.readInt();
        for(int i = 0; i <= n; i++){
            out.append(names[0]).append(' ').append(names[1]).println();
            if(i == n){
                break;
            }
            String killed = in.readString();
            String rep = in.readString();
            replace(names, killed, rep);
        }
    }

    public void replace(String[] names, String a, String b) {
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(a)) {
                names[i] = b;
            }
        }
    }
}
