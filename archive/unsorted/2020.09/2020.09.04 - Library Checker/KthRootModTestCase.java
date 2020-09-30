package contest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import framework.io.FileUtils;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.Modular;
import template.math.Power;
import template.rand.RandomWrapper;

public class KthRootModTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        File probDir = new File("/media/share/sourcecode/library-checker-problems/math/kth_root_mod/");
        File inDir = new File(probDir, "in");
        File outDir = new File(probDir, "out");
        for (File in : inDir.listFiles()) {
            if(!in.getName().equals("max_random_00.in")){
                continue;
            }
            System.out.println("input [" + in.getName() + "](" + in.length() + ")");
            String fileName = in.getName().substring(0, in.getName().lastIndexOf('.'));
            File out = new File(outDir, fileName + ".out");
            tests.add(new Test(FileUtils.readFile(in), FileUtils.readFile(out)));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

}
