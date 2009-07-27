package fungoes.lexiku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * In memory data store for word lists including search/filter methods.
 * 
 * @author benjamin.lee
 *
 */
public class Dictionary implements Iterable<String> {
	private Map<Integer, List<String>> wordsBySize;
	private Set<String> words;
	
	/**
	 * Create a new empty Dictionary
	 */
	public Dictionary() {
		initialize();
	}

	private void initialize() {
		words = new HashSet<String>();
		wordsBySize = new HashMap<Integer, List<String>>();
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
	public void loadWords(Reader reader) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		String word;
		while((word = bufferedReader.readLine()) != null) {
			word = word.toLowerCase();
			
			if(!words.contains(word)) {
				words.add(word);
				updateWordsBySizeSets(word);
			}
		}
		
		for (Integer wordSize : wordsBySize.keySet()) {
			Collections.sort(wordsBySize.get(wordSize));
		}
	}

	private void updateWordsBySizeSets(String word) {
		Integer size = new Integer(word.length());
		List<String> sameSizedWords = wordsBySize.get(size);
		
		if(sameSizedWords == null) {
			sameSizedWords = new ArrayList<String>(1000);
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
	public Iterator<String> iterator() {
		return words.iterator();
	}

	/**
	 * Returns an {@link Iterable} object containing references to all of the words in the dictionary with the given word length
	 * 
	 * @param wordLength desired word length
	 * @return all words matching the word length
	 */
	public Iterable<String> byLengthIterable(int wordLength) {
		Collection<String> wordList = wordsBySize.get(new Integer(wordLength));
		
		if(wordList != null) {
			return Collections.unmodifiableCollection(wordList);
		}
		
		return Collections.emptySet();
	}

	/**
	 * Returns true if the dictionary contains a word with the given prefix (this includes if the prefix equals a word) AND the given length.
	 * 
	 * @param prefix word prefix or the whole word
	 * @param wordLength length of words to search
	 * @return true if a matching word is in the dictionary
	 */
	public boolean containsPrefix(String prefix, int wordLength) {
		List<String> sizedWords = wordsBySize.get(new Integer(wordLength));
		
		if(sizedWords == null) {
			return false;
		}
		
		int resultIndex = Collections.binarySearch(sizedWords, prefix);
		
		if(resultIndex < 0) {
			resultIndex = (resultIndex + 1) * -1;
			
			if(resultIndex == sizedWords.size() || !sizedWords.get(resultIndex).startsWith(prefix)) {
				return false;
			}
		}
		
		return true;
	}

}
