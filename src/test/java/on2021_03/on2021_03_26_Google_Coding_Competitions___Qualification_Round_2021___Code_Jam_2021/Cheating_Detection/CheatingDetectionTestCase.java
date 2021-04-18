package on2021_03.on2021_03_26_Google_Coding_Competitions___Qualification_Round_2021___Code_Jam_2021.Cheating_Detection;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import framework.test.ExternalTestLoader;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class CheatingDetectionTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = ExternalTestLoader.load();
        return tests;
    }

}
