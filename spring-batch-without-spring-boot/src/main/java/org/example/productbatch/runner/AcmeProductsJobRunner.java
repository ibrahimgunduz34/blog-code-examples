package org.example.productbatch.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AcmeProductsJobRunner {
    private final JobLauncher jobLauncher;
    private final Job acmeProductsJob;

    public AcmeProductsJobRunner(JobLauncher jobLauncher,
                                 @Qualifier("acmeProductsJob") Job acmeProductsJob) {
        this.jobLauncher = jobLauncher;
        this.acmeProductsJob = acmeProductsJob;
    }

    @Scheduled(cron = "${acme.catalog.cron}")
    public void run() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String targetFilename = String.format("acme-products-%d.csv", System.currentTimeMillis());
        JobParameters parameters = new JobParametersBuilder()
                .addString("TARGET_FILENAME", targetFilename)
                .toJobParameters();
        jobLauncher.run(acmeProductsJob, parameters);
    }
}
