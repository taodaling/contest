package on2019_10.on2019_10_23_codefestival_2016_final.TaskF1;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2019_10/on2019_10_23_codefestival_2016_final/TaskF1/TaskF.json"))
			Assert.fail();
	}
}
