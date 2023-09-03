package com.example.demo;

import com.example.demo.api.NPMRegistry;
import com.example.demo.api.model.registry.NPMRegistryPackageModel;
import com.example.demo.business.NPMRegistryService;
import com.example.demo.dao.PersonEntityRepository;
import com.example.demo.entities.PersonEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class DemoApplicationTests {

    @Autowired
    private PersonEntityRepository personEntityRepository;
    @Autowired
    private NPMRegistry npmRegistry;
    @Autowired
    private NPMRegistryService npmRegistryService;

    @Test
    void testDAO() {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setFirstName("toto");
        personEntity.setLastName("tata");
        personEntity.setEmail("toto.tata@email.com");
        personEntityRepository.saveAndFlush(personEntity);
        List<PersonEntity> all = personEntityRepository.findAll();
        Assertions.assertThat(all).isNotEmpty().hasSize(1);
    }

    @Test
    void testRegistry() {
        System.out.println();
        long start = System.nanoTime();
        npmRegistryService.registrySeqTest();
        long end = System.nanoTime();
        System.out.println((end - start) / 1_000_000_000.0 + "s");

    }

    @Test
    void testRegistryVirtualThread() {
        // Problème I/O avec des ports déja utilisés, ça reste expérimental
        long start = System.nanoTime();
        List<Future<NPMRegistryPackageModel>> npmRegistryPackageModels = npmRegistryService.registryVirtualThreadTest();
        List<NPMRegistryPackageModel> result = new ArrayList<>();
        npmRegistryPackageModels.forEach(task -> {
            try {
                NPMRegistryPackageModel e = task.get();
                System.out.println(e);
                result.add(e);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        long end = System.nanoTime();
        System.out.println((end - start) / 1_000_000_000.0 + "s");
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    void testVirtualThread() {
        long start = System.nanoTime();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1_000_000; i++) {
                executor.execute(() -> {
                    Thread thread = Thread.currentThread();
                    try {
                        System.out.println(thread);
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        long end = System.nanoTime();
        System.out.println((end - start) / 1_000_000_000.0 + "s");
    }

    @Test
    void testRegistryAsync() throws ExecutionException, InterruptedException {
        // On le fait à la création des threads
        long start = System.nanoTime();
        CompletableFuture<List<NPMRegistryPackageModel>> npmRegistryPackageModels = npmRegistryService.registryAsyncTest();
        List<NPMRegistryPackageModel> npmRegistryPackageModels1 = npmRegistryPackageModels.get();
        long end = System.nanoTime();
        System.out.println((end - start) / 1_000_000_000.0 + "s");
        Assertions.assertThat(npmRegistryPackageModels1).isNotEmpty();
        // Thread créé, plus rapide
        start = System.nanoTime();
        List<NPMRegistryPackageModel> npmRegistryPackageModels2 = npmRegistryService.registryAsyncTest().get();
        end = System.nanoTime();
        System.out.println((end - start) / 1_000_000_000.0 + "s");
        Assertions.assertThat(npmRegistryPackageModels2).isNotEmpty();

    }

    @Test
    void limitAsync() {
        long start = System.nanoTime();
        NPMRegistryPackageModel angular = npmRegistry.getPackageInformation("angular");
        Executor executor = Executors.newFixedThreadPool(200000);
        angular.getVersions().forEach((k, v) -> {
            for (int i = 0; i < 100000; i++) {
                CompletableFuture.runAsync(() -> npmRegistry.getPackageInformation("angular", k), executor);
            }
        });
        long end = System.nanoTime();
        System.out.println((end - start) / 1_000_000_000.0 + "s");
    }

}
