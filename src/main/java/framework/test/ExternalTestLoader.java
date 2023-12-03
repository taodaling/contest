package framework.test;

import framework.io.FileUtils;
import net.egork.chelper.task.Test;

import java.io.File;
import java.util.*;

public class ExternalTestLoader {
    static File root = new File("D:\\competitve-programming\\testcases\\");

    static String getFileName(String whole) {
        int index = whole.lastIndexOf(".");
        if (index >= 0) {
            whole = whole.substring(0, index);
        }
        return whole;
    }

    private static void dfs(File root, Map<String, String> input, Map<String, String> output) {
        if (root.isFile()) {
            if (root.getName().endsWith(".in")) {
                input.put(getFileName(root.getName()),
                        FileUtils.readFile(root));
            } else if (root.getName().endsWith(".out")) {
                output.put(getFileName(root.getName()),
                        FileUtils.readFile(root));
            }
            return;
        }
        for (File file : root.listFiles()) {
            dfs(file, input, output);
        }
    }

    private static Comparator<String> sortByLenThenByVal = Comparator.<String>comparingInt(x -> x.length())
            .thenComparing(Comparator.naturalOrder());

    public static List<Test> loadLocalTests() {
        Map<String, String> input = new TreeMap<>(sortByLenThenByVal);
        Map<String, String> output = new TreeMap<>(sortByLenThenByVal);
        dfs(root, input, output);

        List<Test> ans = new ArrayList<>();

        for (String key : input.keySet()) {
            if (!output.containsKey(key)) {
                continue;
            }
            ans.add(new Test(input.get(key), output.get(key)));
        }
        return ans;
    }
}
