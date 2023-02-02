package spell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    private Trie trie;

    public SpellCorrector() {
        trie = new Trie();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        try {
            Scanner sc = new Scanner(new File(dictionaryFileName));

            while (sc.hasNext()) {
                String word = sc.next();
                trie.add(word);
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        // Convert word to lowercase
        inputWord = inputWord.toLowerCase();

        // If word is an empty string
        if (inputWord.isEmpty()) {
            return null;
        }

        // If word is spelled correctly
        if (trie.find(inputWord) != null) {
            return inputWord;
        }

        // List of all the edit distance 1 words
        ArrayList<String> editDistance1Words = new ArrayList<>();
        // List of all the edit distance 2 words
        ArrayList<String> editDistance2Words = new ArrayList<>();
        // Ordered Set of all possible word suggestions
        Set<String> possibleWordSuggestions = new TreeSet<>();

        // Generate edit distance 1 list
        generateEditDistanceWords(editDistance1Words, inputWord);

        // Populate possible word suggestions set
        for (String suggestion : editDistance1Words) {
            if (trie.find(suggestion) != null) {
                possibleWordSuggestions.add(suggestion);
            }
        }

        // If the set is not empty, return the most accurate suggestion
        if (!possibleWordSuggestions.isEmpty()) {
            return findSuggestion(possibleWordSuggestions);
        } else {
            // Generate edit distance 2 list using all the edit distance 1 words
            for (String generatedWord : editDistance1Words) {
                generateEditDistanceWords(editDistance2Words, generatedWord);
            }

            // Populate possible word suggestions set
            for (String suggestion : editDistance2Words) {
                if (trie.find(suggestion) != null) {
                    possibleWordSuggestions.add(suggestion);
                }
            }

            // Return the most accurate suggestion, or null.
            return findSuggestion(possibleWordSuggestions);
        }
    }

    public void generateEditDistanceWords(ArrayList<String> editDistanceWords, String word) {
        editDistanceWords.addAll(deletionDistance(word));
        editDistanceWords.addAll(transpositionDistance(word));
        editDistanceWords.addAll(alterationDistance(word));
        editDistanceWords.addAll(insertionDistance(word));
    }

    public String findSuggestion(Set<String> possibleWordSuggestions) {
        if (!possibleWordSuggestions.isEmpty()) {
            int maxCount = 0;
            String suggestion = null;

            for (String word : possibleWordSuggestions) {
                if (trie.find(word) != null) {
                    int freq = trie.find(word).getValue();

                    if (freq > maxCount) {
                        maxCount = freq;
                        suggestion = word;
                    }
                }
            }
            return suggestion;
        }
        return null;
    }

    public static List<String> deletionDistance(String word) {
        List<String> deletionDistance = new ArrayList<>();

        // Iterate through all the letters in the word
        for (int i = 0; i < word.length(); i++) {
            // Add the part before and after the deleted letter
            deletionDistance.add(word.substring(0, i) + word.substring(i + 1));
        }

        return deletionDistance;
    }

    public static List<String> transpositionDistance(String word) {
        List<String> transpositionDistance = new ArrayList<>();

        // Iterate through all the letters in the word (except for the last one)
        for (int i = 0; i < word.length() - 1; i++) {
            // Convert string to a char array
            char[] charArray = word.toCharArray();
            // Save the temporary letter
            char temp = charArray[i];
            // Swap the current with the next
            charArray[i] = charArray[i + 1];
            // Swap the next with the curr (saved as temp)
            charArray[i + 1] = temp;

            // Add to list (and convert back to a string)
            transpositionDistance.add(new String(charArray));
        }

        return transpositionDistance;
    }

    public static List<String> alterationDistance(String word)  {
        List<String> alterationDistance = new ArrayList<>();

        // Iterate through all the letters in the word and produce every single possible combination w/ edit distance 1
        for (int i = 0; i < word.length(); i++) {
            // For every single letter in the alphabet
            for (char c = 'a'; c <= 'z'; c++) {
                // If the letter isn't the same as the replacement...
                if (word.charAt(i) != c) {
                    // Swap the letter
                    alterationDistance.add(word.substring(0, i) + c + word.substring(i + 1));
                }
            }
        }
        return alterationDistance;
    }

    public static List<String> insertionDistance(String word) {
        List<String> insertionDistance = new ArrayList<>();

        // Iterate through all the letters in the word
        for (int i = 0; i <= word.length(); i++) {
            // For every single letter in the alphabet
            for (char c = 'a'; c <= 'z'; c++) {
                // Add to the list every single possible combination of the word with 1 extra letter in all possible places
                insertionDistance.add(word.substring(0, i) + c + word.substring(i));
            }
        }
        return insertionDistance;
    }

}
