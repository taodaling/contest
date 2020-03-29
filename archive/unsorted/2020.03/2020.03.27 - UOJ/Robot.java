package contest;

import template.datastructure.LiChaoSegment;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerList;

import java.util.ArrayList;
import java.util.List;

public class Robot {
    public LiChaoSegment.Line inverse(LiChaoSegment.Line line) {
        return new LiChaoSegment.Line(-line.a, -line.b);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        LiChaoSegment.Line[] pos = new LiChaoSegment.Line[n];
        int[] modifyTime = new int[n];
        for (int i = 0; i < n; i++) {
            pos[i] = new LiChaoSegment.Line(0, in.readInt());
        }

        List<Update> updates = new ArrayList<>(m);
        IntegerList qs = new IntegerList(m);
        IntegerList times = new IntegerList(m + 1);
        times.add(0);
        char[] cmd = new char[100];
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            times.add(t);
            in.readString(cmd, 0);
            if (cmd[0] == 'c') {
                Update update = new Update();
                update.time = t;
                update.k = in.readInt() - 1;
                update.x = in.readInt();
                updates.add(update);
            } else {
                qs.add(t);
            }
        }

        IntegerDiscreteMap dm = new IntegerDiscreteMap(times.getData(), 0, times.size());
        IntToLongFunction func = i -> dm.iThElement(i);
        LiChaoSegment top = new LiChaoSegment(dm.minRank(), dm.maxRank());
        LiChaoSegment bot = new LiChaoSegment(dm.minRank(), dm.maxRank());

        for (Update u : updates) {
            int l = dm.rankOf(modifyTime[u.k]);
            int r = dm.rankOf(u.time);
            top.update(l, r, dm.minRank(), dm.maxRank(), pos[u.k], func);
            bot.update(l, r, dm.minRank(), dm.maxRank(), inverse(pos[u.k]), func);
            modifyTime[u.k] = u.time;
            pos[u.k] = new LiChaoSegment.Line(u.x, pos[u.k].apply(modifyTime[u.k]) - u.x * (long) modifyTime[u.k]);
        }

        for (int i = 0; i < n; i++) {
            int l = dm.rankOf(modifyTime[i]);
            int r = dm.maxRank();
            top.update(l, r, dm.minRank(), dm.maxRank(), pos[i], func);
            bot.update(l, r, dm.minRank(), dm.maxRank(), inverse(pos[i]), func);
        }

        for (int i = 0; i < qs.size(); i++) {
            int t = qs.get(i);
            int r = dm.rankOf(t);
            long cand1 = top.query(r, r, dm.minRank(), dm.maxRank(), t);
            long cand2 = bot.query(r, r, dm.minRank(), dm.maxRank(), t);
            out.println(Math.max(cand1, cand2));
        }
    }
}

class Update {
    int time;
    int k;
    int x;
}