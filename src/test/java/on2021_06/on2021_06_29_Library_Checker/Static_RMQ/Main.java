package on2021_06.on2021_06_29_Library_Checker.Static_RMQ;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_06/on2021_06_29_Library_Checker/Static_RMQ/Static RMQ.json"))
			Assert.fail();
	}
}
