package com.iuh.ChatWebApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class StorageConfig {

	@Value("${ACCESS_KEY_ID}")
	private String accessKeyId;

	@Value("${SECRET_ACCESS_KEY}")
	private String accessKeSecret;

	@Value("${REGION}")
	private String bucketRegion;

	@Bean
	public AmazonS3 getAwsS3Client() {
		BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId, accessKeSecret);
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
				.withRegion(bucketRegion).build();
	}

}
