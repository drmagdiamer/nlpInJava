package com.drmagdiamer.nlpCoreLibrary.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NGramUtilTest {
  ////////////////////////////
  @Test
  void generateNGrams_createsCorrectNGramsForSingleWord() {
    String text = "test";
    List<String> result = NGramUtil.generateNGrams(text, 1, true);
    assertEquals(List.of("test"), result);
  }

  @Test
  void generateNGrams_createsCorrectNGramsForMultipleWords() {
    String text = "this is a test";
    List<String> result = NGramUtil.generateNGrams(text, 2, true);
    assertEquals(List.of("this is", "is a", "a test"), result);
  }

  @Test
  void generateNGrams_handlesEmptyString() {
    String text = "";
    List<String> result = NGramUtil.generateNGrams(text, 2, true);
    assertEquals(List.of(), result);
  }

  @Test
  void generateNGrams_handlesNGreaterThanWordCount() {
    String text = "test";
    List<String> result = NGramUtil.generateNGrams(text, 2, true);
    assertEquals(List.of(), result);
  }

  @Test
  void generateNGrams_handlesCaseSensitivity() {
    String text = "This is a Test";
    List<String> result = NGramUtil.generateNGrams(text, 2, false);
    assertEquals(List.of("This is", "is a", "a Test"), result);
  }

  @Test
  void generateNGrams_handlesMultipleSpaces() {
    String text = "this  is   a    test";
    List<String> result = NGramUtil.generateNGrams(text, 2, true);
    assertEquals(List.of("this is", "is a", "a test"), result);
  }

  @Test
  void generateNGrams_handlesLeadingAndTrailingSpaces() {
    String text = "  this is a test  ";
    List<String> result = NGramUtil.generateNGrams(text, 2, true);
    assertEquals(List.of("this is", "is a", "a test"), result);
  }

  @Test
  void generateNGrams_handlesLeadingAndTrailingSpacesWithNGreaterThanWordCount() {
    // Example text
    String text = "The quick brown fox jumps over the lazy dog";

    // Generate word-based 2-grams (bigrams)
    List<String> wordBigrams = NGramUtil.generateNGrams(text, 2, true);
    System.out.println("Word-based 2-grams:");
    for (String bigram : wordBigrams) {
      System.out.println(bigram);
    }
    assertEquals(8, wordBigrams.size());
    System.out.println("...");

    // Generate word-based 3-grams (bigrams)
    List<String> wordTrigrams = NGramUtil.generateNGrams(text, 3, true);
    System.out.println("Word-based 3-grams:");
    for (String bigram : wordTrigrams) {
      System.out.println(bigram);
    }
    assertEquals(7, wordTrigrams.size());
    System.out.println("...");

    // Generate character-based 3-grams (trigrams)
    List<String> charTrigrams = NGramUtil.generateCharacterNGrams(text, 3);
    System.out.println("\nCharacter-based 3-grams:");
    for (String trigram : charTrigrams) {
      System.out.println(trigram);
    }
  }

  @Test
  public void test1() {
    // Input text (the corpus)
    String[] corpusArr = {
      "NLP has changed the world", "I love NLP", "NLP is cool", "NLP is future"
    };
    List<String> corpus = Arrays.asList(corpusArr);

    // The sentence we want to vectorize
    String sentenceToVectorize = "NLP has changed the world";

    // Build the vocabulary of 2-grams (bigrams)
    int n = 2; // Using bigrams for this example
    Set<String> vocabulary = NGramUtil.buildVocabulary(corpus, n, true);

    // Vectorize the sentence
    int[] vector = NGramUtil.vectorizeSentence(sentenceToVectorize, vocabulary, n, true);

    // Print the vector representation
    System.out.println("Vector representation of the sentence:");
    System.out.println(Arrays.toString(vector));

    // Print the vocabulary for reference
    System.out.println("\nVocabulary (Bigrams):");
    System.out.println(vocabulary);
    // Vector representation of the sentence:
    // [0, 0, 1, 1, 0, 0, 0, 1, 1]
    //
    // Vocabulary (Bigrams):
    // [nlp is, is cool, has changed, changed the, love nlp, i love, is future, the world, nlp has]
  }
}
