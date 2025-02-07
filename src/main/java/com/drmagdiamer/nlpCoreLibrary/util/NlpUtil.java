package com.drmagdiamer.nlpCoreLibrary.util;

import java.util.*;
import java.util.stream.Collectors;

public class NlpUtil {
  private NlpUtil() {}

  public static List<String> parseSentence(String sentence, boolean toLowerCase) {
    return Arrays.stream(sentence.split("[ .,!?]"))
        .filter(word -> !word.isEmpty())
        .map(word -> toLowerCase ? word.toLowerCase() : word)
        .collect(Collectors.toList());
  }

  public static List<Integer[]> oneHotEncode(String sentence, boolean toLowerCase) {
    return oneHotEncode(parseSentence(sentence, toLowerCase));
  }

  public static List<Integer[]> oneHotEncode(List<String> categories) {
    // Get unique categories
    Set<String> uniqueCategories = new HashSet<>(categories);
    List<String> categoryList = new ArrayList<>(uniqueCategories);
    Collections.sort(categoryList); // Ensuring consistency in the order

    // Create the one-hot encoding
    List<Integer[]> encodedList = new ArrayList<>();
    for (String category : categories) {
      int[] encoded = new int[categoryList.size()];
      int index = categoryList.indexOf(category);
      encoded[index] = 1; // Set the correct index to 1
      Integer[] boxedEncoded = Arrays.stream(encoded).boxed().toArray(Integer[]::new);
      encodedList.add(boxedEncoded);
    }
    return encodedList;
  }

  // Method to build a Bag of Words model
  public static Integer[][] buildBagOfWords(List<String> documents, boolean toLowerCase) {
    // Tokenize documents and build a vocabulary
    Set<String> vocabulary = new HashSet<>();
    List<List<String>> tokenizedDocuments = new ArrayList<>();
    for (String doc : documents) {
      List<String> tokens = parseSentence(doc, toLowerCase);
      tokenizedDocuments.add(tokens);
      vocabulary.addAll(tokens);
    }
    // Convert vocabulary to a sorted list for consistent ordering
    List<String> vocabList = new ArrayList<>(vocabulary);
    Collections.sort(vocabList);
    // Initialize the bag-of-words matrix
    int[][] bowMatrix = new int[documents.size()][vocabList.size()];
    // Fill the bag-of-words matrix
    for (int i = 0; i < tokenizedDocuments.size(); i++) {
      List<String> tokens = tokenizedDocuments.get(i);
      for (String token : tokens) {
        int index = vocabList.indexOf(token);
        bowMatrix[i][index]++; // Increment count for the word in this document
      }
    }
    // Convert int[][] to Integer[][]
    Integer[][] boxedBowMatrix = new Integer[bowMatrix.length][];
    for (int i = 0; i < bowMatrix.length; i++) {
      boxedBowMatrix[i] = Arrays.stream(bowMatrix[i]).boxed().toArray(Integer[]::new);
    }
    return boxedBowMatrix;
  }

  // Method to calculate TF-IDF
  public static Double tfIdf(String[] doc, List<String[]> docs, String term, boolean toLowerCase) {
    return Double.valueOf(TfIdf.tfIdf(doc, docs, term, toLowerCase));
  }

  // Method to build a TF-IDF model
  public static Double[][] buildTfIdf(List<String> documents, boolean toLowerCase) {
    // Tokenize documents and build a vocabulary
    Set<String> vocabulary = new HashSet<>();
    List<List<String>> tokenizedDocuments = new ArrayList<>();
    for (String doc : documents) {
      List<String> tokens = parseSentence(doc, toLowerCase);
      tokenizedDocuments.add(tokens);
      vocabulary.addAll(tokens);
    }
    // Convert vocabulary to a sorted list for consistent ordering
    List<String> vocabList = new ArrayList<>(vocabulary);
    Collections.sort(vocabList);
    // Initialize the term frequency matrix
    int[][] tfMatrix = new int[documents.size()][vocabList.size()];
    // Fill the term frequency matrix
    for (int i = 0; i < tokenizedDocuments.size(); i++) {
      List<String> tokens = tokenizedDocuments.get(i);
      for (String token : tokens) {
        int index = vocabList.indexOf(token);
        tfMatrix[i][index]++; // Increment count for the word in this document
      }
    }
    // Compute the inverse document frequency
    double[] idfVector = new double[vocabList.size()];
    for (int j = 0; j < vocabList.size(); j++) {
      int docsWithTerm = 0;
      for (int i = 0; i < documents.size(); i++) {
        if (tfMatrix[i][j] > 0) {
          docsWithTerm++;
        }
      }
      idfVector[j] = Math.log((double) documents.size() / (1 + docsWithTerm));
    }
    // Initialize the TF-IDF matrix
    double[][] tfIdfMatrix = new double[documents.size()][vocabList.size()];
    // Fill the TF-IDF matrix
    for (int i = 0; i < documents.size(); i++) {
      for (int j = 0; j < vocabList.size(); j++) {
        tfIdfMatrix[i][j] = tfMatrix[i][j] * idfVector[j];
      }
    }
    // Convert double[][] to Double[][]
    Double[][] boxedTfIdfMatrix = new Double[tfIdfMatrix.length][];
    for (int i = 0; i < tfIdfMatrix.length; i++) {
      boxedTfIdfMatrix[i] = Arrays.stream(tfIdfMatrix[i]).boxed().toArray(Double[]::new);
    }
    return boxedTfIdfMatrix;
  }
}
