package com.rozsa.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
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

    public String create(File file, String ext, String directory) {
        String newId = randomUUID().toString();
        String idStorage = directory + "/" + newId + "." + ext;

        System.out.format("Uploading %s to S3 bucket %s...\n", idStorage, bucketName);

        try {
            client.putObject(bucketName, idStorage, file);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            return null;
        }

        return idStorage;
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


}
