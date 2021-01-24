package on2021_01.on2021_01_20_Library_Checker.K_Shortest_Walk;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_01/on2021_01_20_Library_Checker/K_Shortest_Walk/K-Shortest Walk.json"))
			Assert.fail();
	}
}
