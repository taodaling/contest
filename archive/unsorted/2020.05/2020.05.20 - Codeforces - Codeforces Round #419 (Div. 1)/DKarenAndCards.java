package contest;

import template.geometry.geo3.Line3;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class DKarenAndCards {

    int[][] satisfy = new int[3][2];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] limits = new int[3];
        for (int i = 0; i < 3; i++) {
            limits[i] = in.readInt();
        }

        int[][] cards = new int[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                cards[i][j] = in.readInt();
            }
        }

        List<Rect> cur = new ArrayList<>(10);
        cur.add(new Rect(0, 0, 0, limits[0], limits[1], limits[2]));
        List<Rect> tmp = new ArrayList<>(10);
        List<Rect> mid = new ArrayList<>(10);
        List<Rect> scan = new ArrayList<>(10);
        for (int[] card : cards) {
            scan.clear();
            scan.add(new Rect(card[0], 0, card[2], limits[0], card[1], limits[2]));
            scan.add(new Rect(0, card[1], card[2], card[0], limits[1], limits[2]));
            scan.add(new Rect(card[0], card[1], 0, limits[0], limits[1], limits[2]));

            tmp.clear();
            for (Rect a : cur) {
                mid.clear();
                for (Rect b : scan) {
                    Rect rt = Rect.merge(a, b);
                    if(rt.isEmpty()){
                        continue;
                    }
                    boolean find = false;
                    do {
                        find = false;
                        for (int i = 0; i < mid.size(); i++) {
                            if (Rect.canMerge(mid.get(i), rt)) {
                                find = true;
                                rt = Rect.merge(rt, mid.get(i));
                                mid.remove(i);
                                break;
                            }
                        }
                    } while (find);
                    mid.add(rt);
                }
                tmp.addAll(mid);
            }

            List<Rect> x = tmp;
            tmp = cur;
            cur = x;
        }

        long ans = 0;
        for(Rect r : cur){
            ans += r.volume();
        }

        out.println(ans);
    }
}

class Rect {
    int[] lb = new int[3];
    int[] rt = new int[3];

    public Rect(int x0, int y0, int z0, int x1, int y1, int z1) {
        lb[0] = x0;
        lb[1] = y0;
        lb[2] = z0;
        rt[0] = x1;
        rt[1] = y1;
        rt[2] = z1;
    }

    public Rect() {
    }

    public static boolean canMerge(Rect a, Rect b) {
        for (int i = 0; i < 3; i++) {
            boolean valid = a.lb[i] == b.rt[i] || a.rt[i] == b.lb[i];
            for (int j = 0; j < 3 && valid; j++) {
                if (i == j) {
                    continue;
                }
                if (a.lb[j] != b.lb[j] || a.rt[j] != b.rt[j]) {
                    valid = false;
                }
            }
            if (valid) {
                return true;
            }
        }
        return false;
    }

    public static Rect merge(Rect a, Rect b) {
        Rect ans = new Rect();
        for (int i = 0; i < 3; i++) {
            ans.lb[i] = Math.max(a.lb[i], b.lb[i]);
            ans.rt[i] = Math.min(a.rt[i], b.rt[i]);
        }
        return ans;
    }

    long volume() {
        long ans = 1;
        for (int i = 0; i < 3; i++) {
            ans *= rt[i] - lb[i];
        }
        return ans;
    }

    boolean contain(Rect rect) {
        for (int i = 0; i < 3; i++) {
            if (rect.lb[i] < lb[i] ||
                    rect.rt[i] > rt[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        for (int i = 0; i < 3; i++) {
            if (lb[i] >= rt[i]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(lb) + ":" + Arrays.toString(rt);
    }
}