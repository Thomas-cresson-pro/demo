package com.example.demo.business;

import ch.qos.logback.classic.Logger;
import com.example.demo.api.NPMRegistry;
import com.example.demo.api.model.registry.NPMRegistryPackageModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Component
public class NPMRegistryServiceImpl implements NPMRegistryService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(NPMRegistryServiceImpl.class);

    private final NPMRegistry npmRegistry;

    private final AsyncTaskExecutor applicationTaskExecutor;

    @Override
    public List<NPMRegistryPackageModel> registrySeqTest() {
        NPMRegistryPackageModel angular = npmRegistry.getPackageInformation("angular");
        return angular.getVersions().keySet().stream().map(k -> {
            logger.info("angular version {} begin", k);
            NPMRegistryPackageModel angular1 = npmRegistry.getPackageInformation("angular", k);
            logger.info("angular version {} finished", k);
            return angular1;
        }).toList();
    }

    @Override
    @Async
    public CompletableFuture<List<NPMRegistryPackageModel>> registryAsyncTest() {
        NPMRegistryPackageModel angular = npmRegistry.getPackageInformation("angular");
        List<CompletableFuture<NPMRegistryPackageModel>> list = angular.getVersions().keySet().stream()
                .map((k) -> CompletableFuture.supplyAsync(() -> npmRegistry.getPackageInformation("angular", k))).toList();
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
        return allDoneFuture.thenApply(v -> list.stream().map(CompletableFuture::join).toList());
    }

    @Override
    public List<Future<NPMRegistryPackageModel>> registryVirtualThreadTest() {
        NPMRegistryPackageModel angular = npmRegistry.getPackageInformation("angular");
        return angular.getVersions().keySet().stream().map((k) -> applicationTaskExecutor.submit(() -> {
            Thread thread = Thread.currentThread();
            logger.info("thread {} : angular version {} begin", thread, k);
            return npmRegistry.getPackageInformation("angular", k);
        })).toList();
    }
}
