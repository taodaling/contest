package on2021_08.on2021_08_06_CS_Academy___Virtual_Beta_Round__4.Anagrams;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_08/on2021_08_06_CS_Academy___Virtual_Beta_Round__4/Anagrams/Anagrams.json"))
			Assert.fail();
	}
}
