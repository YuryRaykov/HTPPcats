import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static final String REMOTE_SERVICE_URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static ObjectMapper mapper = new ObjectMapper(); //ObjectMapper осуществляет десирилизацию и серилизацию

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create() // создаем htpp клиент и кофигурируем его
                .setUserAgent("My Test Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000) // время ожидания для подключения (5 сек.)
                        .setSocketTimeout(30000) // сколько держим сокет открытым (как мы подключились не более 30 сек)
                        .setRedirectsEnabled(false) // не отправляем повторный запрос
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType()); // указываем какой формат мы поддерживаем

        CloseableHttpResponse response = httpClient.execute(request); // вызываем метод execute передав ему объект запроса request
        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
        List<Post> posts = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() { // выводим определенный список постов
                }
        );

        posts.stream()
                .filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0)
                .forEach(System.out::println);

        response.close();
        httpClient.close();
    }

}
