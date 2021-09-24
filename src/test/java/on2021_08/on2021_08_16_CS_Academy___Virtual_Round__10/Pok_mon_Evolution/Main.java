package on2021_08.on2021_08_16_CS_Academy___Virtual_Round__10.Pok_mon_Evolution;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_08/on2021_08_16_CS_Academy___Virtual_Round__10/Pok_mon_Evolution/Pokémon Evolution.json"))
			Assert.fail();
	}
}
