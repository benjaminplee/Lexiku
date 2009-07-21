package fungoes.lexiku;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DictionaryTest {
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
		
		for(String word : dictionary) {
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
	
	@Test
	public void dictionaryCanFilterByLength() throws Exception {
		dictionary.loadWords(new BufferedReader(new StringReader(THREE_ANIMALS)));
		Dictionary filteredDictionary = dictionary.produceLengthFilteredDictionary(3);
		
		assertEquals(2, filteredDictionary.size());
		assertTrue(filteredDictionary.isWordPresent("dog"));
		assertTrue(filteredDictionary.isWordPresent("cat"));
		assertFalse(filteredDictionary.isWordPresent("bird"));
	}
	
//	@Test
//	public void dictionaryCanFilterByPrefix() throws Exception {
//		
//	}
	
	// TODO test filter by sub string or wildcard description (prefix, substring, suffix)
	
	// TODO test check returning true if words exist with given constraints, false if there are none
}
