package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class DShop {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        int n = in.ri();
        int m = in.ri();
        Skill[] skills = new Skill[k];
        for (int i = 0; i < k; i++) {
            skills[i] = new Skill();
            skills[i].init = in.ri();
        }
        for (int i = 0; i < n; i++) {
            Op op = new Op();
            op.id = i;
            op.t = in.ri();
            Skill item = skills[in.ri() - 1];
            op.v = in.ri();
            if (op.t == 1) {
                if (op.v > item.init) {
                    if (item.assign == null || item.assign.v < op.v) {
                        item.assign = op;
                    }
                }
            } else if (op.t == 2) {
                item.plus.add(op);
            } else {
                item.mul.add(op);
            }
        }

        PriorityQueue<Skill> pq = new PriorityQueue<>(k, Comparator.<Skill, State>comparing(x -> x.now, State.comp).reversed());
        for (Skill skill : skills) {
            if (skill.assign != null) {
                skill.assign.v = skill.assign.v - skill.init;
                skill.plus.add(skill.assign);
            }
            skill.plus.sort(Comparator.comparingInt(x -> -x.v));
            skill.mul.sort(Comparator.comparingInt(x -> -x.v));
            skill.lastPick = skill.now = new State(-1, -1, 1, 1, skill.init);
            skill.computeNext();
            if (skill.now != null) {
                pq.add(skill);
            }
        }

        int cnt = 0;
        for (int i = 0; i < m && pq.size() > 0; i++) {
            Skill head = pq.remove();
            head.lastPick = head.now;
            head.computeNext();
            if (head.now != null) {
                pq.add(head);
            }
            cnt++;
        }
        out.println(cnt);
        for (Skill skill : skills) {
            State last = skill.lastPick;
            boolean useAssign = false;
            for (int i = 0; i <= last.plus; i++) {
                Op x = skill.plus.get(i);
                if (x.t == 1) {
                    useAssign = true;
                }
            }
            if (useAssign) {
                out.append(skill.assign.id + 1).append(' ');
            }
            for (int i = 0; i <= last.plus; i++) {
                Op x = skill.plus.get(i);
                if (x.t == 2) {
                    out.append(x.id + 1).append(' ');
                }
            }
            for (int i = 0; i <= last.mul; i++) {
                Op x = skill.mul.get(i);
                out.append(x.id + 1).append(' ');
            }
        }
    }
}

class State {
    int plus;
    int mul;
    long a;
    long b;
    long sum;
//    double approx;

    public State(int plus, int mul, long a, long b, long sum) {
        assert a >= 0 && b >= 0;
        this.plus = plus;
        this.mul = mul;
        this.a = a;
        this.b = b;
        this.sum = sum;
//        approx = (double) a / b;
    }

    static Comparator<State> comp = (a, b) -> {
//        if (DigitUtils.isMultiplicationOverflow(a.a, b.b, (long)2e18) ||
//                DigitUtils.isMultiplicationOverflow(a.b, b.a, (long)2e18)) {
            return BigInteger.valueOf(a.a).multiply(BigInteger.valueOf(b.b))
                    .compareTo(BigInteger.valueOf(b.a).multiply(BigInteger.valueOf(a.b)));
//        }
//        return Long.compare(a.a * b.b, b.a * a.b);
    };
}

class Op {
    int id;
    int t;
    int v;
}

class Skill {
    int init;
    Op assign;
    List<Op> plus = new ArrayList<>();
    List<Op> mul = new ArrayList<>();
    State now;
    State lastPick;

    void computeNext() {
        State best = null;
        if (now.plus + 1 < plus.size()) {
            best = new State(now.plus + 1, now.mul, (plus.get(now.plus + 1).v + now.sum), now.sum, plus.get(now.plus + 1).v + now.sum);
        }
        if (now.mul + 1 < mul.size()) {
            State cand = new State(now.plus, now.mul + 1, mul.get(now.mul + 1).v, 1, now.sum);
            if (best == null || State.comp.compare(best, cand) < 0) {
                best = cand;
            }
        }
        now = best;
    }
}
