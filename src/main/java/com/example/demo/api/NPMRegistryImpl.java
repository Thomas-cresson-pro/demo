package com.example.demo.api;

import com.example.demo.api.model.registry.NPMRegistryPackageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NPMRegistryImpl implements NPMRegistry {

    private final RestTemplate npmRestTemplate;

    @Override
    public NPMRegistryPackageModel getPackageInformation(String name) {
        return npmRestTemplate.getForObject(name, NPMRegistryPackageModel.class);
    }

    @Override
    public NPMRegistryPackageModel getPackageInformation(String name, String version) {
        return npmRestTemplate.getForObject(name + "/" + version, NPMRegistryPackageModel.class);
    }
}
