package com.drmagdiamer.nlpCoreLibrary.util;

import java.util.List;

public class TfIdf {
  // Method to calculate Term Frequency (TF)
  public static double tf(String[] doc, String term, boolean toLowerCase) {
    double termCount = 0;
    double total = 0;
    for (String word : doc) {
      word = toLowerCase ? word.toLowerCase() : word;
      total++;
      if (word.equals(term)) {
        termCount++;
      }
    }
    return total == 0 ? 0 : termCount / total;
  }

  // Method to calculate Document Frequency (DF)
  public static int df(List<String[]> docs, String term, boolean toLowerCase) {
    int count = 0;
    for (String[] doc : docs) {
      for (String word : doc) {
        word = toLowerCase ? word.toLowerCase() : word;
        if (word.equals(term)) {
          count++;
          break;
        }
      }
    }
    return count;
  }

  // Method to calculate Inverse Document Frequency (IDF)
  public static double idf(List<String[]> docs, String term, boolean toLowerCase) {
    double df = df(docs, term, toLowerCase);
    return (df == 0) ? 0 : Math.log(docs.size() / df);
  }

  // Method to calculate TF-IDF
  public static double tfIdf(String[] doc, List<String[]> docs, String term, boolean toLowerCase) {
    return tf(doc, term, toLowerCase) * idf(docs, term, toLowerCase);
  }
}
