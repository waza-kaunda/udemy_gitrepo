package com.devopsbuddy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
@EnableJpaRepositories(basePackages = "com.devopsbuddy.backend.persistence.repositories" )
@EntityScan(basePackages = "com.devopsbuddy.backend.persistence.domain.backend")
@EnableTransactionManagement
@PropertySource("file:///${user.home}/.devopsbuddy/application-common.properties")
@PropertySource("file:///${user.home}/.devopsbuddy/stripe.properties")
public class ApplicationConfig {

	@Value("${aws.s3.profile}")
	private String awsProfileName;
	
	@Value("${cloud.aws.credentials.accessKey}")
	private String accessKey;
	
	@Value("${cloud.aws.credentials.secretKey}")
	private String secretKey;
	
	@Value("${cloud.aws.region.static}")
	private String regionName;
		
	@Bean
	public BasicAWSCredentials basicAWSCredentials(){
		return new BasicAWSCredentials(accessKey, secretKey);
	}
	
	@Bean
	public AmazonS3Client s3Client(AWSCredentials awsCredentials){
		
		AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
		Region region = Region.getRegion(Regions.fromName(regionName));
		s3Client.setRegion(region);
		return s3Client;
		
	}
}
