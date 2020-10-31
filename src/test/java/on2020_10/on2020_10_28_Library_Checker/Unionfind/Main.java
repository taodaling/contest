package on2020_10.on2020_10_28_Library_Checker.Unionfind;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_10/on2020_10_28_Library_Checker/Unionfind/Unionfind.json"))
			Assert.fail();
	}
}
