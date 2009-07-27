package fungoes.lexiku;

/**
 * Type of a template tile.
 * 
 * @author benjamin.lee
 *
 */
public enum TileType {
	/**
	 * Open tiles require a letter.
	 */
	Open,
	
	/**
	 * Closed tiles will never have a letter (black on a crossword puzzle).
	 */
	Closed;
}
