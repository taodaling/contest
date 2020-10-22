package on2020_10.on2020_10_22_2019_Petrozavodsk_Winter_Camp__Yandex_Cup.D___Pick_Your_Own_Nim;



import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.datastructure.LinearBasis;
import template.io.FastInput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TokenChecker extends AbstractChecker {
    public TokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int n = stdin.readInt();
        int[] a = new int[n];
        stdin.populate(a);
        int m = stdin.readInt();
        List<Set<Integer>> list = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int k = stdin.readInt();
            Set<Integer> set = new HashSet<>();
            for (int j = 0; j < k; j++) {
                set.add(stdin.readInt());
            }
            list.add(set);
        }
        int ans = expect.readInt();
        int[] nums = new int[m];
        nums[0] = actual.readInt();
        if (nums[0] == -1 && ans == 1 ||
                nums[0] != -1 && ans == -1) {
            return Verdict.WA;
        }
        if(nums[0] == -1 && ans == -1){
            return Verdict.OK;
        }
        for (int i = 1; i < m; i++) {
            nums[i] = actual.readInt();
        }
        for (int i = 0; i < m; i++) {
            if (!list.get(i).contains(nums[i])) {
                return Verdict.WA;
            }
        }


        LinearBasis basis = new LinearBasis();
        for (int x : a) {
            basis.add(x);
        }
        for (int x : nums) {
            if (basis.add(x) == -1) {
                return Verdict.WA;
            }
        }
        return Verdict.OK;
    }
}
