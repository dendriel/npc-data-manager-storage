package com.rozsa.s3.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.rozsa.configuration.StorageProperties;
import com.rozsa.s3.StorageResourceInputStream;
import com.rozsa.s3.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.UUID.randomUUID;


@Slf4j
@Service("storageS3")
public class StorageS3 implements StorageService {
    private final AmazonS3 client;

    private final String bucketName;

    @Autowired
    public StorageS3(StorageProperties storageProperties) {
        bucketName = storageProperties.getBucketName();
        client = initializeClient();

        initializeBucket();
    }

    private AmazonS3 initializeClient() {
        return AmazonS3ClientBuilder.standard().build();
    }

    private void initializeBucket() {
        if (client.doesBucketExistV2(bucketName)) {
            log.info("Bucket \"{}\" already exists", bucketName);
            return;
        }

        try {
            client.createBucket(bucketName);
            log.info("Bucket \"{}\" has been created", bucketName);
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
            PutObjectRequest request = new PutObjectRequest(bucketName, idStorage, file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            request.setMetadata(metadata);
            client.putObject(request);

        } catch (Exception e) {
            log.error("Failed to upload file {} to S3 bucket {}. Error: {}", idStorage, bucketName, e);
            return null;
        }

        log.info("Successfully uploaded file {} to S3 bucket {}", idStorage, bucketName);

        return idStorage;
    }

    @Override
    public void deleteResource(String idStorage) {
        try {
            client.deleteObject(bucketName, idStorage);
        } catch (Exception e) {
            log.error("Failed to remove resource {} from bucket.", idStorage, e);
            return;
        }

        log.info("Resource {} was successfully removed from bucket.", idStorage);
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
            log.error("Failed to get resource from bucket.", e);
            return null;
        }

        S3ObjectInputStream inputStream = s3object.getObjectContent();
        ObjectMetadata metadata = s3object.getObjectMetadata();

        return new StorageResourceInputStream(inputStream, metadata.getContentType(), metadata.getContentLength());
    }

    @Override
    public URL getResourceAccessUrl(String storageId, Date expirationDate) {
        try {
            return client.generatePresignedUrl(bucketName, storageId, expirationDate);
        } catch (Exception e) {
            log.error("Failed to generate pre-signed URL for storageId: {}; and expirationDate: {}", storageId, expirationDate, e);
            return null;
        }
    }
}
