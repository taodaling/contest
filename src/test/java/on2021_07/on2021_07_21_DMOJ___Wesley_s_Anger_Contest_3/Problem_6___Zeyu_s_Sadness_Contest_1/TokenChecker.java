package on2021_07.on2021_07_21_DMOJ___Wesley_s_Anger_Contest_3.Problem_6___Zeyu_s_Sadness_Contest_1;



import chelper.AbstractChecker;
import net.egork.chelper.tester.StringInputStream;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;

import java.util.*;
import java.util.stream.Collectors;

public class TokenChecker extends AbstractChecker {
    public TokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        String[] all = stdin.readAll();
        String whole = Arrays.stream(all).collect(Collectors.joining(" "));
        stdin = new FastInput(new StringInputStream(whole));
        int n = stdin.ri();
        int m = stdin.ri();
        int k = stdin.ri();
        int[] finalOrder = new int[n];
        List<Problem6ZeyusSadnessContest1TestCase.Event> events = new ArrayList<>();
        Set<String> time = new HashSet<>();
        for (int i = 0; i < n; i++) {
            finalOrder[i] = actual.ri() - 1;
            for (int j = 0; j < m; j++) {
                String s = actual.rs();
                if (s.equals("-/-")) {
                    continue;
                }
                String[] splitted = s.split("/");
                time.add(splitted[0]);
                events.add(new Problem6ZeyusSadnessContest1TestCase.Event(finalOrder[i], j, Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1])));
            }
        }

        String corre = Problem6ZeyusSadnessContest1TestCase.handleEvent(n, m, events);
        String[] reformated = new FastInput(new StringInputStream(corre)).readAll();
        String joined = Arrays.stream(reformated).collect(Collectors.joining(" "));
        return joined.equals(whole) && time.size() == k ? Verdict.OK : Verdict.WA;
    }
}