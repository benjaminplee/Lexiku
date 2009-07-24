package fungoes.lexiku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BoardBuilder {

	public List<String[][]> build(Dictionary dictionary, Template template) {

		List<String[][]> completedBoards = new ArrayList<String[][]>();
		Space[][] spaces = emptySpaces(template);
		List<StartingPoint> startingPoints = template.horizontalStartingPoints();
		int currentStartingPoints = 0;
		Set<String> usedWords = new HashSet<String>(startingPoints.size() * 5);
		
		fillSpaces(spaces, startingPoints, currentStartingPoints, dictionary, template, completedBoards, usedWords);
		
		return completedBoards;
	}

	private void fillSpaces(Space[][] spaces,
			List<StartingPoint> startingPoints, int currentStartingPoint,
			Dictionary dictionary, Template template,
			List<String[][]> completedBoards, Set<String> usedWords) {
		
		if(currentStartingPoint == startingPoints.size()) {
			String[][] completedBoard = new String[template.getWidth()][template.getHeight()];
			
			for(int x = 0; x < template.getWidth(); x++) {
				for(int y = 0; y < template.getHeight(); y++) {
					completedBoard[x][y] = spaces[x][y].letter;
				}
			}
			
			Lexiku.printBoard(template, completedBoards);
			
			completedBoards.add(completedBoard);
		}
		else {
			StartingPoint sPoint = startingPoints.get(currentStartingPoint++);
			
			for(String candidateWord : dictionary.byLengthIterable(sPoint.length)) {
				if(candidateWord.length() == 1 || !usedWords.contains(candidateWord)) {
					boolean works = true;
					
					if(sPoint.y > 0) {
						for(int i = 0; i < sPoint.length && works; i++) {
							int dx = sPoint.x + i;
							int dy = sPoint.y - 1;
							
							String prefix = spaces[dx][dy].verticalWordSoFar + String.valueOf(candidateWord.charAt(i));
							int verticalWordLength = template.verticalWordLengthFor(dx, sPoint.y);
							
							if(prefix.length() == verticalWordLength) {
								works = !prefix.equals(candidateWord) && (prefix.length() == 1 || !usedWords.contains(prefix)) && dictionary.isWordPresent(prefix);
							}
							else {
								works = dictionary.containsPrefix(prefix, verticalWordLength);
							}
						}
					}
					
					if(works) {
						for(int i = 0; i < sPoint.length; i++) {
							int dx = sPoint.x + i;
							int dy = sPoint.y - 1;
							
							spaces[dx][sPoint.y].letter = String.valueOf(candidateWord.charAt(i));
							
							if(sPoint.y > 0) {
								spaces[dx][sPoint.y].verticalWordSoFar = spaces[dx][dy].verticalWordSoFar + String.valueOf(candidateWord.charAt(i));
							}
							else {
								spaces[dx][sPoint.y].verticalWordSoFar = spaces[dx][sPoint.y].letter;
							}
						}
						
						usedWords.add(candidateWord);
						fillSpaces(spaces, startingPoints, currentStartingPoint, dictionary, template, completedBoards, usedWords);
						usedWords.remove(candidateWord);
					}
				}
			}
		}
		
	}

	private Space[][] emptySpaces(Template template) {
		Space[][] spaces = new Space[template.getWidth()][template.getHeight()];
		for(int x = 0; x < template.getWidth(); x++) {
			for(int y = 0; y < template.getHeight(); y++) {
				spaces[x][y] = new Space();
			}
		}
		return spaces;
	}
	
	private static class Space {
		public String letter = "-";
		public String verticalWordSoFar = "";
	}
	
}
