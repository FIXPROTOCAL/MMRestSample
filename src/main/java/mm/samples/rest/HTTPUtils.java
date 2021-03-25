package mm.samples.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HTTPUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String login(String url, String user, String password) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url + "/oauth/token");
            httpPost.setHeader("Authorization", "Basic bW13ZWJ1aTptbQ==");

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "password"));
            params.add(new BasicNameValuePair("username", user));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("scope", "public"));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                TokenData tokenData = objectMapper.readValue(entity.getContent(), TokenData.class);
                return tokenData.access_token;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T> T post(String url, String query, String token, T body, Class<T> responseType) throws Exception {
        return objectMapper.readValue(post(url, query, token, objectMapper.writeValueAsString(body)), responseType);
    }

    public static <T> T post(String url, String query, String token, Class<T> responseType) throws Exception {
        return objectMapper.readValue(post(url, query, token, (String) null), responseType);
    }

    public static <T> void post(String url, String query, String token, T body) throws Exception {
        post(url, query, token, objectMapper.writeValueAsString(body));
    }

    public static String post(String url, String query, String token, String json) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url + query);
            httpPost.setHeader("Authorization", "bearer " + token);

            if (json != null) {
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setEntity(new StringEntity(json));
            }

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                return IOUtils.toString(entity.getContent(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
