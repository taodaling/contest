package contest;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;

import template.DigitUtils;
import template.FastInput;

public class AntsonaCircle {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int l = in.readInt();
        int t = in.readInt();

        Ant[] ants = new Ant[n];
        for (int i = 0; i < n; i++) {
            ants[i] = new Ant();
            ants[i].x = in.readInt();
            ants[i].w = in.readInt();
            ants[i].id = i;
            ants[i].label = i;
        }

        boolean allSameDirection = true;
        for (int i = 1; i < n; i++) {
            allSameDirection = allSameDirection && ants[i].w == ants[i - 1].w;
        }

        if (allSameDirection) {
            for (int i = 0; i < n; i++) {
                int x = ants[i].x;
                if (ants[i].w == 1) {
                    x += t;
                } else {
                    x -= t;
                }
                out.println(DigitUtils.mod(x, l));
            }
            return;
        }

        int diffIndex = 0;
        for (int i = 1; i < n; i++) {
            if(ants[i].w == 2 && ants[i - 1].w == 1){
                diffIndex = i;
                break;
            }
        }
        if(diffIndex == n){
            diffIndex = 0;
        }

        Deque<Ant> cw = new ArrayDeque<>(n);
        Deque<Ant> ccw = new ArrayDeque<>(n);
        for(int i = 0; i < n; i++){
            Ant ant =
        }
    }

}


class Ant {
    int id;
    int x;
    int w;
    int label;
}
