import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static ObjectMapper mapper = new ObjectMapper();
    public static final String REMOTE_SERVICE_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

            List<Cats> cats = mapper.readValue(
                    response.getEntity().getContent(), new
                            TypeReference<>() {
                            });
            cats = cats.stream()
                    .filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0)
                    .collect(Collectors.toList());

            cats.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}



