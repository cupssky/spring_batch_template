package com.tgkim.batch.demo.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job simpleJob() {
    return jobBuilderFactory.get("simplejob")
        .start(simpleStep(null))
        .next(mainStep(null))
        .build();
  }

  @Bean
  @JobScope
  public Step simpleStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
    return stepBuilderFactory.get("simpleStep")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>> This is simple Step");
          log.info(">>>>> requestDate = {}", requestDate);
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  @Bean
  @JobScope
  public Step mainStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
    return stepBuilderFactory.get("mainStep")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>> This is main Step");
          log.info(">>>>> requestDate = {}", requestDate);
          return RepeatStatus.FINISHED;
        })
        .build();
  }
}
