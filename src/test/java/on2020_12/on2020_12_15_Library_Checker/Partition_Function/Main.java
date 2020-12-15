package on2020_12.on2020_12_15_Library_Checker.Partition_Function;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_12/on2020_12_15_Library_Checker/Partition_Function/Partition Function.json"))
			Assert.fail();
	}
}
