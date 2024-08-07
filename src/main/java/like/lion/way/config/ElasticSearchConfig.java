package like.lion.way.config;

import java.util.Arrays;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "like.lion.way.els.repository")
public class ElasticSearchConfig {

    @Value("${spring.elasticsearch.uris}")
    private String[] uris;

    @Value("${spring.elasticsearch.username}")
    private String username;

    @Value("${spring.elasticsearch.password}")
    private String password;

    @Bean
    public RestHighLevelClient client() {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));


        RestClientBuilder builder = RestClient.builder(
                Arrays.stream(uris)
                        .map(uri -> new HttpHost(uri, 9200, "http"))
                        .toArray(HttpHost[]::new)
        ).setHttpClientConfigCallback(httpClientBuilder ->
                httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider)
        );

        return new RestHighLevelClient(builder);
    }
}
