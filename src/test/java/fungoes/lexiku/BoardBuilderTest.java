package fungoes.lexiku;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;


public class BoardBuilderTest {
	
	@Test
	public void emptyDictionaryYieldsNoBoards() throws Exception {
		Dictionary dictionary = new Dictionary();
		Template template = new Template(new StringReader("OO\nOO"));
		
		List<String[][]> boards = new BoardBuilder().build(dictionary, template);
		
		assertEquals(0, boards.size());
	}
	
	
	@Test
	public void fourByFourTemplateProducesSingleBoard() throws Exception {
		Dictionary dictionary = new Dictionary();
		dictionary.loadWords(new StringReader("ab\nbc\nad\ndc"));
		
		Template template = new Template(new StringReader("OO\nOO"));
		
		BoardBuilder builder = new BoardBuilder();
		List<String[][]> boards = builder.build(dictionary, template);
		
		assertEquals(2, boards.size());
		
		String[][] board = boards.get(0);
		assertEquals(2, board.length);
		assertEquals(2, board[0].length);
		
	}
}
