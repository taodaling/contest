package on2020_12.on2020_12_13_Library_Checker.Strongly_Connected_Components;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_12/on2020_12_13_Library_Checker/Strongly_Connected_Components/Strongly Connected Components.json"))
			Assert.fail();
	}
}
