package s3event;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import s3event.config.AmazonConfig;
import s3event.model.Employee;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler for requests to Lambda function.
 */
public class AppS3 {
    private AmazonS3 s3= AmazonConfig.getAmazonS3Client();
    private AmazonSQS sqs=AmazonConfig.getAmazonSQSClient();
    private AmazonDynamoDB dynamoDB=AmazonConfig.getAmazonDynamodDBClient();

    private final ObjectMapper objectMapper=new ObjectMapper();
    public void handleRequest(final S3Event event) {

         event.getRecords().forEach(record->{
                String bucketName=record.getS3().getBucket().getName();
                String key=record.getS3().getObject().getKey();
             S3ObjectInputStream objectContent = s3.getObject(bucketName, key)
                     .getObjectContent();

             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(objectContent));
            List<Employee> employeeList= bufferedReader.lines().map(x->{
                 String[] split = x.split(",");
                Employee emp=new Employee(Integer.parseInt(split[0]),split[1],Integer.parseInt(split[2]));
                 return emp;
             }).collect(Collectors.toList());
            System.out.println("=========================================================");
            employeeList.forEach(emp->System.out.println(emp));



            employeeList.forEach(emp->{
                SendMessageRequest request=new SendMessageRequest();
                request.setQueueUrl("https://sqs.ap-south-1.amazonaws.com/796731763832/Employee-sqs");
                try {
                    request.setMessageBody(objectMapper.writeValueAsString(emp));
                    sqs.sendMessage(request);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            });



         });


    }
}