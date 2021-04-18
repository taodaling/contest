package on2021_03.on2021_03_25_Library_Checker.Cartesian_Tree;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_03/on2021_03_25_Library_Checker/Cartesian_Tree/Cartesian Tree.json"))
			Assert.fail();
	}
}
