package com.rozsa.s3.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.rozsa.business.SystemPropertiesBusiness;
import com.rozsa.s3.StorageResourceInputStream;
import com.rozsa.s3.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;


@Slf4j
@Service("storageS3")
public class StorageS3 implements StorageService {
    private final AmazonS3 client;

    private final String bucketName;
    @Autowired
    public StorageS3(SystemPropertiesBusiness systemProperties) {
        client = initializeClient();

        bucketName = systemProperties.getStorageBucketName();

        initializeBucket();
    }

    private AmazonS3 initializeClient() {
        return AmazonS3ClientBuilder.standard().build();
    }

    private void initializeBucket() {
        if (client.doesBucketExistV2(bucketName)) {
            log.info("Bucket \"{}\" already exists.\n", bucketName);
            return;
        }

        try {
            client.createBucket(bucketName);
            log.info("Bucket \"{}\" has been created.\n", bucketName);
        } catch (AmazonS3Exception e) {
            log.error("Failed to create bucket. Error: " + e.getErrorMessage());
        }
    }

    private String createId() {
        String newId = randomUUID().toString();
        newId = newId.replace("-", "");

        // TODO: Maybe validate ids to check for duplicates.
        return newId;
    }

    @Override
    public String createDirectory(String directory) {
        return createId();
    }

    @Override
    public String createResource(File file, String contentType, String ext, String directory) {
        String newId = createId();
        String idStorage = directory + "/" + newId + "." + ext;

        log.info("Uploading {} to S3 bucket {}...", idStorage, bucketName);

        try {
            client.putObject(bucketName, idStorage, file);

            PutObjectRequest request = new PutObjectRequest(bucketName, idStorage, file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            request.setMetadata(metadata);
            client.putObject(request);

        } catch (AmazonServiceException e) {
            log.error("Failed to upload file {} to S3 bucket {}. Error: {}", idStorage, bucketName, e.getErrorMessage());
            return null;
        }

        log.info("Successfully uploaded file {} to S3 bucket {}", idStorage, bucketName);

        return idStorage;
    }

    @Override
    public void deleteResource(String resource) {
        // TODO: doesnt seems the most trustable operation..
        // TODO: how to wait for delete confirmation?
        client.deleteObject(bucketName, resource);
    }

    @Override
    public List<String> listDirectories() {
        ListObjectsV2Result result = client.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        List<String> directories = new ArrayList<>();
        for (S3ObjectSummary os : objects) {
            String[] tokens = os.getKey().split("/");
            if (tokens.length == 1) {
                directories.add(tokens[0]);
            }
        }

        return directories;
    }

    @Override
    public List<String> listResources(String directory) {
        ListObjectsV2Result result = client.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        List<String> resources = new ArrayList<>();

        for (S3ObjectSummary os : objects) {
            String resourceName = getResourceNameFrom(os, directory);
            if (resourceName != null) {
                resources.add(resourceName);
            }
        }

        return resources;
    }

    private String getResourceNameFrom(S3ObjectSummary os, String directory) {
        String[] tokens = os.getKey().split("/");
        if (tokens.length <= 1) {
            return null;
        }

        String dirName = tokens[0];
        if (!dirName.equals(directory)) {
            return null;
        }

        StringBuilder resourceName = new StringBuilder();

        for (int i = 1; i < tokens.length; i++) {
            resourceName.append(tokens[i]);
            if (i + 1 < tokens.length) {
                resourceName.append("/");
            }
        }

        return resourceName.toString();
    }

    @Override
    public StorageResourceInputStream getResource(String storageId) {
        S3Object s3object;
        try {
            s3object = client.getObject(bucketName, storageId);
        }
        catch (AmazonServiceException e) {
            System.err.println(e.toString());
            return null;
        }

        S3ObjectInputStream inputStream = s3object.getObjectContent();
        ObjectMetadata metadata = s3object.getObjectMetadata();

        return new StorageResourceInputStream(inputStream, metadata.getContentType(), metadata.getContentLength());
    }

}
