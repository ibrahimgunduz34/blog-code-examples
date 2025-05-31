package org.example.productbatch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@StepScope
public class FileDownloaderTasklet implements Tasklet {
    private final String targetFilename;
    private final String downloadUrl;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String downloadPath;

    public FileDownloaderTasklet(@Value("${acme.catalog.url}") String downloadUrl,
                                 @Value("${project.download_path}") String downloadPath,
                                 @Value("#{jobParameters['TARGET_FILENAME']}") String targetFilename) {
        this.downloadUrl = downloadUrl;
        this.downloadPath = downloadPath;
        this.targetFilename = targetFilename;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Path path = Paths.get(downloadPath, targetFilename).toAbsolutePath();
        Files.createDirectories(path.getParent());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(downloadUrl))
                .header("Accept", "text/csv")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException("Failed to download file");
        }
        Files.writeString(path, response.body());

        return RepeatStatus.FINISHED;

    }
}
