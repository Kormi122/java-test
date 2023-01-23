package tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import tests.structure.StructureTest01_Direction;
import tests.structure.StructureTest02_Position;
import tests.structure.StructureTest03_AbstractBoard;
import tests.structure.StructureTest04_SparseBoard;
import tests.structure.StructureTest05_Piece;
import tests.structure.StructureTest06_King;

@Suite
@SelectClasses({
	StructureTest01_Direction.class,
	DraughtsTest.class,

	StructureTest02_Position.class,
	StructureTest03_AbstractBoard.class,

	StructureTest04_SparseBoard.class,
	SparseBoardTest.class,

	StructureTest05_Piece.class,
	PieceTest.class,

	StructureTest06_King.class,
	KingTest.class,
})
public class TestSuite {}
