package com.lepik.mugloar.client;

import com.lepik.mugloar.client.dto.*;
import com.lepik.mugloar.mappers.*;
import com.lepik.mugloar.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
public class GameApiClient {
    private final String baseUrl;
    private final RestTemplate restTemplate;
    private final TaskMapper taskMapper;
    private final GameMapper gameMapper;
    private final SolvedTaskMapper solvedTaskMapper;
    private final ShopMapper shopMapper;
    private final PurchaseMapper purchaseMapper;
    private final ReputationMapper reputationMapper;

    public GameApiClient(
            @Value("${mugloar.api.base-url}") String baseUrl,
            RestTemplate restTemplate,
            TaskMapper taskMapper,
            GameMapper gameMapper,
            SolvedTaskMapper solvedTaskMapper,
            ShopMapper shopMapper,
            PurchaseMapper purchaseMapper, ReputationMapper reputationMapper
    ) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
        this.taskMapper = taskMapper;
        this.gameMapper = gameMapper;
        this.solvedTaskMapper = solvedTaskMapper;
        this.shopMapper = shopMapper;
        this.purchaseMapper = purchaseMapper;
        this.reputationMapper = reputationMapper;
    }

        public Optional<Game> startGame() {
            try {
                String url = baseUrl + "/game/start";
                StartGameResponse response = restTemplate.postForObject(url, null, StartGameResponse.class);
                return Optional.of(gameMapper.toDomain(response));
            } catch (HttpClientErrorException e) {
                System.out.println("Failed to start a new game: " + e.getResponseBodyAsString());
                return Optional.empty();
            }
        }

        public Optional<List<Task>> getTasks(String gameId) {
            try {
                String url = baseUrl + String.format("/%s/messages", gameId);
                TaskResponse[] taskResponseList = restTemplate.getForObject(url, TaskResponse[].class);
                return Optional.of(taskMapper.toDomainList(taskResponseList));
            } catch (RestClientException e) {
                System.out.println("Failed to fetch tasks for game " + gameId + ": " + e.getMessage());
                return Optional.empty();
            }
        }

        public Optional<SolvedTask> SolveTask(String gameId, String adId) {
            String url = baseUrl + String.format("/%s/solve/%s", gameId, adId);
            try {
                SolvedTaskResponse response =
                        restTemplate.postForObject(url, null, SolvedTaskResponse.class);
                return Optional.of(solvedTaskMapper.toDomain(response));
            } catch (HttpClientErrorException.BadRequest e) {
                System.out.println("Solve rejected for ad " + adId + ": " + e.getResponseBodyAsString());
                return Optional.empty();
            }
        }

        public Optional<List<Item>> getShopItems(String gameId) {
            try {
                String url = baseUrl + String.format("/%s/shop", gameId);
                ShopResponse[] shopResponseList = restTemplate.getForObject(url, ShopResponse[].class);
                return Optional.of(shopMapper.toDomainList(shopResponseList));
            } catch (RestClientException e) {
                System.out.println("Failed to fetch shop items for game " + gameId + ": " + e.getMessage());
                return Optional.empty();
            }
        }

        public Optional<Purchase> buyItem(String gameId, String itemId) {
            try {
                String url = baseUrl + String.format("/%s/shop/buy/%s", gameId, itemId);
                PurchaseResponse purchaseResponse = restTemplate.postForObject(url, null, PurchaseResponse.class);
                return Optional.of(purchaseMapper.toDomain(purchaseResponse));
            } catch (RestClientException e) {
                System.out.println("Failed to buy item " + itemId + " for game " + gameId + ": " + e.getMessage());
                return Optional.empty();
            }
        }

        public Optional<Reputation> getReputation(String gameId) {
            try {
                String url = baseUrl + String.format("/%s/investigate/reputation", gameId);
                ReputationResponse reputationResponse = restTemplate.postForObject(url, null, ReputationResponse.class);
                return Optional.of(reputationMapper.toDomain(reputationResponse));
            } catch (RestClientException e) {
                System.out.println("Failed to investigate for game " + gameId + ": " + e.getMessage());
                return Optional.empty();
            }
        }
}
