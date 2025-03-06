package br.com.cahenre.testeAwsConnection.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.List;

@RestController
@RequestMapping("/teste")
@AllArgsConstructor
public class Entrypoint {

    @Autowired
    private final S3Client s3Client;

    @Autowired
    private SecretsManagerClient secretsManagerClient;


    @GetMapping("/listar")
    public ResponseEntity<List<String>> listar() {
        List<String> buckets = s3Client.listBuckets().buckets().stream().map(bucket -> bucket.name()).toList();


        return ResponseEntity.ok(buckets);
    }

    @GetMapping("/segredo")
    public ResponseEntity<String> segredo() {
        String secretArn = "arn:aws:secretsmanager:us-east-1:577638383225:secret:cahenre/teste-Ovvv9h";

        GetSecretValueRequest request = GetSecretValueRequest.builder()
                .secretId(secretArn)
                .build();

        GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);

        return ResponseEntity.ok(response.secretString());
    }



}
