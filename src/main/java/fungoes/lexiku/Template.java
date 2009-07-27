package fungoes.lexiku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Template for a board to be solved including the locations of letters and blocked spaces.
 * 
 * @author benjamin.lee
 *
 */
public class Template {
	public static final char OPEN_TILE_CHARACTER = 'O';
	public static final char CLOSED_TILE_CHARACTER = 'X';
	
	private final int width;
	private final int height;
	private final Tile[][] template;
	private final List<StartingPoint> horizontalWordStartingPoints;

	/**
	 * Loads a new template from the given input.  The input data should have one row per line, 'O' for open spaces and 'X' for closed.
	 * All lines from the reader should have the same length.
	 * 
	 * @param templateReader reader containing the template data
	 * @throws IOException
	 */
	public Template(Reader templateReader) throws IOException {
		template = buildTileTemplate(convertTemplateReaderToCharacters(new BufferedReader(templateReader)));
		
		width = template.length;
		height = template[0].length;
		
		horizontalWordStartingPoints = recordHorizontalStartingPoints();
		
		for(int x = 0; x < width; x++) {
			int wordLength = -1;
			
			for(int y = 0; y < height; y++) {
				if(template[x][y].type == TileType.Open) {
					if(wordLength == -1) {
						wordLength = 0;
					}
					
					wordLength++;
				}
				else { // closed
					if(wordLength != -1) {
						for(int dy = 1; dy <= wordLength; dy++) {
							template[x][y - dy].verticalWordLength = wordLength;
						}
						
						wordLength = -1;
					}
				}
			}
			
			if(wordLength != -1) {
				for(int dy = 1; dy <= wordLength; dy++) {
					template[x][height - dy].verticalWordLength = wordLength;
				}
				
				wordLength = -1;
			}
		}
	}

	private List<StartingPoint> recordHorizontalStartingPoints() {
		List<StartingPoint> points = new ArrayList<StartingPoint>();
		
		for(int y = 0; y < height; y++) {
			StartingPoint point = null;
			
			for(int x = 0; x< width; x++) {
				if(template[x][y].type == TileType.Open) {
					if(point == null) {
						point = new StartingPoint(x, y, 1);
					}
					else {
						point.length++;
					}
				}
				else { // closed
					if(point != null) {
						points.add(point);
						point = null;
					}
				}
			}
			
			if(point != null) {
				points.add(point);
			}
		}
		return points;
	}

	private List<char[]> convertTemplateReaderToCharacters(
			BufferedReader bufferedTemplate) throws IOException {
		List<char[]> dynamicTemplate = new ArrayList<char[]>();
		
		String line;
		while((line = bufferedTemplate.readLine()) != null) {
			dynamicTemplate.add(line.toCharArray());
		}
		return dynamicTemplate;
	}

	private static Tile[][] buildTileTemplate(List<char[]> dynamicTemplate) throws IOException {
		int width = dynamicTemplate.get(0).length;
		int height = dynamicTemplate.size();
		int lineLength = width;
		Tile[][] template = new Tile[width][height];
		
		for(int y = 0; y < height; y++) {
			char[] characters = dynamicTemplate.get(y);
			
			if(characters.length != lineLength && lineLength >= 0) {
				throw new IOException("Line lengths must all be the same.");
			}
			
			lineLength = characters.length;
			
			for(int x = 0; x< width; x++) {
				Tile tile = new Tile();
				
				if(characters[x] == OPEN_TILE_CHARACTER) {
					tile.type = TileType.Open;
				}
				else {
					tile.type = TileType.Closed;
				}
				
				template[x][y] = tile;
			}
		}
		
		return template;
	}

	/**
	 * @return template width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return template height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Finds the type of a given point in the template.
	 * 
	 * @param x
	 * @param y
	 * @return tile type
	 */
	public TileType checkTile(int x, int y) {
		return template[x][y].type;
	}

	/**
	 * Finds the {@link StartingPoint} of all horizontal words in the template.
	 * 
	 * @return list of starting points
	 */
	public List<StartingPoint> horizontalStartingPoints() {
		return horizontalWordStartingPoints;
	}

	/**
	 * For a given point, return the length of the vertical word it is part of. 
	 * 
	 * @param x
	 * @param y
	 * @return vertical word length
	 */
	public int verticalWordLengthFor(int x, int y) {
		return template[x][y].verticalWordLength;
	}
	
	private static class Tile {
		public TileType type = null;
		public int verticalWordLength = 0;
	}
	
}
