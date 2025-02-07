package com.drmagdiamer.nlpCoreLibrary.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

// Training a Neural Network is a time-consuming process.
// It must be done offline.
// The resulting Neural Network can be saved to a file and loaded in online interactions
public class Word2VecHelperTrain {
  public static final String DEFAULT_SOURCE_NAME = "data\\text_corpus.txt";
  public static final String DEFAULT_TARGET_NAME = "data\\word2vecModel.bin";
  private static final String PUNCTUATION = "[.,!?]";
  // Define a set of stop words
  private static final Set<String> STOP_WORDS =
      new HashSet<>(
          Arrays.asList(
              "a", "an", "the", "in", "on", "and", "or", "but", "with", "for", "to", "of"
              // Add more stop words as needed
              ));

  public static void main(String[] args) {
    String textCorpusFile =
        (args == null || args.length < 1 || args[0] == null) ? DEFAULT_SOURCE_NAME : args[0];
    String targetModelFile =
        (args == null || args.length < 2 || args[1] == null) ? DEFAULT_TARGET_NAME : args[1];
    // Load the input file (corpus)
    File file = null;
    try {
      file = new File(textCorpusFile);
    } catch (Exception e) {
      System.err.println("Error loading file: " + e.getMessage());
      System.exit(1);
    }
    // Check if the file exists and has content
    if (!file.exists() || file.length() == 0) {
      System.err.println("Corpus file is missing or empty. Please provide a valid corpus.");
      System.exit(1);
    }

    // Load sentences from the corpus file
    LineSentenceIterator sentenceIterator = new LineSentenceIterator(file);

    // Tokenizer to split sentences into words (with lowercase normalization)
    TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
    // Apply a custom preprocessor to lowercase everything and preserve words like "NLP"
    tokenizerFactory.setTokenPreProcessor(
        token -> {
          String lowercasedToken = token.toLowerCase();
          lowercasedToken = lowercasedToken.replaceAll(PUNCTUATION, "");
          // Remove the token if it's a stop word
          return STOP_WORDS.contains(lowercasedToken) ? null : lowercasedToken;
        });

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

    // Train the Word2Vec model
    System.out.println("Training Word2Vec model...");
    word2Vec.fit(); // Time-consuming task

    // Save the trained model
    WordVectorSerializer.writeWord2VecModel(word2Vec, targetModelFile);
    System.out.println("Word2Vec model saved to: " + targetModelFile);
  }
}
