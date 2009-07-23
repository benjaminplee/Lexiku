package fungoes.lexiku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DictionaryTest {
	private static final String LETTER_WORDS = "aa\nbb\ncccc\ndddd\nee\nf";
	private static final String TWO_ANIMALS = "zebra\nsnake";
	private static final String THREE_ANIMALS = "dog\ncat\nbird";
	private Dictionary dictionary;
	
	@Before
	public void setup() {
		dictionary = new Dictionary();
	}

	@Test
	public void newDictionaryIsEmpty() {
		assertTrue(dictionary.isEmpty());
	}
	
	@Test
	public void newDictionaryHasZeroSize() throws Exception {
		assertEquals(0, dictionary.size());
	}
	
	// TODO modify api to use Reader instead of BufferedReader
	
	@Test
	public void emptyInputStreamDoesNotChangeSizeOrIfEmpty() throws Exception {
		dictionary.loadWords(new BufferedReader(new StringReader("")));
		
		assertTrue(dictionary.isEmpty());
		assertEquals(0, dictionary.size());
	}
	
	@Test
	public void loadingWordsCausesDictionaryToNotBeEmpty() throws Exception {
		dictionary.loadWords(new BufferedReader(new StringReader(THREE_ANIMALS)));
		
		assertFalse(dictionary.isEmpty());
	}
	
	@Test
	public void loadingWordsCausesSizeToChangeFromZero() throws Exception {
		dictionary.loadWords(new BufferedReader(new StringReader(THREE_ANIMALS)));
		
		assertEquals(3, dictionary.size());
	}
	
	@Test
	public void loadWordsIsAddative() throws Exception {
		dictionary.loadWords(new BufferedReader(new StringReader(THREE_ANIMALS)));
		dictionary.loadWords(new BufferedReader(new StringReader(TWO_ANIMALS)));
		
		assertEquals(5, dictionary.size());
	}
	
	@Test
	public void loadingTheSameWordTwiceInTheSameLoadResultsInOnlyOneEntry() throws Exception {
		dictionary.loadWords(new BufferedReader(new StringReader("dog\ndog\ncat")));
		
		assertEquals(2, dictionary.size());
		assertTrue(dictionary.isWordPresent("dog"));
		assertTrue(dictionary.isWordPresent("cat"));
	}
	
	@Test
	public void loadingTheSameWordTwiceFromDifferentLoadsResultsInOnlyOneEntry() throws Exception {
		dictionary.loadWords(new BufferedReader(new StringReader("dog\ncat")));
		dictionary.loadWords(new BufferedReader(new StringReader("dog\ncat")));
		
		assertEquals(2, dictionary.size());
		assertTrue(dictionary.isWordPresent("dog"));
		assertTrue(dictionary.isWordPresent("cat"));
	}
	
	@Test
	public void unloadedWordsAreNotPresent() throws Exception {
		dictionary.loadWords(new BufferedReader(new StringReader(TWO_ANIMALS)));

		assertFalse(dictionary.isWordPresent("monkey"));
		assertFalse(dictionary.isWordPresent("goose"));
	}
	
	@Test
	public void canFindLoadedWords() throws Exception {
		dictionary.loadWords(new BufferedReader(new StringReader(THREE_ANIMALS)));
		
		assertTrue(dictionary.isWordPresent("dog"));
		assertTrue(dictionary.isWordPresent("cat"));
		assertTrue(dictionary.isWordPresent("bird"));
	}
	
	@SuppressWarnings("serial")
	@Test
	public void dictionaryProvidesIterator() throws Exception {
		Set<String> expectedWords = new HashSet<String>() {{
			add("dog");
			add("cat");
			add("bird");
			add("zebra");
			add("snake");
		}};
		
		dictionary.loadWords(new BufferedReader(new StringReader(THREE_ANIMALS)));
		dictionary.loadWords(new BufferedReader(new StringReader(TWO_ANIMALS)));
		
		assertThatTheIterableHasTheSameValuesAsTheGivenSet(expectedWords, dictionary);
	}

	private void assertThatTheIterableHasTheSameValuesAsTheGivenSet(Set<String> expectedWords, Iterable<String> iterable)  {
		for(String word : iterable) {
			if(expectedWords.contains(word)) {
				expectedWords.remove(word);
			}
			else {
				fail("Unexpected word was found in the dictionary: " + word);
			}
		}
		
		if(expectedWords.size() != 0) {
			fail("Not all of the expected words were iterated over by the dictionary: " + Arrays.toString(expectedWords.toArray(new String[expectedWords.size()])));
		}
	}
	
	@SuppressWarnings("serial")
	@Test
	public void dicationaryHasWordLengthIterators() throws Exception {
		Set<String> expectedWords = new HashSet<String>() {{
			add("aa");
			add("bb");
			add("ee");
		}};
		
		dictionary.loadWords(new BufferedReader(new StringReader(LETTER_WORDS)));
		
		assertThatTheIterableHasTheSameValuesAsTheGivenSet(expectedWords, dictionary.byLengthIterable(2));
	}
	
	@Test
	public void wordLengthIterablesAreValidButEmptyForWordLengthsWithNoWords() throws Exception {
		dictionary.loadWords(new BufferedReader(new StringReader(LETTER_WORDS)));
		
		Iterable<String> wordsByLength = dictionary.byLengthIterable(3);
		
		Iterator<String> iterator = wordsByLength.iterator();
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void dictionaryKnowsIfWordsExistWithGivenPrefix() throws Exception {
		dictionary.loadWords(new BufferedReader(new StringReader(THREE_ANIMALS)));
		dictionary.loadWords(new BufferedReader(new StringReader(TWO_ANIMALS)));
		
		assertTrue(dictionary.containsPrefix("d", 3));
		assertTrue(dictionary.containsPrefix("do", 3));
		assertTrue(dictionary.containsPrefix("dog", 3));
		assertTrue(dictionary.containsPrefix("c", 3));
		assertTrue(dictionary.containsPrefix("sna", 5));
		
		assertFalse(dictionary.containsPrefix("z", 3));
		assertFalse(dictionary.containsPrefix("jo", 3));
		assertFalse(dictionary.containsPrefix("dogz", 3));
		assertFalse(dictionary.containsPrefix("zdog", 4));
		
		assertFalse(dictionary.containsPrefix("d", 5));
		assertFalse(dictionary.containsPrefix("do", 10));
		assertFalse(dictionary.containsPrefix("do", 2));
	}

}
