package dev.mkk7.blog.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;



@Configuration
public class ElasticSearchConfig implements IElasticSearch{
	
	@Bean
	public ElasticsearchClient client() throws Exception {
		
		final CredentialsProvider credentialsProvider =
			    new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY,
			    new UsernamePasswordCredentials("elastic", "@fpdlvnf"));
			
			/*
			 * Header[] defaultHeaders = new Header[]{new BasicHeader("Authorization",
			 * "Bearer 45be2fa372db2b3f1c88e1e56050427cfee57ad572f522d4d56891346eac9ce6")};
			 */
			
		Path caCertificatePath = Paths.get("C:\\elastic_ssl\\elasticsearch-8.2.3\\config\\certs\\http_ca.crt");
		
		CertificateFactory factory =
		    CertificateFactory.getInstance("X.509");
		
		Certificate trustedCa;
		
		try (InputStream is = Files.newInputStream(caCertificatePath)) {
		    trustedCa = factory.generateCertificate(is);
		}
		
		KeyStore trustStore = KeyStore.getInstance("pkcs12");
		
		trustStore.load(null, null);
		
		trustStore.setCertificateEntry("ca", trustedCa);
		
		SSLContextBuilder sslContextBuilder = SSLContexts.custom()
		    .loadTrustMaterial(trustStore, null);
		
		final SSLContext sslContext = sslContextBuilder.build();
		
		RestClientBuilder restClientBuilder = RestClient.builder(
		    new HttpHost("localhost", 9200, "https"))
		    .setHttpClientConfigCallback(new HttpClientConfigCallback() {
		        @Override
		        public HttpAsyncClientBuilder customizeHttpClient(
		            HttpAsyncClientBuilder httpClientBuilder) {
		        	httpClientBuilder.disableAuthCaching();
		        	httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
		            return httpClientBuilder.setSSLContext(sslContext);
		        }
		    });

//		restClientBuilder.setDefaultHeaders(defaultHeaders);			
			
		// Create the low-level client
		RestClient restClient = restClientBuilder.build();

		// Create the transport with a Jackson mapper
		ElasticsearchTransport transport = new RestClientTransport(
		    restClient, new JacksonJsonpMapper());

		// And create the API client
		ElasticsearchClient client = new ElasticsearchClient(transport);
		
		return client;
	}

}
