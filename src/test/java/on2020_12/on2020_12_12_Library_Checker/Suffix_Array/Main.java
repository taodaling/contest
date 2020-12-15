package on2020_12.on2020_12_12_Library_Checker.Suffix_Array;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_12/on2020_12_12_Library_Checker/Suffix_Array/Suffix Array.json"))
			Assert.fail();
	}
}
