package fungoes.lexiku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Template {
	public static final char OPEN_TILE_CHARACTER = 'O';
	public static final char CLOSED_TILE_CHARACTER = 'X';
	
	private final int width;
	private final int height;
	private final char[][] template;

	public Template(Reader templateReader) throws IOException {
		BufferedReader bufferedTemplate = new BufferedReader(templateReader);
		int lineCount = 0;
		int lineLength = -1;
		List<char[]> dynamicTemplate = new ArrayList<char[]>();
		
		String line;
		while((line = bufferedTemplate.readLine()) != null) {
			lineCount++;
			
			if(line.length() != lineLength && lineLength >= 0) {
				throw new IOException("Line lengths must all be the same.");
			}
			
			lineLength = line.length();
			
			dynamicTemplate.add(line.toCharArray());
		}
		
		width = lineLength;
		height = lineCount;
		
		template = dynamicTemplate.toArray(new char[width][height]);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Tile checkTile(int x, int y) {
		if(OPEN_TILE_CHARACTER == template[x][y]) {
			return Tile.Open;
		}
		
		return Tile.Closed;
	}

}
