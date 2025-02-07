package com.drmagdiamer.nlpCoreLibrary.util;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.Test;

public class StanfordNlpTest {
  //    @Test
  //    void givenSampleText_whenTokenize_thenExpectedTokensReturned() {
  //        Properties props = new Properties();
  //        props.setProperty("annotators", "tokenize");
  //        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
  //
  //        String text = "The german shepard display an act of kindness";
  //        Annotation document = new Annotation(text);
  //        pipeline.annotate(document);
  //
  //        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
  //        System.out.println("..."+sentences);
  //        StringBuilder tokens = new StringBuilder();
  //
  //        for (CoreMap sentence : sentences) {
  //            System.out.println("..."+sentence);
  //            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
  //                String word = token.get(CoreAnnotations.TextAnnotation.class);
  //                tokens.append(word).append(" ");
  //            }
  //        }
  //        assertEquals("The german shepard display an act of kindness", tokens.toString().trim());
  //    }

  @Test
  void givenSampleText2() {
    // Set up the CoreNLP pipeline with the POS tagging annotator
    Properties props = new Properties();
    props.setProperty(
        "annotators", "tokenize,ssplit,pos"); // Use "tokenize,ssplit,pos" for POS tagging
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

    // The sentence you want to tag
    String text = "Stanford University is located in California.";

    // Create an Annotation object for the text
    Annotation document = new Annotation(text);

    // Run the POS tagging pipeline on the text
    pipeline.annotate(document);

    // Retrieve sentences from the document
    List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

    // Iterate through each sentence and print the word with its POS tag
    for (CoreMap sentence : sentences) {
      for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
        // Get the word
        String word = token.get(CoreAnnotations.TextAnnotation.class);

        // Get the POS tag of the word
        String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

        // Print word and its POS tag
        System.out.println(word + " : " + pos);
      }
    }
  }
}
