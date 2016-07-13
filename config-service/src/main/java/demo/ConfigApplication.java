package demo;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigServer
public class ConfigApplication implements CommandLineRunner {

	@Autowired
	HttpProxy httpProxy;

	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}

	@Override
	public void run(String... arg0) throws Exception {

		if (httpProxy != null && httpProxy.getHost() != null){

			Authenticator.setDefault(new Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        if (getRequestorType() == RequestorType.PROXY) {
			            String user = "xxxx"; //System.getProperty("http.proxyUser");
			            String password = "yyR"; //System.getProperty("http.proxyPassword");
	                    return new PasswordAuthentication(user, password.toCharArray());
			        }
			        return null;
			    }
			});
			
			// Enable config server to reach repository via proxy server bug
			// #146
			// https://github.com/spring-cloud/spring-cloud-config/issues/146
			ProxySelector.setDefault(new ProxySelector() {
				final ProxySelector delegate = ProxySelector.getDefault();

				@Override
				public List<Proxy> select(URI uri) {
					// Filter the URIs to be proxied
					if (uri.toString().contains("github") && uri.toString().contains("https")) {
						return Arrays.asList(new Proxy(Type.HTTP,
								InetSocketAddress.createUnresolved(httpProxy.getHost(), httpProxy.getPort())));
					}
					if (uri.toString().contains("github") && uri.toString().contains("http")) {
						return Arrays.asList(new Proxy(Type.HTTP,
								InetSocketAddress.createUnresolved(httpProxy.getHost(), httpProxy.getPort())));
					}
					// revert to the default behaviour
					return delegate == null ? Arrays.asList(Proxy.NO_PROXY) : delegate.select(uri);
				}

				@Override
				public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
					if (uri == null || sa == null || ioe == null) {
						throw new IllegalArgumentException("Arguments can't be null.");
					}
				}
			});
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

}
