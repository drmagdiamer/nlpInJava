package com.drmagdiamer.nlpCoreLibrary.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NGramUtil {
  // Method to generate word-based n-grams
  public static List<String> generateNGrams(String text, int n, boolean toLowerCase) {
    List<String> words = NlpUtil.parseSentence(text, toLowerCase); // Split the text into words
    List<String> nGrams = new ArrayList<>();

    for (int i = 0; i <= words.size() - n; i++) {
      StringBuilder nGram = new StringBuilder();
      for (int j = 0; j < n; j++) {
        if (j > 0) {
          nGram.append(" "); // Add space between words
        }
        nGram.append(words.get(i + j));
      }
      nGrams.add(nGram.toString());
    }

    return nGrams;
  }

  // Method to generate character-based n-grams
  public static List<String> generateCharacterNGrams(String text, int n) {
    List<String> nGrams = new ArrayList<>();

    for (int i = 0; i <= text.length() - n; i++) {
      String nGram = text.substring(i, i + n); // Extract substring of length n
      nGrams.add(nGram);
    }

    return nGrams;
  }

  // Method to create a vocabulary of n-grams from all sentences
  public static Set<String> buildVocabulary(List<String> sentences, int n, boolean toLowerCase) {
    Set<String> vocabulary = new HashSet<>();
    for (String sentence : sentences) {
      vocabulary.addAll(generateNGrams(sentence, n, toLowerCase));
    }
    return vocabulary;
  }

  // Method to vectorize a sentence based on the vocabulary
  public static int[] vectorizeSentence(
      String sentence, Set<String> vocabulary, int n, boolean toLowerCase) {
    List<String> sentenceNGrams = generateNGrams(sentence, n, toLowerCase);
    int[] vector = new int[vocabulary.size()];
    List<String> vocabList = new ArrayList<>(vocabulary); // Convert set to list for indexing
    // Create the vector: 1 if n-gram exists in the sentence, 0 otherwise
    for (int i = 0; i < vocabList.size(); i++) {
      if (sentenceNGrams.contains(vocabList.get(i))) {
        vector[i] = 1;
      } else {
        vector[i] = 0;
      }
    }
    return vector;
  }
}
