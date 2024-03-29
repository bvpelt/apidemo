package nl.bsoft.apidemo.library.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.exception.InternalServerErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Getter
@Service
public class APIService extends ExternalRequestService {

    @Value("${nl.bsoft.apidemo.config.bestuurlijkegrenzen.rest.api.maxpagesize}")
    private int MAX_PAGE_SIZE; // = 50;

    @Value("${nl.bsoft.apidemo.config.bestuurlijkegrenzen.rest.api.baseurl}")
    private String apiUrl;

    @Value("${nl.bsoft.apidemo.config.bestuurlijkegrenzen.rest.api.apikey}")
    private String apiKey;

    @Value("${nl.bsoft.apidemo.config.bestuurlijkegrenzen.rest.api.coordinatereferencesystem}")
    private String coordinateReferenceSystem;

    @Autowired
    public APIService(ObjectMapper objectMapper, HttpClient httpClient, Duration requestDuration) {
        super(objectMapper, httpClient, requestDuration);
        log.debug("Started BestuurlijkGebiedService with apiUrl: {}, apkKey: {}, CRS: {}", apiUrl, apiKey, coordinateReferenceSystem);
    }

    @Override
    public HttpRequest.Builder request(URI uri) {
        log.debug("Started BestuurlijkGebiedService building request with apiUrl: {}, apkKey: {}, CRS: {}", apiUrl, apiKey, coordinateReferenceSystem);
        HttpRequest.Builder request =
                super.request(uri)
                        .header("Content-Crs", coordinateReferenceSystem)
                        .header("Accept-Crs", coordinateReferenceSystem);
        if (StringUtils.isNotBlank(apiKey)) {
            request = request.header("X-Api-Key", apiKey);
        }
        return request;
    }

    public <T> void processAllItemsFromCompletableFuture(
            BiFunction<Integer, Integer, CompletableFuture<T>> method, ToIntFunction<T> getTotalCount) {
        CompletableFuture<T> future = method.apply(0, MAX_PAGE_SIZE);
        T object = getCompletedFuture(future);
        for (int i = 1; i <= getTotalCount.applyAsInt(object); i++) {
            getCompletedFuture(method.apply(i, MAX_PAGE_SIZE));
        }
    }

    public <T> List<T> getAllItemsFromCompletableFuture(
            BiFunction<Integer, Integer, CompletableFuture<T>> method, ToIntFunction<T> getTotalCount) {
        CompletableFuture<T> future = method.apply(0, MAX_PAGE_SIZE);
        T object = getCompletedFuture(future);
        List<CompletableFuture<T>> futures =
                IntStream.range(1, getTotalCount.applyAsInt(object))
                        .mapToObj(pageIndex -> method.apply(pageIndex, MAX_PAGE_SIZE))
                        .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return Stream.concat(futures.stream(), Stream.of(future))
                .map(completedFuture -> completedFuture.getNow(null))
                .filter(Objects::nonNull)
                .toList();
    }

    private <T> T getCompletedFuture(CompletableFuture<T> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InternalServerErrorException(e);
        } catch (ExecutionException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
