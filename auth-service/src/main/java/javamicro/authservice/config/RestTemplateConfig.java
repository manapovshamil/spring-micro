package javamicro.authservice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        try {
            // 1. Создаём SSL-контекст, который принимает все сертификаты (ТОЛЬКО для отладки!)
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial((X509Certificate[] chain, String authType) -> true)
                    .build();
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                    sslContext, NoopHostnameVerifier.INSTANCE);

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslSocketFactory)
                    .build();
            // 2. Настраиваем пул соединений с SSL
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(100); // Максимум 100 соединений
            connectionManager.setDefaultMaxPerRoute(20); // 20 соединений на хост
            //connectionManager.setValidateAfterInactivity(TimeValue.ofSeconds(30));

            // 3. Настраиваем таймауты
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(Timeout.ofSeconds(10))  // Таймаут подключения (15 сек)
                    .setResponseTimeout(Timeout.ofSeconds(20)) // Таймаут ответа (30 сек)
                    .build();

            // 4. Создаём HttpClient с ConnectionManager (замена `setSSLContext()`)
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(connectionManager) // Вместо `setSSLContext()`
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            // 5. Подключаем к RestTemplate
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
            return new RestTemplate(factory);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.error("Ошибка при настройке SSL для RestTemplate: {}", e.getMessage());
            throw new RuntimeException("Ошибка SSL", e);
        }
    }
}
