package com.drmagdiamer.nlpCoreLibrary.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Collection;
import java.util.Objects;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.junit.jupiter.api.Test;
import org.nd4j.common.io.ClassPathResource;
import reactor.core.publisher.Mono;

class Word2VecHelperTest {
  @Test
  void testWord2VecHelper() {
    Mono<Word2Vec> word2VecMono = Word2VecHelper.loadModel();
    assertNotNull(word2VecMono);
    word2VecMono
        .map(
            word2Vec -> {
              assertNotNull(word2Vec);
              // Example usage: Find words similar to 'NLP'
              Collection<String> similarWords = word2Vec.wordsNearest("nlp", 10);
              System.out.println("Words similar to 'NLP': " + similarWords);

              // Example usage: Get the vector for the word 'NLP'
              double[] wordVector = word2Vec.getWordVector("nlp");
              System.out.println("Vector for 'NLP': ");
              if (Objects.isNull(wordVector)) {
                System.out.println("Word 'NLP' not found in the model");
              } else {
                for (double v : wordVector) {
                  System.out.print(v + " ");
                }
              }
              return word2Vec;
            })
        .block();
  }

  @Test
  void testWord2VecHelperPreTrained() {
    Mono<Word2Vec> word2VecMono =
        Word2VecHelper.loadModel("data\\GoogleNews-vectors-negative300.bin");
    assertNotNull(word2VecMono);
    word2VecMono
        .map(
            word2Vec -> {
              assertNotNull(word2Vec);
              // Example usage: Find words similar to 'NLP'
              Collection<String> similarWords = word2Vec.wordsNearest("school", 10);
              System.out.println("Words similar to 'school': " + similarWords);

              // Example usage: Get the vector for the word 'NLP'
              double[] wordVector = word2Vec.getWordVector("school");
              System.out.println("Vector for 'school': ");
              if (Objects.isNull(wordVector)) {
                System.out.println("Word 'school' not found in the model");
              } else {
                for (double v : wordVector) {
                  System.out.print(v + " ");
                }
              }
              return word2Vec;
            })
        .block();
  }

  //

  //    public static void main(String[] args) throws Exception {
  //        // Load the input file (corpus)
  //        ClassPathResource resource= new ClassPathResource("text_corpus.txt");
  //
  //        String result = "";
  //        try (BufferedReader reader = new BufferedReader(new
  // InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
  //            result =  reader.lines().collect(Collectors.joining("\n"));
  //        }
  //
  //        System.out.println(result);
  //        File file = resource.getFile();
  //
  //        // Load sentences from the corpus file
  //        LineSentenceIterator sentenceIterator = new LineSentenceIterator(file);
  //
  //        // Tokenizer to split sentences into words
  //        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
  //        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
  //
  //        // Build and train the Word2Vec model
  //        Word2Vec word2Vec = new Word2Vec.Builder()
  //                .minWordFrequency(2)       // Minimum frequency for words to be included
  //                .iterations(50)            // Number of training iterations
  //                .layerSize(100)            // Size of the word vectors
  //                .seed(42)                  // Random seed for reproducibility
  //                .windowSize(5)             // Context window size for skip-gram model
  //                .iterate(sentenceIterator) // Sentences for training
  //                .tokenizerFactory(tokenizerFactory) // Tokenizer for text processing
  //                .build();
  //
  //        System.out.println("Training Word2Vec model...");
  //        word2Vec.fit(); // Train the model
  //
  //        // Save the Word2Vec model
  //        WordVectorSerializer.writeWord2VecModel(word2Vec, "word2vecModel.txt");
  //
  //        // Example usage: Find words similar to 'NLP'
  //        Collection<String> similarWords = word2Vec.wordsNearest("NLP", 10);
  //        System.out.println("Words similar to 'NLP': " + similarWords);
  //
  //        // Example usage: Get the vector for the word 'NLP'
  //        double[] wordVector = word2Vec.getWordVector("NLP");
  //        if(Objects.isNull(wordVector)) {
  //            System.out.println("Word 'NLP' not found in the model");
  //        } else {
  //            for (double v : wordVector) {
  //                System.out.print(v + " ");
  //            }
  //        }
  //    }

  @Test
  public void test1() {
    File file = null;
    // Load the input file (corpus)
    try {
      file = new ClassPathResource("text_corpus.txt").getFile();
    } catch (Exception e) {
      System.err.println("Error loading corpus file: " + e.getMessage());
      return;
    }

    // Check if the file exists and has content
    if (!file.exists() || file.length() == 0) {
      System.err.println("Corpus file is missing or empty. Please provide a valid corpus.");
      return;
    }

    // Load sentences from the corpus file
    LineSentenceIterator sentenceIterator = new LineSentenceIterator(file);

    // Tokenizer to split sentences into words (with lowercase normalization)
    TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
    // Apply a custom preprocessor to lowercase everything and preserve words like "NLP"
    tokenizerFactory.setTokenPreProcessor(token -> token.toLowerCase());

    // Build and train the Word2Vec model
    Word2Vec word2Vec =
        new Word2Vec.Builder()
            .minWordFrequency(1) // Minimum frequency for words to be included
            .iterations(5) // Number of training iterations
            .layerSize(100) // Size of the word vectors
            .seed(42) // Random seed for reproducibility
            .windowSize(5) // Context window size for skip-gram model
            .iterate(sentenceIterator) // Sentences for training
            .tokenizerFactory(tokenizerFactory) // Tokenizer for text processing
            .build();

    System.out.println("Training Word2Vec model...");
    word2Vec.fit(); // Train the model

    // Convert target word to lowercase since our preprocessor lowercased all words
    String targetWord = "nlp"; // Lowercase version to match tokenization

    if (word2Vec.hasWord(targetWord)) {
      Collection<String> similarWords = word2Vec.wordsNearest(targetWord, 10);
      System.out.println("Words similar to '" + targetWord + "': " + similarWords);
    } else {
      System.out.println("The word '" + targetWord + "' is not in the vocabulary.");
    }

    // Example usage: Get the vector for the word 'NLP'
    double[] wordVector = word2Vec.getWordVector(targetWord);
    if (wordVector != null) {
      System.out.println("Vector for '" + targetWord + "': ");
      for (double v : wordVector) {
        System.out.print(v + " ");
      }
      System.out.println(); // for a new line
    } else {
      System.out.println("Word vector for '" + targetWord + "' is not available.");
    }

    // Save the Word2Vec model for future use
    WordVectorSerializer.writeWord2VecModel(word2Vec, "word2vecModel-TEST.bin");
  }
}
