package com.example.demo.business;

import com.example.demo.api.model.registry.NPMRegistryPackageModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface NPMRegistryService {

    List<NPMRegistryPackageModel> registrySeqTest();

    CompletableFuture<List<NPMRegistryPackageModel>> registryAsyncTest();

    List<Future<NPMRegistryPackageModel>> registryVirtualThreadTest();
}
