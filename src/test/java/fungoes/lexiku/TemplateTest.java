package fungoes.lexiku;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

public class TemplateTest {
	private static final String WRONG_SIZE_BOARD = "OOOO\nOXXO\nOO\nOO";
	private static final String MEDIUM_BOARD = "OOOOO\nOXXXO\nOXXXO\nOOOOO";
	private static final String SMALL_BOARD = "OOOO\nOXXO\nOXXO";
	private static final String LARGE_BOARD = "XOOOXXOOOX" + "\n" +
												"OOOOOOOOOO" + "\n" + 
												"OOOOOOOOOO" + "\n" + 
												"OOOOOOOOOO" + "\n" + 
												"XOOOXXOOOX" + "\n" + 
												"XOOOXXOOOX" + "\n" + 
												"OOOOOOOOOO" + "\n" + 
												"OOOOOOOOOO" + "\n" + 
												"OOOOOOOOOO" + "\n" + 
												"XOOOXXOOOX";
	
	@Test(expected = IOException.class)
	public void allTemplateLinesMustHaveTheSameLength() throws Exception {
		new Template(new StringReader(WRONG_SIZE_BOARD));
	}
	
	@Test
	public void heightAndWidthAreComputedFromTemplate() throws Exception {
		Template template = new Template(new StringReader(MEDIUM_BOARD));
		
		assertEquals(5, template.getWidth());
		assertEquals(4, template.getHeight());
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void pastBoundsThrowsException() throws Exception {
		Template template = new Template(new StringReader(SMALL_BOARD));
		template.checkTile(20, 20);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void negativeTileLocationThrowsException() throws Exception {
		Template template = new Template(new StringReader(SMALL_BOARD));
		template.checkTile(-20, -20);
	}
	
	@Test
	public void templateIsLoadedCorrectly() throws Exception {
		Template template = new Template(new StringReader(SMALL_BOARD));
		
		assertEquals(4, template.getWidth());
		assertEquals(3, template.getHeight());
		
		assertEquals(Tile.Open, template.checkTile(0, 0));
		assertEquals(Tile.Open, template.checkTile(1, 0));
		assertEquals(Tile.Open, template.checkTile(2, 0));
		assertEquals(Tile.Open, template.checkTile(3, 0));
		
		assertEquals(Tile.Open, template.checkTile(0, 1));
		assertEquals(Tile.Closed, template.checkTile(1, 1));
		assertEquals(Tile.Closed, template.checkTile(2, 1));
		assertEquals(Tile.Open, template.checkTile(3, 1));
		
		assertEquals(Tile.Open, template.checkTile(0, 2));
		assertEquals(Tile.Closed, template.checkTile(1, 2));
		assertEquals(Tile.Closed, template.checkTile(2, 2));
		assertEquals(Tile.Open, template.checkTile(3, 2));
	}
	
	@Test
	public void findsHorizontalWordStartingPointsLeftToRightTopToBottom() throws Exception {
		Template template = new Template(new StringReader(LARGE_BOARD));
		
		assertEquals(10, template.getWidth());
		assertEquals(10, template.getHeight());
		
		List<StartingPoint> startingPoints = template.horizontalStartingPoints();
		
		assertEquals(14, startingPoints.size());
		assertEquals(new StartingPoint(1, 0, 3), startingPoints.get(0));
		assertEquals(new StartingPoint(6, 0, 3), startingPoints.get(1));
		assertEquals(new StartingPoint(0, 1, 10), startingPoints.get(2));
		assertEquals(new StartingPoint(0, 2, 10), startingPoints.get(3));
		assertEquals(new StartingPoint(0, 3, 10), startingPoints.get(4));
		assertEquals(new StartingPoint(1, 4, 3), startingPoints.get(5));
		assertEquals(new StartingPoint(6, 4, 3), startingPoints.get(6));
		assertEquals(new StartingPoint(1, 5, 3), startingPoints.get(7));
		assertEquals(new StartingPoint(6, 5, 3), startingPoints.get(8));
		assertEquals(new StartingPoint(0, 6, 10), startingPoints.get(9));
		assertEquals(new StartingPoint(0, 7, 10), startingPoints.get(10));
		assertEquals(new StartingPoint(0, 8, 10), startingPoints.get(11));
		assertEquals(new StartingPoint(1, 9, 3), startingPoints.get(12));
		assertEquals(new StartingPoint(6, 9, 3), startingPoints.get(13));
	}
	
	
}
