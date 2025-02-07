package com.drmagdiamer.nlpCoreLibrary.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class GloVeHelperTest {

  @Test
  void test1() throws IOException {
    // Example: Retrieve vector for the word "nlp"
    String word = "nlp";
    double[] wordVector = GloVeHelper.getWordVector(word);

    if (wordVector != null) {
      System.out.println("Vector for '" + word + "': ");
      for (double v : wordVector) {
        System.out.print(v + " ");
      }
      System.out.println();
    } else {
      System.out.println("Word '" + word + "' not found in GloVe model.");
    }

    // Example: Find cosine similarity between two words
    String word1 = "nlp";
    String word2 = "machine";
    double similarity = GloVeHelper.getSimilarity(word1, word2);
    System.out.println(
        "Cosine similarity between '" + word1 + "' and '" + word2 + "': " + similarity);
  }
}
