package demo.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The Mongo configuration class for this Spring Data MongoDB application.
 *
 * @author Kenny Bastani
 * @author Josh Long
 */
@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private Integer port;
    
    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;
    
    @Value("${spring.data.mongodb.db}")
    private String db;
    
    @Bean
    public Mongo mongo() throws Exception {
    	ServerAddress sa = new ServerAddress(host, port);
    	MongoCredential mongoCredential =  MongoCredential.createMongoCRCredential(username, db, ((password!=null)?password.toCharArray():null));
    	return new MongoClient(sa, Arrays.asList(mongoCredential));
    }

    @Override
    public String getDatabaseName() {
        return "orders";
    }

    @Override
    public String getMappingBasePackage() {
        return "demo";
    }

    @Bean
    @Override
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        converterList.add(new LongToDateTimeConverter());
        converterList.add(new StringToDateTimeConverter());
        return new CustomConversions(converterList);
    }

    @ReadingConverter
    static class LongToDateTimeConverter implements Converter<Long, Date> {

        @Override
        public Date convert(Long source) {
            if (source == null) {
                return null;
            }

            return new Date(source);
        }
    }

    @ReadingConverter
    static class StringToDateTimeConverter implements Converter<String, Date> {

        @Override
        public Date convert(String source) {
            if (source == null) {
                return null;
            }

            return new Date(source);
        }
    }
}
