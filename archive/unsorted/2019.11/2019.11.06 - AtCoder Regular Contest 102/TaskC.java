package contest;

import java.util.Arrays;

import template.FastInput;
import template.FastOutput;
import template.PreSum;

public class TaskC {
    Exam[] exams;
    long x;
    int n;
    PreSum ps;
    long debt;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        x = in.readInt();
        exams = new Exam[n];
        debt = 0;
        for (int i = 0; i < n; i++) {
            exams[i] = new Exam();
            exams[i].b = in.readInt();
            exams[i].l = in.readInt();
            exams[i].r = in.readInt();
            exams[i].profit = exams[i].r * x - exams[i].b * (exams[i].r - exams[i].l);
            debt += exams[i].l * exams[i].b;
        }

        Arrays.sort(exams, (a, b) -> -Long.compare(a.profit, b.profit));
        ps = new PreSum(Arrays.stream(exams).mapToLong(x -> x.profit).toArray());


        check(2540);
        long l = 0;
        long r = x * n;
        while(l < r){
            long m = (l + r) >> 1;
            if(check(m)){
                r = m;
            }else{
                l = m + 1;
            }
        }

        out.println(l);
    }

    public boolean check(long time) {
        long remain = time % x;
        long block = time / x;
        if (block >= n) {
            return true;
        }

        for (int i = 0; i < n; i++) {
            long debtRemain = debt;
            if (i < block) {
                debtRemain -= (ps.prefix((int) block) - exams[i].profit);
            } else if(block - 1 >= 0){
                debtRemain -= ps.prefix((int) block - 1);
            }
            if (debtRemain <= 0 || debtRemain - (remain - exams[i].b) * exams[i].r - exams[i].b * exams[i].l <= 0 ||
                    debtRemain - remain * exams[i].l <= 0) {
                return true;
            }
        }
        return false;
    }
}


class Exam {
    long b;
    long l;
    long r;

    long profit;
}
