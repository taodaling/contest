package on2020_04.on2020_04_05_Kattis_Archive.Superdoku;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_04/on2020_04_05_Kattis_Archive/Superdoku/Superdoku.json"))
			Assert.fail();
	}
}
