package com.drmagdiamer.nlpCoreLibrary.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class NlpUtilTest {
  @Test
  void parseSentence_splitsSentenceIntoWordsWithLowerCase() {
    String sentence = "This is a Test";
    List<String> result = NlpUtil.parseSentence(sentence, true);
    assertEquals(List.of("this", "is", "a", "test"), result);
  }

  @Test
  void parseSentence_splitsSentenceIntoWordsWithoutLowerCase() {
    String sentence = "This is a Test";
    List<String> result = NlpUtil.parseSentence(sentence, false);
    assertEquals(List.of("This", "is", "a", "Test"), result);
  }

  @Test
  void parseSentence_handlesEmptyStringWithLowerCase() {
    String sentence = "";
    List<String> result = NlpUtil.parseSentence(sentence, true);
    assertEquals(List.of(), result);
  }

  @Test
  void parseSentence_handlesEmptyStringWithoutLowerCase() {
    String sentence = "";
    List<String> result = NlpUtil.parseSentence(sentence, false);
    assertEquals(List.of(), result);
  }

  @Test
  void parseSentence_handlesMultipleSpacesWithLowerCase() {
    String sentence = "This  is   a    Test";
    List<String> result = NlpUtil.parseSentence(sentence, true);
    assertEquals(List.of("this", "is", "a", "test"), result);
  }

  @Test
  void parseSentence_handlesMultipleSpacesWithoutLowerCase() {
    String sentence = "This  is   a    Test";
    List<String> result = NlpUtil.parseSentence(sentence, false);
    assertEquals(List.of("This", "is", "a", "Test"), result);
  }

  @Test
  void parseSentence_handlesLeadingAndTrailingSpacesWithLowerCase() {
    String sentence = "  This  is  a  Test  ";
    List<String> result = NlpUtil.parseSentence(sentence, true);
    assertEquals(List.of("this", "is", "a", "test"), result);
    System.out.println(result);
  }

  @Test
  void parseSentence_handlesLeadingAndTrailingSpacesWithoutLowerCase() {
    String sentence = "  This  is  a  Test  ";
    List<String> result = NlpUtil.parseSentence(sentence, false);
    assertEquals(List.of("This", "is", "a", "Test"), result);
    System.out.println(result);
  }

  @Test
  void parseSentence_handlesLeadingAndTrailingSpacesWithLowerCaseWithPunctuation() {
    String sentence = "  This  is  a  Test.This is a different test,while this is a third test  ";
    List<String> result = NlpUtil.parseSentence(sentence, true);
    assertEquals(
        List.of(
            "this",
            "is",
            "a",
            "test",
            "this",
            "is",
            "a",
            "different",
            "test",
            "while",
            "this",
            "is",
            "a",
            "third",
            "test"),
        result);
    System.out.println(result);
  }

  @Test
  void oneHotEncode_encodesSingleCategory() {
    List<String> categories = List.of("test");
    List<Integer[]> result = NlpUtil.oneHotEncode(categories);
    assertEquals(1, result.size());
    assertArrayEquals(new Integer[] {1}, result.get(0));
  }

  /////////////////////

  @Test
  void oneHotEncode_handlesLeadingAndTrailingSpacesWithLowerCase() {
    String sentence = "  This  is  a  Test  ";
    List<Integer[]> result = NlpUtil.oneHotEncode(sentence, true);
    assertEquals(4, result.size());
    assertArrayEquals(new Integer[] {0, 0, 0, 1}, result.get(0));
    assertArrayEquals(new Integer[] {0, 1, 0, 0}, result.get(1));
    assertArrayEquals(new Integer[] {1, 0, 0, 0}, result.get(2));
    assertArrayEquals(new Integer[] {0, 0, 1, 0}, result.get(3));
    result.forEach(arr -> System.out.println(Arrays.toString(arr)));
  }

  @Test
  void oneHotEncode_encodesMultipleCategoriesInSentence() {
    String sentence = "  This  is   a  test  ";
    List<Integer[]> result = NlpUtil.oneHotEncode(sentence, true);
    assertEquals(4, result.size());
    assertArrayEquals(new Integer[] {0, 0, 0, 1}, result.get(0));
    assertArrayEquals(new Integer[] {0, 1, 0, 0}, result.get(1));
    assertArrayEquals(new Integer[] {1, 0, 0, 0}, result.get(2));
    assertArrayEquals(new Integer[] {0, 0, 1, 0}, result.get(3));
    result.forEach(arr -> System.out.println(Arrays.toString(arr)));
  }

  @Test
  void oneHotEncode_encodesMultipleCategories() {
    List<String> categories = List.of("this", "is", "a", "test");
    List<Integer[]> result = NlpUtil.oneHotEncode(categories);
    assertEquals(4, result.size());
    assertArrayEquals(new Integer[] {0, 0, 0, 1}, result.get(0));
    assertArrayEquals(new Integer[] {0, 1, 0, 0}, result.get(1));
    assertArrayEquals(new Integer[] {1, 0, 0, 0}, result.get(2));
    assertArrayEquals(new Integer[] {0, 0, 1, 0}, result.get(3));
    result.forEach(arr -> System.out.println(Arrays.toString(arr)));
  }

  @Test
  void oneHotEncode_handlesEmptyList() {
    List<String> categories = List.of();
    List<Integer[]> result = NlpUtil.oneHotEncode(categories);
    assertEquals(0, result.size());
  }

  @Test
  void oneHotEncode_handlesRepeatedCategories() {
    List<String> categories = List.of("test", "test", "test");
    List<Integer[]> result = NlpUtil.oneHotEncode(categories);
    assertEquals(3, result.size());
    assertArrayEquals(new Integer[] {1}, result.get(0));
    assertArrayEquals(new Integer[] {1}, result.get(1));
    assertArrayEquals(new Integer[] {1}, result.get(2));
  }

  @Test
  void oneHotEncode_handlesUnsortedCategories() {
    List<String> categories = List.of("test", "a", "is", "this");
    List<Integer[]> result = NlpUtil.oneHotEncode(categories);
    assertEquals(4, result.size());
    assertArrayEquals(new Integer[] {0, 0, 1, 0}, result.get(0));
    assertArrayEquals(new Integer[] {1, 0, 0, 0}, result.get(1));
    assertArrayEquals(new Integer[] {0, 1, 0, 0}, result.get(2));
    assertArrayEquals(new Integer[] {0, 0, 0, 1}, result.get(3));
  }

  /////////////////////
  @Test
  void buildBagOfWords_createsCorrectMatrixForSingleDocument() {
    List<String> documents = List.of("this is a test");
    Integer[][] result = NlpUtil.buildBagOfWords(documents, true);
    assertEquals(1, result.length);
    assertArrayEquals(new Integer[] {1, 1, 1, 1}, result[0]);
  }

  @Test
  void buildBagOfWords_createsCorrectMatrixForMultipleDocuments() {
    List<String> documents = List.of("this is a test", "this is another test");
    Integer[][] result = NlpUtil.buildBagOfWords(documents, true);
    assertEquals(2, result.length);
    assertArrayEquals(new Integer[] {1, 0, 1, 1, 1}, result[0]);
    assertArrayEquals(new Integer[] {0, 1, 1, 1, 1}, result[1]);
  }

  @Test
  void buildBagOfWords_handlesEmptyDocumentList() {
    List<String> documents = List.of();
    Integer[][] result = NlpUtil.buildBagOfWords(documents, true);
    assertEquals(0, result.length);
  }

  @Test
  void buildBagOfWords_handlesEmptyDocuments() {
    List<String> documents = List.of("", "");
    Integer[][] result = NlpUtil.buildBagOfWords(documents, true);
    assertEquals(2, result.length);
    assertArrayEquals(new Integer[] {}, result[0]);
    assertArrayEquals(new Integer[] {}, result[1]);
  }

  @Test
  void buildBagOfWords_handlesCaseSensitivity() {
    List<String> documents = List.of("This is a Test", "this is a test");
    Integer[][] result = NlpUtil.buildBagOfWords(documents, false);
    assertEquals(2, result.length);
    // remember that Capital is before any small letter in ASCII
    assertArrayEquals(new Integer[] {1, 1, 1, 1, 0, 0}, result[0]);
    assertArrayEquals(new Integer[] {0, 0, 1, 1, 1, 1}, result[1]);
    Arrays.stream(result).map(Arrays::toString).forEach(System.out::println);
  }

  @Test
  void buildBagOfWords_handlesMultipleSpaces() {
    List<String> documents = List.of("this  is   a    test");
    Integer[][] result = NlpUtil.buildBagOfWords(documents, true);
    assertEquals(1, result.length);
    assertArrayEquals(new Integer[] {1, 1, 1, 1}, result[0]);
  }

  @Test
  void buildBagOfWords_handlesLeadingAndTrailingSpaces() {
    List<String> documents = List.of("  this is a test  ");
    Integer[][] result = NlpUtil.buildBagOfWords(documents, true);
    assertEquals(1, result.length);
    assertArrayEquals(new Integer[] {1, 1, 1, 1}, result[0]);
  }

  @Test
  void buildBagOfWords_handlesRepeatedWords() {
    List<String> documents =
        Arrays.asList("The cat sat on the mat", "The dog sat on the log", "The cat chased the dog");
    Integer[][] result = NlpUtil.buildBagOfWords(documents, true);
    assertEquals(3, result.length);
    assertArrayEquals(new Integer[] {1, 0, 0, 0, 1, 1, 1, 2}, result[0]);
    assertArrayEquals(new Integer[] {0, 0, 1, 1, 0, 1, 1, 2}, result[1]);
    assertArrayEquals(new Integer[] {1, 1, 1, 0, 0, 0, 0, 2}, result[2]);

    Arrays.stream(result).map(Arrays::toString).forEach(System.out::println);
  }

  ////////////////////////
  @Test
  void tfIdf_calculatesCorrectTfIdfForSingleDocument() {
    String[] doc = {"this", "is", "a", "test", "test"};
    List<String[]> docs =
        List.of(
            new String[] {"this", "is", "a", "test"},
            new String[] {"this", "is", "another", "test"});
    Double result = NlpUtil.tfIdf(doc, docs, "test", true);
    assertEquals(0.4 * Math.log(2.0 / 2), result, 0.001);
  }

  @Test
  void tfIdf_handlesEmptyDocument() {
    String[] doc = {};
    List<String[]> docs =
        List.of(
            new String[] {"this", "is", "a", "test"},
            new String[] {"this", "is", "another", "test"});
    Double result = NlpUtil.tfIdf(doc, docs, "test", true);
    assertEquals(0.0, result, 0.001);
  }

  @Test
  void tfIdf_handlesNoOccurrences() {
    String[] doc = {"this", "is", "a", "test"};
    List<String[]> docs =
        List.of(
            new String[] {"this", "is", "a", "test"},
            new String[] {"this", "is", "another", "example"});
    Double result = NlpUtil.tfIdf(doc, docs, "word", true);
    assertEquals(0.0, result, 0.001);
  }

  @Test
  void tfIdf_handlesCaseSensitivity() {
    String[] doc = {"This", "is", "a", "Test", "test"};
    List<String[]> docs =
        List.of(
            new String[] {"this", "is", "a", "test"},
            new String[] {"this", "is", "another", "test"});
    Double result = NlpUtil.tfIdf(doc, docs, "test", false);
    assertEquals(0.4 * Math.log(2.0 / 2), result, 0.001);
  }

  @Test
  void tfIdf_calculatesCorrectTfIdfForMultipleDocuments() {
    String[] doc = {"this", "is", "a", "test", "test"};
    List<String[]> docs =
        List.of(
            new String[] {"this", "is", "a", "test"},
            new String[] {"this", "is", "another", "test"},
            new String[] {"this", "is", "yet", "another", "test"});
    Double result = NlpUtil.tfIdf(doc, docs, "test", true);
    assertEquals(0.4 * Math.log(3.0 / 3), result, 0.001);
  }

  /////////////////////////
  @Test
  void buildTfIdf_createsCorrectMatrixForSingleDocument() {
    List<String> documents = List.of("this is a test");
    Double[][] result = NlpUtil.buildTfIdf(documents, true);
    assertEquals(1, result.length);
    System.out.println(Arrays.toString(result[0]));
    //    assertArrayEquals(new Double[] {0.0, 0.0, 0.0, 0.0}, result[0]);
  }

  @Test
  void buildTfIdf_createsCorrectMatrixForMultipleDocuments() {
    List<String> documents = List.of("this is a test", "this is another test");
    Double[][] result = NlpUtil.buildTfIdf(documents, true);
    assertEquals(2, result.length);
    Arrays.stream(result).map(Arrays::toString).forEach(System.out::println);
    //    assertArrayEquals(new Double[] {0.0, 0.0, 0.0, 0.0, 0.0}, result[0]);
    //    assertArrayEquals(new Double[] {0.0, 0.0, 0.0, 0.0, 0.0}, result[1]);
  }

  @Test
  void buildTfIdf_handlesEmptyDocumentList() {
    List<String> documents = List.of();
    Double[][] result = NlpUtil.buildTfIdf(documents, true);
    assertEquals(0, result.length);
  }

  @Test
  void buildTfIdf_handlesEmptyDocuments() {
    List<String> documents = List.of("", "");
    Double[][] result = NlpUtil.buildTfIdf(documents, true);
    assertEquals(2, result.length);
    assertArrayEquals(new Double[] {}, result[0]);
    assertArrayEquals(new Double[] {}, result[1]);
  }

  @Test
  void buildTfIdf_handlesCaseSensitivity() {
    List<String> documents = List.of("This is a Test", "this is a test");
    Double[][] result = NlpUtil.buildTfIdf(documents, false);
    assertEquals(2, result.length);
    Arrays.stream(result).map(Arrays::toString).forEach(System.out::println);
    //    assertArrayEquals(new Double[] {0.0, 0.0, 0.0, 0.0}, result[0]);
    //    assertArrayEquals(new Double[] {0.0, 0.0, 0.0, 0.0}, result[1]);
  }

  @Test
  void buildTfIdf_handlesMultipleSpaces() {
    List<String> documents = List.of("this  is   a    test");
    Double[][] result = NlpUtil.buildTfIdf(documents, true);
    assertEquals(1, result.length);
    Arrays.stream(result).map(Arrays::toString).forEach(System.out::println);
    //    assertArrayEquals(new Double[] {0.0, 0.0, 0.0, 0.0}, result[0]);
  }

  @Test
  void buildTfIdf_handlesLeadingAndTrailingSpaces() {
    List<String> documents = List.of("  this is a test  ");
    Double[][] result = NlpUtil.buildTfIdf(documents, true);
    assertEquals(1, result.length);
    Arrays.stream(result).map(Arrays::toString).forEach(System.out::println);
    //    assertArrayEquals(new Double[] {0.0, 0.0, 0.0, 0.0}, result[0]);
  }
}
