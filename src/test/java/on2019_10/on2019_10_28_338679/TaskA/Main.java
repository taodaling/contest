package on2019_10.on2019_10_28_338679.TaskA;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2019_10/on2019_10_28_338679/TaskA/TaskA.json"))
			Assert.fail();
	}
}
