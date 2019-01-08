package com.tgkim.batch.demo.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextConditionalJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job stepNextConditionalJob() {
    return jobBuilderFactory.get("stepNextConditionalJob")
        .start(conditionalJobStep1())
        .on("FAILED")
        .to(conditionalJobStep3())
        .on("*")
        .end()
        .from(conditionalJobStep1())
        .on("*")
        .to(conditionalJobStep2())
        .next(conditionalJobStep3())
        .on("*")
        .end()
        .end().build();
  }

  @Bean
  public Step conditionalJobStep1() {
    return stepBuilderFactory.get("conditionalJobStep1")
        .tasklet((stepContribution, chunkContext) -> {
          log.info(">>>>> This is stepNextConditionalJob Step1");
          stepContribution.setExitStatus(ExitStatus.FAILED);
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  @Bean
  public Step conditionalJobStep2() {
    return stepBuilderFactory.get("conditionalJobStep2")
        .tasklet((stepContribution, chunkContext) -> {
          log.info(">>>>> This is stepNextConditionalJob Step2");
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  @Bean
  public Step conditionalJobStep3() {
    return stepBuilderFactory.get("conditionalJobStep3")
        .tasklet((stepContribution, chunkContext) -> {
          log.info(">>>>> This is stepNextConditionalJob Step3");
          return RepeatStatus.FINISHED;
        })
        .build();
  }

}
