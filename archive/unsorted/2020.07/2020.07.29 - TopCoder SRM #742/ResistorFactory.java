package contest;

import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class ResistorFactory {
    //Debug debug = new Debug(true);
    public int[] construct(long nanoOhms) {
        double d = nanoOhms * 1e-9;
        double prec = 1e-12;
        int used = 60;
        double[] power = new double[used + used + 1];
        power[0] = 1;
        for (int i = 1; i <= used; i++) {
            power[i] = 1L << i;
            add(i - 1, i - 1, 0);
        }
        power[1 + used] = 0.5;
        add(0, 0, 1);
        for (int i = 2; i <= used; i++) {
            power[used + i] = power[used + i - 1] / 2;
            add(used + i - 1, used + i - 1, 1);
        }

       // debug.debug("power", power);
        List<Integer> need = new ArrayList<>();
        for (int i = used; i >= 0 && d > 0; i--) {
            if (d >= power[i] || power[i] - d <= prec) {
                need.add(i);
                d -= power[i];
            }
        }
        for (int i = used + 1; i < power.length && d > 0; i++) {
            if (d >= power[i] || power[i] - d <= prec) {
                need.add(i);
                d -= power[i];
            }
        }

        if (Math.abs(d) > 1e-9) {
            throw new RuntimeException();
        }

        while (need.size() < 2) {
            need.add(power.length - 1);
        }
        int cur = need.get(0);
        for (int i = 1; i < need.size(); i++) {
            int x = need.get(i);
            add(cur, x, 0);
            cur = seq.size() / 3;
        }
        return seq.stream().mapToInt(Integer::intValue).toArray();
    }


    List<Integer> seq = new ArrayList<>();

    public void add(int a, int b, int op) {
        seq.add(a);
        seq.add(b);
        seq.add(op);
    }
}
