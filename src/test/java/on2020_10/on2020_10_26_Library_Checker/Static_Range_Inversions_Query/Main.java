package on2020_10.on2020_10_26_Library_Checker.Static_Range_Inversions_Query;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_10/on2020_10_26_Library_Checker/Static_Range_Inversions_Query/Static Range Inversions Query.json"))
			Assert.fail();
	}
}
