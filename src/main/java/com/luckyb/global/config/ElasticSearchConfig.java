package com.luckyb.global.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.luckyb.domain.search")
public class ElasticSearchConfig {

  @Value("${spring.data.elasticsearch.url}")
  private String host;

  @Value("${spring.data.elasticsearch.username}")
  private String username;

  @Value("${spring.data.elasticsearch.password}")
  private String password;

  @Bean
  public ElasticsearchClient elasticsearchClient() {
    // Basic Auth
    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(
        AuthScope.ANY,
        new UsernamePasswordCredentials(username, password)
    );

    RestClient restClient = RestClient.builder(HttpHost.create(host))
        .setHttpClientConfigCallback(httpClientBuilder ->
            httpClientBuilder
                .setSSLContext(disableSslVerification())  // SSL 무시
                .setSSLHostnameVerifier(allHostsValid())  // Hostname 검증 무시
                .setDefaultCredentialsProvider(credentialsProvider) // 인증 적용
        )
        .build();

    ElasticsearchTransport transport = new RestClientTransport(restClient,
        new JacksonJsonpMapper());
    return new ElasticsearchClient(transport);
  }

  public static SSLContext disableSslVerification() {
    try {
      TrustManager[] trustAllCerts = new TrustManager[]{
          new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
              return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
          }
      };
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      return sc;
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      log.warn(e.getMessage());
    }
    return null;
  }

  public static HostnameVerifier allHostsValid() {
    return (hostname, session) -> true;
  }
}
