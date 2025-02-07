package com.drmagdiamer.nlpCoreLibrary.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GloVeHelper {
  private static final String GLOVE_FILE_PATH = "data\\glove.840B.300d.txt"; // For 300D vectors
  // Map to hold word embeddings (word -> vector)
  private static Map<String, double[]> gloveMap = new HashMap<>();
  private static boolean isLoaded = false;

  public static synchronized void loadModel() throws IOException {
    if (isLoaded) {
      return;
    }
    // Path to the GloVe file (make sure to specify the correct path)
    String gloveFilePath = GLOVE_FILE_PATH; // For 300D vectors

    // Load GloVe vectors
    loadGloveModel(gloveFilePath);
    isLoaded = true;
  }

  public static double getSimilarity(String word1, String word2) {
    double[] vector1 = getWordVector(word1);
    double[] vector2 = getWordVector(word2);
    if (vector1 == null || vector2 == null) {
      return -1.0;
    }
    return cosineSimilarity(vector1, vector2);
  }

  // Method to load the GloVe model into memory
  private static void loadGloveModel(String filePath) throws IOException {
    System.out.println("Loading GloVe model from: " + filePath);
    BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

    String line;
    while ((line = br.readLine()) != null) {
      String[] values = line.split(" ");
      String word = values[0];
      double[] vector = new double[values.length - 1];

      // Read the vector values for the word
      for (int i = 1; i < values.length; i++) {
        vector[i - 1] = Double.parseDouble(values[i]);
      }
      gloveMap.put(word, vector);
    }
    br.close();
    System.out.println("GloVe model loaded. Total words: " + gloveMap.size());
  }

  // Method to retrieve the vector for a word
  public static double[] getWordVector(String word) {
    if (!isLoaded) {
      try {
        loadModel();
      } catch (IOException e) {
        log.error("Error loading GloVe model: " + e.getMessage());
        return new double[0];
      }
    }
    return gloveMap.get(word.toLowerCase());
  }

  // Method to calculate cosine similarity between two vectors
  private static double cosineSimilarity(double[] vectorA, double[] vectorB) {
    if (vectorA == null || vectorB == null) {
      return -1.0; // Return -1 if one of the vectors is null
    }

    double dotProduct = 0.0;
    double normA = 0.0;
    double normB = 0.0;

    for (int i = 0; i < vectorA.length; i++) {
      dotProduct += vectorA[i] * vectorB[i];
      normA += Math.pow(vectorA[i], 2);
      normB += Math.pow(vectorB[i], 2);
    }

    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
  }
}
