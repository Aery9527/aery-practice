import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Go {

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet("https://www.ptt.cc/bbs/DIABLO/M.1637803267.A.211.html");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity == null) {
                System.out.println("WTF!?");
                return;
            }

            String result = EntityUtils.toString(entity);
            String[] lines = result.split("span.[\\w|\\s|\\=|\\\"|-]+push-content\">:");

            Pattern pattern = Pattern.compile("7(\\d{3})10");

            Set<Integer> push = new HashSet<>();
            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    push.add(Integer.valueOf(matcher.group()));
                }
            }

            Set<Integer> all = new HashSet<>();
            for (int i = 100; i < 100000; i += 100) {
                int n = 700010 + i;
                all.add(n);
            }

            all.removeAll(push);

            System.out.println("Not yet appeared (" + all.size() + ") :");
            all.forEach(System.out::println);
        }

    }

}
