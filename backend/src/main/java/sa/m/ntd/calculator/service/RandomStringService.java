package sa.m.ntd.calculator.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static sa.m.ntd.calculator.data.OperationData.EXTERNAL_RANDOM_ENDPOINT;

@Service
public class RandomStringService {

    private final RestClient restClient = RestClient.create();

    public String getNumber() {
        return restClient
                .get()
                .uri(EXTERNAL_RANDOM_ENDPOINT)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }
}
