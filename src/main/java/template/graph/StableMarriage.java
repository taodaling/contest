package template.graph;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class StableMarriage {
    private Girl[] girls;
    private Boy[] boys;

    private static class Girl {
        Boy fere;
        int id;
    }

    private static class Boy {
        Girl fere;
        Deque<Girl> remainChoices = new ArrayDeque<>();
        int id;
    }

    /**
     * O(n^2\log_2n)
     * <br>
     * boyFavors[i][j] means how much the boy i like the girl j. The same for girlFavors.
     * @param boyFavors every boy has n girls in preference list
     * @param girlFavors every girl has n boys in preference list
     */
    public StableMarriage(final int[][] boyFavors, final int[][] girlFavors) {
        int n = boyFavors.length;
        boys = new Boy[n];
        girls = new Girl[n];
        for (int i = 0; i < n; i++) {
            girls[i] = new Girl();
            girls[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            final Boy boy = new Boy();
            boy.id = i;
            Arrays.sort(girls, (a, b) -> -Integer.compare(boyFavors[boy.id][a.id], boyFavors[boy.id][b.id]));
            boy.remainChoices.addAll(Arrays.asList(girls));
            boys[i] = boy;
        }
        Arrays.sort(girls, (a, b) -> Integer.compare(a.id, b.id));

        Deque<Boy> unmarried = new ArrayDeque<>(Arrays.asList(boys));
        while (!unmarried.isEmpty()) {
            Boy head = unmarried.removeFirst();
            Girl girl = head.remainChoices.removeFirst();
            if (girl.fere == null) {
                combine(head, girl);
            } else if (girlFavors[girl.id][girl.fere.id] < girlFavors[girl.id][head.id]) {
                girl.fere.fere = null;
                unmarried.addLast(girl.fere);
                combine(head, girl);
            } else {
                unmarried.addFirst(head);
            }
        }
    }

    public int husbandOf(int id) {
        return girls[id].fere.id;
    }

    public int wifeOf(int id) {
        return boys[id].fere.id;
    }

    private void combine(Boy boy, Girl girl) {
        boy.fere = girl;
        girl.fere = boy;
    }

}