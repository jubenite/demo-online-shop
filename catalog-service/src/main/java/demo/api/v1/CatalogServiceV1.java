package demo.api.v1;


import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import demo.catalog.Catalog;
import demo.catalog.CatalogInfo;
import demo.catalog.CatalogInfoRepository;
import demo.product.Product;

@Service
public class CatalogServiceV1 {
    private CatalogInfoRepository catalogInfoRepository;
    private RestTemplate restTemplate;
    private final Logger log = LoggerFactory.getLogger(CatalogServiceV1.class);
    

    @Autowired
    public CatalogServiceV1(CatalogInfoRepository catalogInfoRepository,
                            @LoadBalanced RestTemplate restTemplate) {
        this.catalogInfoRepository = catalogInfoRepository;
        this.restTemplate = restTemplate;
    }

    @HystrixCommand
    public Catalog getCatalog() {
        Catalog catalog;

        log.info("getting active catalogs in business service");
        
        CatalogInfo activeCatalog = catalogInfoRepository.findCatalogByActive(true);

        catalog = restTemplate.getForObject(String.format("http://inventory-service/api/catalogs/search/findCatalogByCatalogNumber?catalogNumber=%s",
                activeCatalog.getCatalogId()), Catalog.class);

        ProductsResource products = restTemplate.getForObject(String.format("http://inventory-service/api/catalogs/%s/products",
                catalog.getId()), ProductsResource.class);

        catalog.setProducts(products.getContent().stream().collect(Collectors.toSet()));
        return catalog;
    }

    @HystrixCommand
    public Product getProduct(String productId) {    	
    	log.info("getting active products in business service");    	
        return restTemplate.getForObject(String.format("http://inventory-service/v1/products/%s",
                productId), Product.class);
    }
}
