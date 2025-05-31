package org.example.productbatch.reader;

import org.example.productbatch.model.AcmeProduct;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
@StepScope
public class AcmeProductsCsvFileReader implements ItemReader<AcmeProduct>, ItemStream {
    private final FlatFileItemReader<AcmeProduct> delegate;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.delegate.open(executionContext);
    }

    public AcmeProductsCsvFileReader(@Value("#{jobParameters['TARGET_FILENAME']}") String targetFilename,
                                     @Value("${project.download_path}") String downloadPath) {
        String fileName = Paths.get(downloadPath, targetFilename).toAbsolutePath().toString();
        this.delegate = createDelegate(fileName);
    }

    @Override
    public AcmeProduct read() throws Exception {
        return delegate.read();
    }

    private FlatFileItemReader<AcmeProduct> createDelegate(String fileName) {
        return new FlatFileItemReaderBuilder<AcmeProduct>()
                .name("acmeProductReader")
                .resource(new FileSystemResource(fileName))
                .encoding("UTF-8")
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .quoteCharacter('"')
                .names("index", "name", "description", "brand", "category",
                        "price", "currency", "stock", "ean", "color", "size",
                        "availability", "internalId")
                .targetType(AcmeProduct.class)
                .strict(false)
                .build();
    }
}
