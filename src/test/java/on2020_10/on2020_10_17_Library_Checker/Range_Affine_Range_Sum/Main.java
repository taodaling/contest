package on2020_10.on2020_10_17_Library_Checker.Range_Affine_Range_Sum;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_10/on2020_10_17_Library_Checker/Range_Affine_Range_Sum/Range Affine Range Sum.json"))
			Assert.fail();
	}
}
