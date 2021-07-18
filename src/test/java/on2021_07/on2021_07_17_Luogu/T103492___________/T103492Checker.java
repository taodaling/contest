package on2021_07.on2021_07_17_Luogu.T103492___________;



import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class T103492Checker extends AbstractChecker {
    public T103492Checker(String parameters) {
        super(parameters);
    }

    public List<String> read(FastInput in) {
        List<String> ans = new ArrayList<>();
        while (in.hasMore()) {
            ans.add(in.readLine().trim());
        }
        ans = ans.stream().map(x -> {
            String[] segments = x.split("\\s+");
            Arrays.sort(segments);
            return String.join(" ", segments);
        }).sorted().collect(Collectors.toList());
        return ans;
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        List<String> a = read(expect);
        List<String> b = read(actual);
        return a.equals(b) ? Verdict.OK : Verdict.WA;
    }
}