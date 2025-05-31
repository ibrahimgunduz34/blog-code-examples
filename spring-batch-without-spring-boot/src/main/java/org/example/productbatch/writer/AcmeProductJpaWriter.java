package org.example.productbatch.writer;

import org.example.productbatch.model.Product;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class AcmeProductJpaWriter implements ItemWriter<Product> {
    private final JdbcBatchItemWriter<Product> delegate;

    public AcmeProductJpaWriter(@Qualifier("domainDataSource") DataSource dataSource) {
        this.delegate = new JdbcBatchItemWriterBuilder<Product>()
                .dataSource(dataSource)
                .sql("""
                INSERT INTO catalog_products (name, description, price, currency)
                VALUES (:name, :description, :price, :currency)
                """)
                .beanMapped()
                .build();

        try {
            this.delegate.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException("JdbcBatchItemWriter init failed", e);
        }
    }

    @Override
    public void write(Chunk<? extends Product> chunk) throws Exception {
        delegate.write(chunk);
    }
}
