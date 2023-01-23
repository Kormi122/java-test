package tests.structure;

import org.junit.jupiter.api.Test;

import tests.util.CheckThat;

public class StructureTest01_Direction {
	@Test
	public void structure() throws Exception {
		CheckThat.theEnum("position.Direction")
			.hasEnumElements("UP_LEFT", "UP_RIGHT", "DOWN_LEFT", "DOWN_RIGHT");
	}
}
