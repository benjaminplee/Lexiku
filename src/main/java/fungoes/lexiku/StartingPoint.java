package fungoes.lexiku;

/**
 * Starting point for a word on a given board.
 * 
 * @author benjamin.lee
 *
 */
public class StartingPoint {
	public int x;
	public int y;
	public int length;
	
	/**
	 * The starting point for a particular word.  Does not denote the word's direction.
	 * 
	 * @param x
	 * @param y
	 * @param length word length including starting point
	 */
	public StartingPoint(int x, int y, int length) {
		this.x = x;
		this.y = y;
		this.length = length;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + length;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StartingPoint other = (StartingPoint) obj;
		if (length != other.length)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + x + "," + y + ":" + length + ")";
	}
	
}
