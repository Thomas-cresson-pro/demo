package com.example.demo.api;

import com.example.demo.api.model.registry.NPMRegistryPackageModel;

public interface NPMRegistry {

    NPMRegistryPackageModel getPackageInformation(String name);

    NPMRegistryPackageModel getPackageInformation(String name, String version);
}
