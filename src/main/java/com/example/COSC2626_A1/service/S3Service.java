package com.example.COSC2626_A1.service;


import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.model.*;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;

@Service
public class S3Service implements AutoCloseable {
    private final AmazonS3 s3Client;
    private final String s3BucketName;

    @Value("${app.testing}")
    private boolean testing;

    public S3Service(
            @Value("${aws.access.key}") String accessKey,
            @Value("${aws.access.secret-key}") String secretKey,
            @Value("${aws.sessionToken}") String sessionToken,
            @Value("${aws.region}") String regionName,
            @Value("${aws.s3.bucket-name}") String s3BucketName) {

        this.s3BucketName = s3BucketName;

        //Create S3 client.
        //Do this once, in the constructor, to minimise overhead with credentials etc.
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicSessionCredentials(
                        accessKey,
                        secretKey,
                        sessionToken)))
                .withRegion(regionName)
                .build();
    }

    @Override
    public void close() {
        //NOTE: Ordinarily, this would NOT occur in a real-world app, but is being done here to save AW$$$
        System.out.println("Closing S3 client...");

        if(this.testing) {
            System.out.println("TESTING: BUCKET WILL NOT BE DELETED");
        } else {
            deleteBucket();
        }

        System.out.println("S3 client closed.");
    }

    public void createS3BucketIfNotExists() {
        //Create the S3 bucket on AWS
        //Adapted from Week03 workshop S3Tasks code (https://rmit.instructure.com/courses/141320/files/43744075?wrap=1)
        //Viewed: 2025-03-14

        System.out.println("Creating bucket " + s3BucketName);

        try {
            if (!s3Client.doesBucketExistV2(s3BucketName)) {
                //Create bucket in the region specified by the client.
                s3Client.createBucket(new CreateBucketRequest(s3BucketName));
                System.out.println("S3 bucket created: " + s3BucketName);
            } else {
                System.out.println("Bucket: " + s3BucketName + " already exists");
            }

            //Verify that the bucket was created by retrieving it and checking its location.
            String bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(s3BucketName));
            System.out.println("Bucket location: " + bucketLocation);

        }  catch (AmazonServiceException e) {
            //Call transmitted successfully, but AWS S3 couldn't process it and returned an error response.
            e.printStackTrace();

        } catch (SdkClientException e) {
            //Couldn't reach AWS S3 for response, or client couldn't parse the response .
            e.printStackTrace();
        }
    }

    public void uploadFileList(Map<String, Path> imageFiles) {
        for(Map.Entry<String, Path> entry : imageFiles.entrySet()) {

            //TODO: Move this to image downloader
            if (s3Client.doesObjectExist(s3BucketName, entry.getKey())) {
                System.out.println("Skipped: " + entry.getKey() + "(already exists).");
                continue;
            }

            if (uploadFile(entry.getKey(), entry.getValue())) {
                System.out.println("Uploaded: " + entry.getKey());
            } else {
                System.out.println("Upload of " + entry.getKey() + " failed");
            }
        }
    }
    public boolean uploadFile(String fileName, Path filePath) {
        //Upload a local file to S3 using the given fileName and path
        //Adapted from Week03 workshop S3Tasks code (https://rmit.instructure.com/courses/141320/files/43744075?wrap=1)
        //Viewed: 2025-03-14
        boolean status = false;

        try {
            // Upload a file as a new object with ContentType and title specified.
            PutObjectRequest request = new PutObjectRequest(s3BucketName, fileName, filePath.toFile());
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");
            metadata.addUserMetadata("title", fileName);
            request.setMetadata(metadata);
            s3Client.putObject(request);
            status = true;

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return status;
    }

    public String getPreSignedImageUrl(String fileName) {
        //Generate an (expiring) pre-signed URL for the image stored in S3.
        //...this prevents the backend from having to download and store the image each time.

        //Adapted from example here:
        //https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/doc_source/PresignedUrlUploadObjectJavaSDK.md
        //Viewed: 2025-04-13

        //Set the expiration time for the link to 1 hour.
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(s3BucketName, fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }


    public void deleteBucket() {
        //Delete the (unversioned) S3 bucket from AWS
        //Adapted from Week03 workshop S3Tasks code (https://rmit.instructure.com/courses/141320/files/43744075?wrap=1)
        //Viewed: 2025-03-14

        try {
            ObjectListing objectListing = s3Client.listObjects(s3BucketName);

            System.out.println("Deleting all objects in bucket: " + s3BucketName);

            //Delete all objects from the bucket.
            while (true) {
                for (S3ObjectSummary obj : objectListing.getObjectSummaries()) {
                    System.out.println("Deleting object: " + obj.getKey());
                    s3Client.deleteObject(s3BucketName, obj.getKey());
                }

                //The listObjects() call returns object listings in pages.
                //f the listing was truncated, we need to retrieve the next page of objects and continue.
                if (objectListing.isTruncated()) {
                    objectListing = s3Client.listNextBatchOfObjects(objectListing);
                } else {
                    System.out.println("No more objects in bucket: " + s3BucketName);
                    break;
                }
            }

            //Bucket is empty; delete the bucket.
            System.out.println("Deleting bucket: " + s3BucketName +"...");
            s3Client.deleteBucket(s3BucketName);
            System.out.println("Bucket deleted.");

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client couldn't
            // parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}
