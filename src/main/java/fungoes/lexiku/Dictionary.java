package fungoes.lexiku;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * In memory data store for word lists including search/filter methods.
 * 
 * @author benjamin.lee
 *
 */
public class Dictionary implements Iterable<String> {
	private Map<Integer, Set<String>> wordsBySize;
	private Set<String> words;
	
	// TODO analyze performance
	
	/**
	 * Create a new empty Dictionary
	 */
	public Dictionary() {
		initialize();
	}

	private void initialize() {
		words = new HashSet<String>();
		wordsBySize = new HashMap<Integer, Set<String>>();
	}
	
	/**
	 * Produces a new Dictionary based on the given initial word list; assuming that all initial words are of the same given size.
	 * 
	 * @param size length of all initial words
	 * @param initialWords initial word list
	 */
	private Dictionary(Integer size, Set<String> initialWords) {
		this();
		
		if(initialWords != null) {
			words = initialWords;
			wordsBySize.put(size, initialWords);
		}
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return words.size();
	}

	/**
	 * Reviews the {@link BufferedReader} lines and creates a new word per line.  Duplicates, within one reader or between multiple invokations,
	 * will be counted only once.  Subsequent calls to loadWords are additive.
	 * 
	 * @param reader containing word list, one word per line
	 * @throws IOException
	 */
	public void loadWords(BufferedReader reader) throws IOException {
		String word;
		
		while((word = reader.readLine()) != null) {
			if(!words.contains(word)) {
				words.add(word);
				updateWordsBySizeCounts(word);
			}
		}
	}

	private void updateWordsBySizeCounts(String word) {
		Integer size = new Integer(word.length());
		Set<String> sameSizedWords = wordsBySize.get(size);
		
		if(sameSizedWords == null) {
			sameSizedWords = new HashSet<String>(1000);
		}
		
		sameSizedWords.add(word);
		
		wordsBySize.put(size, sameSizedWords);
	}

	/**
	 * Returns true if the given word is found in the dictionary.
	 * 
	 * @param candidate word to test
	 * @return true if the word has been loaded into the dictionary
	 */
	public boolean isWordPresent(String candidate) {
		return words.contains(candidate);
	}

	/**
	 * Iterates over all words loaded into the dictionary in an undefined order.
	 */
	@Override
	public Iterator<String> iterator() {
		return words.iterator();
	}

	/**
	 * Creates a new Dictionary based on the given one containing only those words of the given length
	 * 
	 * @param length desired filter length
	 * @return new Dictionary with words of the desired length
	 */
	public Dictionary produceLengthFilteredDictionary(int length) {
		// TODO rename size/length to be consistent
		
		Integer size = new Integer(length);
		return new Dictionary(size, wordsBySize.get(size));
	}

}
