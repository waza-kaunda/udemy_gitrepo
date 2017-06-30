package com.devopsbuddy.backend.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

	private static final Logger log = LoggerFactory.getLogger(S3Service.class);

	private static final String PROFILE_PICTURE_FILE_NAME = "profilePicture";

	@Value("${aws.s3.root.bucket.name}")
	private String bucketName;

	@Value("${aws.s3.profile}")
	private String awsProfileName;

	@Value("$image.store.tmp.folder")
	private String tempImageStore;

	@Autowired
	private AmazonS3Client s3Client;

	/**
	 * 
	 * @param uploadedFile
	 * @param username
	 * @return
	 * @throws IOException
	 */
	public String storeProfileImage(MultipartFile uploadedFile, String username) throws IOException {

		String profileImageUrl = null;

		if (uploadedFile != null && !uploadedFile.isEmpty()) {

			byte[] bytes = uploadedFile.getBytes();

			// The root of our temporary assets. Will create if it doesn't exist
			File tmpImageStoredFolder = new File(tempImageStore + File.separatorChar + username);
			if (!tmpImageStoredFolder.exists()) {
				log.info("Creating the temporary root for the S3 assets");
				tmpImageStoredFolder.mkdirs();
			}

			// The Temporary file where the profile image will be stored
			File tmpProfileImageFile = new File(tmpImageStoredFolder.getAbsolutePath() + File.separatorChar
					+ PROFILE_PICTURE_FILE_NAME + "." + FilenameUtils.getExtension(uploadedFile.getOriginalFilename()));

			log.info("Temporary file wil be saved to {}", tmpProfileImageFile.getAbsolutePath());

			try (BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(tmpProfileImageFile.getAbsolutePath()))) {
				stream.write(bytes);
			}

			profileImageUrl = this.storeProfileImageToS3(tmpProfileImageFile, username);

			tmpProfileImageFile.delete();
		}

		return profileImageUrl;
	}

	/**
	 * 
	 * @param resource
	 * @param username
	 * @return
	 */
	private String storeProfileImageToS3(File resource, String username) {

		String resourceUrl = null;

		if (!resource.exists()) {
			log.error("The file {} does not exist. Throwing an exception", resource.getAbsolutePath());
			throw new IllegalArgumentException("The File " + resource.getAbsolutePath() + " doesn't exist");
		}

		String rootBucketUrl = this.ensureBucketExists(bucketName);

		if (rootBucketUrl == null) {
			log.error(
					"This bucket {} does not exist and the application was not able to create it. The image won't be stored with the profile",
					rootBucketUrl);

		} else {
			AccessControlList acl = new AccessControlList();
			acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

			String key = username + "/" + PROFILE_PICTURE_FILE_NAME + "."
					+ FilenameUtils.getExtension(resource.getName());

			try {
				s3Client.putObject(new PutObjectRequest(bucketName, key, resource).withAccessControlList(acl));
				resourceUrl = s3Client.getResourceUrl(bucketName, key);
			} catch (AmazonClientException ace) {
				log.error(
						"A Client exception occurred while trying to store the profile image {} on S3. The profile image won't be stored",
						resource.getAbsolutePath(), ace);
			}
		}

		return resourceUrl;
	}

	/**
	 * 
	 * @param bucketName
	 * @return
	 */
	private String ensureBucketExists(String bucketName) {

		String bucketUrl = null;

		try {

			if(!s3Client.doesBucketExist(bucketName)){
				log.info("Bucket {} doesn't exist... Creating one", bucketName);
				s3Client.createBucket(bucketName);
				log.info("Created buket : {}", bucketName);
			}
			bucketUrl = s3Client.getResourceUrl(bucketName, null) + bucketName;
		} catch (AmazonClientException ace) {

			log.error("An error occurred while connecting to S3. Will not execute action for bucket: {}", bucketName,
					ace);

		}

		return bucketUrl;
	}

}
