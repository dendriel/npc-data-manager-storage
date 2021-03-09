package com.rozsa.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.rozsa.business.SystemPropertiesBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;


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
            System.out.format("Bucket \"%s\" already exists.\n", bucketName);
            return;
        }

        try {
            client.createBucket(bucketName);
            System.out.format("Bucket \"%s\" has been created.\n", bucketName);
        } catch (AmazonS3Exception e) {
            System.err.println(e.getErrorMessage());
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

        System.out.format("Uploading %s to S3 bucket %s...\n", idStorage, bucketName);

        try {
            client.putObject(bucketName, idStorage, file);

            PutObjectRequest request = new PutObjectRequest(bucketName, idStorage, file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            request.setMetadata(metadata);
            client.putObject(request);

        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            return null;
        }

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
        S3Object s3object = client.getObject(bucketName, storageId);
        S3ObjectInputStream inputStream = s3object.getObjectContent();

        ObjectMetadata metadata = s3object.getObjectMetadata();

        StorageResourceInputStream resource = new StorageResourceInputStream(
                storageId, inputStream, metadata.getContentType(), metadata.getContentLength());

        return resource;
    }

}
