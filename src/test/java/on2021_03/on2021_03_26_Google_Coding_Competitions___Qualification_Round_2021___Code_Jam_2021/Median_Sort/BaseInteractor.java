package on2021_03.on2021_03_26_Google_Coding_Competitions___Qualification_Round_2021___Code_Jam_2021.Median_Sort;



import chelper.AbstractInteractor;
import chelper.BindInOutStream;
import chelper.ExternalInterator;
import net.egork.chelper.tester.State;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;
import template.utils.SortUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class BaseInteractor {
    public final Verdict interact(InputStream input, InputStream solutionOutput, OutputStream solutionInput, State<Boolean> state) throws Throwable {
        int type = new FastInput(input).ri();
        ExternalInterator interator = new ExternalInterator("python", "C:\\Users\\dalt\\Desktop\\local_testing_tool.py", "" + 2);
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new BindInOutStream(interator.getIn(), solutionInput));
        service.submit(new BindInOutStream(solutionOutput, interator.getOut()));
        service.shutdown();
        int status = interator.getExitCode();
        return status == 0 ? Verdict.OK : Verdict.WA;
    }

//    @Override
//    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
//        int n = input.ri();
//        int[] seq = input.ri(n);
//        int[] sortedIndices = IntStream.range(0, n).toArray();
//        SortUtils.quickSort(sortedIndices, (i, j) -> Integer.compare(seq[i], seq[j]), 0, n);
//        int cnt = 0;
//        solutionInput.println(1);
//        solutionInput.println(n);
//        solutionInput.println(170);
//        solutionInput.flush();
//        while (true) {
//            solutionOutput.skipBlank();
//            solutionOutput.skipEmptyLine();
//            String line = solutionOutput.readLine();
//            String[] split = line.split("\\s+");
//            int[] x = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
//            if (split.length == 3) {
//                cnt++;
//                if (cnt > 300) {
//                    solutionInput.println(-1).flush();
//                    return Verdict.WA;
//                }
//                SortUtils.quickSort(x, (i, j) -> Integer.compare(seq[i - 1], seq[j - 1]), 0, 3);
//                solutionInput.println(x[1]).flush();
//            } else {
//                int[] inv = x.clone();
//                SequenceUtils.reverse(inv);
//                boolean lr = true;
//                boolean rl = true;
//                for (int i = 0; i < n; i++) {
//                    if (sortedIndices[i] + 1 != x[i]) {
//                        lr = false;
//                    }
//                    if (sortedIndices[i] + 1 != inv[i]) {
//                        rl = false;
//                    }
//                }
//                if (!lr && !rl) {
//                    solutionInput.println(-1).flush();
//                    return Verdict.WA;
//                }
//                solutionInput.println(1).flush();
//                return Verdict.OK;
//            }
//        }
//    }
}
