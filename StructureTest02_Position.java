package tests.structure;

import static org.junit.jupiter.api.Assertions.assertAll;
import static tests.util.CheckThat.Modifiability.NOT_MODIFIABLE;
import static tests.util.CheckThat.Staticness.NOT_STATIC;
import static tests.util.CheckThat.Visibility.PUBLIC;

import org.junit.jupiter.api.Test;

import tests.util.CheckThat;

public class StructureTest02_Position {
	@Test
	public void structure() {
		assertAll(
			() ->
				CheckThat.theClass("position.Position")
					.hasConstructorWithParams(int.class, int.class)
					.thatIs(PUBLIC)
			,() ->
				CheckThat.theClass("position.Position")
					.hasFieldOfType("h", int.class)
					.thatIs(PUBLIC, NOT_STATIC, NOT_MODIFIABLE)
			,() ->
					CheckThat.theClass("position.Position")
					.hasFieldOfType("v", int.class)
					.thatIs(PUBLIC, NOT_STATIC, NOT_MODIFIABLE)
			,() ->
				CheckThat.theClass("position.Position")
					.hasMethodWithParams("next", "position.Direction")
					.thatIs(PUBLIC, NOT_STATIC)
					.thatReturns("position.Position")
			,() ->
				CheckThat.theClass("position.Position")
					.hasMethodWithParams("distance", "position.Position")
					.thatIs(PUBLIC, NOT_STATIC)
					.thatReturns(int[].class)
			,() ->
				CheckThat.theClass("position.Position")
					.hasMethodWithParams("isDiagonalTo", "position.Position")
					.thatIs(PUBLIC, NOT_STATIC)
					.thatReturns(boolean.class)
			,() ->
				CheckThat.theClass("position.Position")
					.hasMethodWithNoParams("toString")
					.thatIs(PUBLIC, NOT_STATIC)
					.thatReturns(String.class)
			,() ->
				CheckThat.theClass("position.Position")
					.hasMethodWithParams("equals", Object.class)
					.thatIs(PUBLIC, NOT_STATIC)
					.thatReturns(boolean.class)
		);
	}
}
