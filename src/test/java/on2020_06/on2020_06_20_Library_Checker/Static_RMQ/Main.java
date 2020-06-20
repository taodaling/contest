package on2020_06.on2020_06_20_Library_Checker.Static_RMQ;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_06/on2020_06_20_Library_Checker/Static_RMQ/Static RMQ.json"))
			Assert.fail();
	}
}
