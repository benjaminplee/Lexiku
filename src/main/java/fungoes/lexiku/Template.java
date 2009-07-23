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
	private final Tile[][] template;
	private final List<StartingPoint> horizontalWordStartingPoints = new ArrayList<StartingPoint>();

	public Template(Reader templateReader) throws IOException {
		template = buildTileTemplate(convertTemplateReaderToCharacters(new BufferedReader(templateReader)));
		
		width = template.length;
		height = template[0].length;
		
		for(int y = 0; y < height; y++) {
			StartingPoint point = null;
			
			for(int x = 0; x< width; x++) {
				if(template[x][y] == Tile.Open) {
					if(point == null) {
						point = new StartingPoint(x, y, 1);
					}
					else {
						point.length++;
					}
				}
				else if(template[x][y] == Tile.Closed) {
					if(point != null) {
						horizontalWordStartingPoints.add(point);
						point = null;
					}
				}
			}
			
			if(point != null) {
				horizontalWordStartingPoints.add(point);
			}
		}
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
				if(characters[x] == OPEN_TILE_CHARACTER) {
					template[x][y] = Tile.Open;
				}
				else {
					template[x][y] = Tile.Closed;
				}
			}
		}
		
		return template;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Tile checkTile(int x, int y) {
		return template[x][y];
	}

	public List<StartingPoint> horizontalStartingPoints() {
		return horizontalWordStartingPoints;
	}

}
