package contest;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IncreasingJumpsDiv1 {
    int center = 1250;

    public int[] jump(int[] frogs) {
        List<Frog> frogList = new ArrayList<>();
        for (int i = 0; i < frogs.length; i++) {
            frogList.add(new Frog(frogs[i], i));
        }
        while (true) {
            int step = ans.size() + 1;
            Frog best = null;
            int approach = 0;
            for (Frog f : frogList) {
                int a = approach(f.x, step);
                if (a > approach) {
                    best = f;
                    approach = a;
                }
            }
            if (best == null) {
                break;
            }
            if (best.x > center) {
                move(best, -1);
            } else {
                move(best, 1);
            }
        }
        List<Frog> active = frogList.stream().filter(x -> x.x != center).collect(Collectors.toList());
        while (!active.isEmpty()) {
            Frog exit = null;
            for (Frog f : active) {
                if (approach(f.x, active.size()) <= 0) {
                    exit = f;
                    break;
                }
            }
            if (exit != null) {
                active.remove(exit);
                while (exit.x != center) {
                    int sign = exit.x < center ? 1 : -1;
                    move(exit, -sign);
                    move(exit, sign);
                }
                continue;
            }

            for (Frog f : active) {
                f.sign = f.x < center ? 1 : -1;
                move(f, -f.sign);
            }
            for (Frog f : active) {
                move(f, f.sign);
            }
        }

        for (Frog frog : frogList) {
            if (frog.x != center) {
                throw new RuntimeException();
            }
            move(frog, 1);
        }
        if(ans.size() > 2500){
            throw new RuntimeException();
        }
        System.err.println(ans.size());
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }

    public int approach(int x, int step) {
        int eld = Math.abs(x - center);
        if (x > center) {
            x -= step;
        } else {
            x += step;
        }
        int newDist = Math.abs(x - center);
        return eld - newDist;
    }


    List<Integer> ans = new ArrayList<>();

    public void move(Frog f, int sign) {
        int step = ans.size() + 1;
        f.x += sign * step;
        ans.add(sign * (f.id + 1));
    }
}

class Frog {
    int x;
    int id;
    int sign;

    public Frog(int x, int id) {
        this.x = x;
        this.id = id;
    }
}