package on2020_10.on2020_10_25_Library_Checker.Queue_Operate_All_Composite;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_10/on2020_10_25_Library_Checker/Queue_Operate_All_Composite/Queue Operate All Composite.json"))
			Assert.fail();
	}
}
