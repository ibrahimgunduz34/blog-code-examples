package org.example.productbatch.processor;

import org.example.productbatch.model.AcmeProduct;
import org.example.productbatch.model.Product;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class AcmeProductProcessor implements ItemProcessor<AcmeProduct, Product> {
    @Override
    public Product process(AcmeProduct item) throws Exception {
        if (!item.getAvailability().equals("in_stock")) {
            return null; // Skip products that are not in stock
        }

        return new Product(
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getCurrency()
        );
    }
}
