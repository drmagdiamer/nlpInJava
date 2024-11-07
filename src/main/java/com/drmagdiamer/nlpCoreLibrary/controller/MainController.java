package com.drmagdiamer.nlpCoreLibrary.controller;

import com.drmagdiamer.nlpCoreLibrary.util.NlpUtil;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/nlp")
public class MainController {
  @PostMapping("/oneHotEncoding")
  public List<Integer[]> processPerson(@RequestBody(required = true) String text) {
    log.info("Processing using one Hot Encoding {} ", text);

    return NlpUtil.oneHotEncode(text, true);
  }
}
