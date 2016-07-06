package demo;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

import demo.address.AddressRepository;
import demo.catalog.CatalogRepository;
import demo.inventory.InventoryRepository;
import demo.product.ProductRepository;
import demo.shipment.ShipmentRepository;
import demo.warehouse.WarehouseRepository;

@Configuration
//@Profile({"docker"})
class GraphConfiguration extends Neo4jConfiguration {

//    @Autowired
//    private Neo4jProperties properties;

    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
       org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
       config
           .driverConfiguration()
           .setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver");
//           .setURI("D:\\temp\\graph.db")

//       	createPermanentFileStore("file:///D:/temp/graph.db");
       return config;
    }
        
//    @Bean
//    public Neo4jServer neo4jServer() {
//        String uri = this.properties.getUri();
//        String pw = this.properties.getPassword();
//        String user = this.properties.getUsername();
//        if (StringUtils.hasText(pw) && StringUtils.hasText(user)) {
//            return new RemoteServer(uri, user, pw);
//        }
//        return new RemoteServer(uri);
//    }

    @Bean
    public SessionFactory getSessionFactory() {
        // we need to specify which packages Neo4j should scan
        // we'll use classes in each package to avoid brittleness
        Class<?>[] packageClasses = {
                ProductRepository.class,
                ShipmentRepository.class,
                WarehouseRepository.class,
                AddressRepository.class,
                CatalogRepository.class,
                InventoryRepository.class
        };
        String[] packageNames =
                Arrays.asList(packageClasses)
                        .stream()
                        .map( c -> getClass().getPackage().getName())
                        .collect(Collectors.toList())
                        .toArray(new String[packageClasses.length]);
        return new SessionFactory(getConfiguration(), packageNames);
    }

    @Bean
    public Session getSession() throws Exception {
        return super.getSession();
    }
}
