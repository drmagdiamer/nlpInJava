package com.drmagdiamer.nlpCoreLibrary.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class TfIdfTest {
  @Test
  void tf_calculatesCorrectTermFrequency() {
    String[] doc = {"this", "is", "a", "test", "test"};
    double result = TfIdf.tf(doc, "test", true);
    assertEquals(0.4, result, 0.001);
  }

  @Test
  void tf_handlesEmptyDocument() {
    String[] doc = {};
    double result = TfIdf.tf(doc, "test", true);
    assertEquals(0.0, result, 0.001);
  }

  @Test
  void df_calculatesCorrectDocumentFrequency() {
    List<String[]> docs =
        List.of(
            new String[] {"this", "is", "a", "test"},
            new String[] {"this", "is", "another", "test"});
    int result = TfIdf.df(docs, "test", true);
    assertEquals(2, result);
  }

  @Test
  void df_handlesNoOccurrences() {
    List<String[]> docs =
        List.of(
            new String[] {"this", "is", "a", "test"},
            new String[] {"this", "is", "another", "example"});
    int result = TfIdf.df(docs, "word", true);
    assertEquals(0, result);
  }

  @Test
  void idf_calculatesCorrectInverseDocumentFrequency() {
    List<String[]> docs =
        List.of(
            new String[] {"this", "is", "a", "test"},
            new String[] {"this", "is", "another", "test"});
    double result = TfIdf.idf(docs, "test", true);
    assertEquals(Math.log(2.0 / 2), result, 0.001);
  }

  @Test
  void tfIdf_calculatesCorrectTfIdf() {
    String[] doc = {"this", "is", "a", "test"};
    List<String[]> docs =
        List.of(
            new String[] {"this", "is", "a", "test"},
            new String[] {"this", "is", "another", "test"});
    double result = TfIdf.tfIdf(doc, docs, "test", true);
    assertEquals(0.4 * Math.log(2.0 / 2), result, 0.001);
  }

  @Test
  void tfIdf_handlesCaseSensitivity() {
    String[] doc = {"This", "is", "a", "Test", "test"};
    List<String[]> docs =
        List.of(
            new String[] {"this", "is", "a", "test"},
            new String[] {"this", "is", "another", "test"});
    double result = TfIdf.tfIdf(doc, docs, "test", false);
    assertEquals(0.4 * Math.log(2.0 / 2), result, 0.001);
  }
}
