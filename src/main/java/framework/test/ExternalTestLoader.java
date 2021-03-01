package framework.test;

import framework.io.FileUtils;
import net.egork.chelper.task.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTestLoader {
    static File root = new File("F:\\sourcecode\\contest\\testcase");

    static String getFileName(String whole) {
        int index = whole.lastIndexOf(".");
        if (index >= 0) {
            whole = whole.substring(0, index);
        }
        return whole;
    }

    public static List<Test> load() {
        Map<String, String> input = new HashMap<>();
        Map<String, String> output = new HashMap<>();
        for (File file : root.listFiles()) {
            if (file.getName().endsWith(".in")) {
                input.put(getFileName(file.getName()),
                        FileUtils.readFile(file));
            } else {
                output.put(getFileName(file.getName()),
                        FileUtils.readFile(file));
            }
        }
        List<Test> ans = new ArrayList<>();
        for (String key : input.keySet()) {
            ans.add(new Test(input.get(key), output.get(key)));
        }
        return ans;
    }
}
