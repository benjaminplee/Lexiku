package fungoes.lexiku;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Lexiku {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		if(args.length != 2) {
			System.out.println("2 Arguments Required. (dictionary file, template file)");
			return;
		}
		
		Dictionary dictionary = new Dictionary();
		dictionary.loadWords(new FileReader(new File(args[0])));
		
		Template template = new Template(new FileReader(new File(args[1])));
		
		BoardBuilder builder = new BoardBuilder();
		
		List<String[][]> boards = builder.build(dictionary, template);

		System.out.println("Boards Found: " + boards.size());
		
		//printBoard(template, boards);
	}

	public static void printBoard(Template template, List<String[][]> boards) {
		for(String[][] board : boards) {
			for(int y = 0; y < template.getHeight(); y++) {
				for(int x = 0; x < template.getWidth(); x++) {
					System.out.print(board[x][y]);
				}
				System.out.println();
			}
			
			System.out.println();
		}
	}

}
