package org.iit.assignment.cloudkon.dynamoDB;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

/**
 * <p>
 * Makes connection to DynamoDB database to send and receive data to DynamoDB
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
public class DynamoDB {

	static AmazonDynamoDBClient dynamoDB;

	public static final String accessKey = "AKIAJ25X2SUASHBZ4P3A";
	public static final String secretKey = "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";

	/**
	 * The only information needed to create a client are security credentials
	 * consisting of the AWS Access Key ID and Secret Access Key. All other
	 * configuration, such as the service endpoints, are performed
	 * automatically. Client parameters, such as proxies, can be specified in an
	 * optional ClientConfiguration object when constructing a client.
	 * 
	 * @see com.amazonaws.auth.BasicAWSCredentials
	 * @see com.amazonaws.auth.ProfilesConfigFile
	 * @see com.amazonaws.ClientConfiguration
	 */
	public static void init() throws Exception {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential
		 * profile by reading from the credentials file located at
		 * (/Users/Aakash/.aws/credentials).
		 */
		AWSCredentials credentails = null;

		try {
			credentails = new BasicAWSCredentials(accessKey, secretKey);
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct " + "location (/Users/Aakash/.aws/credentials), and is in valid format.",
					e);
		}
		dynamoDB = new AmazonDynamoDBClient(credentails);
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);
		System.out.println("Connection to DynamoDB sucessful !");
	}

	/**
	 * Put data to database
	 * 
	 * @param tableName
	 *            - table name
	 * @param value
	 *            - value to put in table
	 */
	public static void putDataIntoTable(String tableName, String value) {
		Map<String, AttributeValue> item = newItem("TaskId", value);
		dynamoDB.putItem(tableName, item);
	}

	/**
	 * Getting data from table of DynamoDB
	 * 
	 * @param tableName
	 *            - table name
	 * @param value
	 *            - value to put in table
	 * @return data of DynamoDB
	 */
	public static Map<String, AttributeValue> getDataFromTable(String tableName, String value) {
		GetItemResult getItemResult = null;
		try {
			Map<String, AttributeValue> item = newItem("TaskId", value);
			getItemResult = dynamoDB.getItem(tableName, item);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return getItemResult.getItem();
	}

	private static Map<String, AttributeValue> newItem(String key, String value) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put(key, new AttributeValue(value));

		return item;
	}
}
