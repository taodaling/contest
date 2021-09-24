package on2021_07.on2021_07_19_DMOJ___DMOPC__19_Contest_5.P2___Charlie_s_Crazy_Conquest;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class P2CharliesCrazyConquest {

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int h = in.ri();
        Person[] people = new Person[2];
        for (int i = 0; i < 2; i++) {
            people[i] = new Person(h);
        }
        int[][] cmds = new int[2][2 * n];
        for (int i = 0; i < n; i++) {
            cmds[0][i * 2] = in.rc();
            cmds[1][i * 2] = in.ri();
        }
        for (int i = 0; i < n; i++) {
            cmds[0][i * 2 + 1] = in.rc();
            cmds[1][i * 2 + 1] = in.ri();
        }
        for (int i = 0; i < 2 * n; i++) {
            debug.debug("i", i);
            debug.debug("people", people);
            Person me = people[i & 1];
            Person other = people[(i & 1) ^ 1];
            int c = cmds[0][i];
            int d = cmds[1][i];
            if (c == 'A') {
                if (other.dodge == -1) {
                    other.hp -= d;
                } else {
                    other.dodge = -1;
                }
            }
            if (other.dodge != -1) {
                other.hp -= other.dodge;
                other.dodge = -1;
            }
            if (c == 'D') {
                me.dodge = d;
            }
            if (other.hp <= 0) {
                out.println(other == people[1] ? "VICTORY" : "DEFEAT");
                return;
            }
        }
        debug.debug("i", 2 * n);
        debug.debug("people", people);
        if (people[1].dodge != -1) {
            people[1].hp -= people[1].dodge;
            if (people[1].hp <= 0) {
                out.println("VICTORY");
                return;
            }
        }

        out.println("TIE");
    }
}

class Person {
    int hp;

    public Person(int hp) {
        this.hp = hp;
    }

    int dodge = -1;

    @Override
    public String toString() {
        return "Person{" +
                "hp=" + hp +
                ", dodge=" + dodge +
                '}';
    }
}