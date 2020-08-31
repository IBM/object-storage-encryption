package com.example;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
import com.ibm.cloud.objectstorage.SdkClientException;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.EncryptionType;

public class CreateBucket {

	private static AmazonS3 _cos = null;

	public static void main(String[] args) {
		try {
			SDKGlobalConfiguration.IAM_ENDPOINT = Constants.COS_AUTH_ENDPOINT;

			_cos = createClient(Constants.COS_API_KEY_ID, Constants.COS_SERVICE_CRN, Constants.COS_ENDPOINT,
					Constants.COS_BUCKET_LOCATION);
			createBucketKP(Constants.BUCKET_NAME);
		} catch (SdkClientException sdke) {
			sdke.printStackTrace();
			System.out.printf("SDK Error: %s\n", sdke.getMessage());
		} catch (Exception e) {
			System.out.printf("Error: %s\n", e.getMessage());
		}

	}

	public static void createBucketKP(String bucketName) {
		System.out.printf("Creating new encrypted bucket: %s\n", bucketName);

		EncryptionType encType = new EncryptionType();
		encType.setKmsEncryptionAlgorithm(Constants.COS_KP_ALGORITHM);
		encType.setIBMSSEKMSCustomerRootKeyCrn(Constants.COS_KP_ROOTKEY_CRN);

		CreateBucketRequest req = new CreateBucketRequest(bucketName).withEncryptionType(encType);
		;
		_cos.createBucket(req);

		System.out.printf("Bucket: %s created!", bucketName);
	}

	public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url,
			String location) {
		AWSCredentials credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);
		ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
		clientConfig.setUseTcpKeepAlive(true);

		AmazonS3 cos = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location))
				.withPathStyleAccessEnabled(true).withClientConfiguration(clientConfig).build();

		return cos;
	}

}
