package org.acme.cadastro;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.PostgreSQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.utility.DockerImageName;

public class CadastroTestLifecycleManager implements QuarkusTestResourceLifecycleManager {

    public static final DockerImageName PG_IMAGE = DockerImageName.parse("postgres:12.2");

    private PostgreSQLContainer postgres = new PostgreSQLContainer<>(PG_IMAGE);


    @Override
    public Map<String, String> start() {
        postgres.start();
        Map<String, String> propriedades = new HashMap<String, String>();

        //Banco de dados
        propriedades.put("quarkus.datasource.jdbc.url", postgres.getJdbcUrl());
        propriedades.put("quarkus.datasource.username", postgres.getUsername());
        propriedades.put("quarkus.datasource.password", postgres.getPassword());

        return propriedades;
    }

    @Override
    public void stop() {
        if (postgres != null && postgres.isRunning()) {
            postgres.stop();
        }
    }
}
