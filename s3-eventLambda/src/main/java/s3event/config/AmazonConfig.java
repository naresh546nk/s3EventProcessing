package s3event.config;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

public class AmazonConfig {
    public static  AmazonS3 getAmazonS3Client(){
      return  AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                //.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey,secretKey)))
                .build();

    }

    public static AmazonDynamoDB getAmazonDynamodDBClient(){
        return  AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.AP_SOUTH_1)
                //.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey,secretKey)))
                .build();

    }

    public  static AmazonSQS getAmazonSQSClient(){
        return AmazonSQSClientBuilder
                .standard()
                .withRegion(Regions.AP_SOUTH_1)
               // .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey,secretKey)))
                .build();
    }
}
