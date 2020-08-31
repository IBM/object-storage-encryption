package com.example;

import com.ibm.cloud.objectstorage.config.resource_configuration.v1.ResourceConfiguration;
import com.ibm.cloud.objectstorage.config.resource_configuration.v1.model.*;
import com.ibm.cloud.sdk.core.service.security.IamOptions;

public class ConfigureLogging {

	private static final String BUCKET = Constants.BUCKET_NAME;
	private static final String API_KEY = Constants.COS_API_KEY_ID;
	private static final String AT_CRN = Constants.AT_CRN;

	public static void main(String[] args) {

		IamOptions options = new IamOptions.Builder().apiKey(API_KEY).build();

		ResourceConfiguration CLIENT = new ResourceConfiguration(options);

		ActivityTracking activityTracking = new ActivityTracking();
		activityTracking.setActivityTrackerCrn(AT_CRN);
		activityTracking.setReadDataEvents(true);
		activityTracking.setWriteDataEvents(true);

		UpdateBucketConfigOptions update = new UpdateBucketConfigOptions.Builder(BUCKET)
				.activityTracking(activityTracking).build();

		CLIENT.updateBucketConfig(update).execute();

		GetBucketConfigOptions bucketOptions = new GetBucketConfigOptions.Builder(BUCKET).build();
		Bucket bucket = CLIENT.getBucketConfig(bucketOptions).execute();
		System.out.println(bucket);
	}
}
