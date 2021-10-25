package on2021_10.on2021_10_17_Library_Checker.Static_Range_Frequency;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_10/on2021_10_17_Library_Checker/Static_Range_Frequency/Static Range Frequency.json"))
			Assert.fail();
	}
}
