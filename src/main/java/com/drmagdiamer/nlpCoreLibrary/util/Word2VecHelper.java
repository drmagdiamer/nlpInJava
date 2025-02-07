package com.drmagdiamer.nlpCoreLibrary.util;

import java.io.File;
import java.io.IOException;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Log4j2
public class Word2VecHelper {

  private Word2VecHelper() {}

  private static Map<String, Word2Vec> data = new HashMap<>();

  public static File loadFileFromResource(String filePath) throws IOException {
    return new ClassPathResource(filePath).getFile();
  }

  public static Mono<Word2Vec> loadModel() {
    return loadModel(Word2VecHelperTrain.DEFAULT_TARGET_NAME);
  }

  public static Mono<Word2Vec> loadModel(String targetModelFile) {
    if (data.containsKey(targetModelFile)) {
      return Mono.just(data.get(targetModelFile));
    } else {
      return loadModelFromFile(targetModelFile).doOnNext(model -> data.put(targetModelFile, model));
    }
  }

  // Asynchronous model loading trained Neural Network
  public static synchronized Mono<Word2Vec> loadModelFromFile(String targetModelFile) {
    if (data.containsKey(targetModelFile)) {
      return Mono.just(data.get(targetModelFile));
    }
    return Mono.fromCallable(
            () -> {
              System.out.println("Starting loadModel ...");
              // Check if model already exists to avoid retraining
              File modelFile = new File(targetModelFile);
              if (modelFile.exists()) {
                System.out.println("Loading existing Word2Vec model...");
                log.info("Loading existing Word2Vec model...");
                return WordVectorSerializer.readWord2VecModel(modelFile);
              } else {
                System.out.println("Model File Missing");
                log.error("Model File Missing");
                return null;
              }
            })
        .subscribeOn(Schedulers.boundedElastic()) // Run training on a separate thread pool
        .onErrorResume(
            e -> {
              System.err.println("Error during Word2Vec loadModel: " + e.getMessage());
              e.printStackTrace();
              return Mono.empty();
            });
  }

  public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
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
