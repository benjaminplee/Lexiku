package fungoes.lexiku;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class TemplateTest {
	private static final String WRONG_SIZE_BOARD = "OOOO\nOXXO\nOO\nOO";
	private static final String MEDIUM_BOARD = "OOOOO\nOXXXO\nOXXXO\nOOOOO";
	private static final String SMALL_BOARD = "OOOO\nOXXO\nOXXO";
	
	@Test(expected = IOException.class)
	public void allTemplateLinesMustHaveTheSameLength() throws Exception {
		new Template(new StringReader(WRONG_SIZE_BOARD));
	}
	
	@Test
	public void boardHeightAndWidthAreComputedFromTemplate() throws Exception {
		Template board = new Template(new StringReader(MEDIUM_BOARD));
		
		assertEquals(5, board.getWidth());
		assertEquals(4, board.getHeight());
	}
	
	@Test
	public void templateIsLoadedCorrectly() throws Exception {
		Template board = new Template(new StringReader(SMALL_BOARD));
		
		assertEquals(4, board.getWidth());
		assertEquals(3, board.getHeight());
		
		assertEquals(Tile.Open, board.checkTile(0, 0));
		assertEquals(Tile.Open, board.checkTile(0, 1));
		assertEquals(Tile.Open, board.checkTile(0, 2));
		assertEquals(Tile.Open, board.checkTile(0, 3));
		
		assertEquals(Tile.Open, board.checkTile(1, 0));
		assertEquals(Tile.Closed, board.checkTile(1, 1));
		assertEquals(Tile.Closed, board.checkTile(1, 2));
		assertEquals(Tile.Open, board.checkTile(1, 3));
		
		assertEquals(Tile.Open, board.checkTile(2, 0));
		assertEquals(Tile.Closed, board.checkTile(2, 1));
		assertEquals(Tile.Closed, board.checkTile(2, 2));
		assertEquals(Tile.Open, board.checkTile(2, 3));
	}
}
