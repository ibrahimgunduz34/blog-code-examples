package org.example.productbatch.config;

import org.example.productbatch.model.AcmeProduct;
import org.example.productbatch.model.Product;
import org.example.productbatch.processor.AcmeProductProcessor;
import org.example.productbatch.reader.AcmeProductsCsvFileReader;
import org.example.productbatch.tasklet.FileDownloaderTasklet;
import org.example.productbatch.writer.AcmeProductJpaWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class AcmeProductsJobConfig {
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    public AcmeProductsJobConfig(@Qualifier("transactionManager") PlatformTransactionManager transactionManager,
                                 JobRepository jobRepository) {
        this.transactionManager = transactionManager;
        this.jobRepository = jobRepository;
    }

    @Bean(name = "acmeProductsJob")
    public Job acmeProductsJob(@Qualifier("acmeProductsDownloadStep") Step acmeProductsStep,
                               @Qualifier("acmeProductsProcessStep") Step acmeProductsProcessStep) {
        return new JobBuilder("acmeProducts", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(acmeProductsStep)
                .next(acmeProductsProcessStep)
                .build();
    }

    @Bean(name = "acmeProductsDownloadStep")
    public Step acmeProductsStep(FileDownloaderTasklet fileDownloaderTasklet) {
        return new StepBuilder("acmeProductsDownloadStep", jobRepository)
                .tasklet(fileDownloaderTasklet, transactionManager)
                .build();
    }

    @Bean(name = "acmeProductsProcessStep")
    public Step acmeProductsProcessStep(AcmeProductsCsvFileReader csvReader,
                                        AcmeProductProcessor processor,
                                        AcmeProductJpaWriter jpaWriter) {
        return new StepBuilder("acmeProductsProcessStep", jobRepository)
                .<AcmeProduct, Product>chunk(10, transactionManager)
                .reader(csvReader)
                .processor(processor)
                .writer(jpaWriter)
                .build();
    }
}
