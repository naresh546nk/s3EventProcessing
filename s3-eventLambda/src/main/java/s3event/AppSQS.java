package s3event;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import s3event.config.AmazonConfig;
import s3event.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppSQS {

    private final AmazonDynamoDB dyaDynamoDB=AmazonConfig.getAmazonDynamodDBClient();
    private final DynamoDBMapper mapper=new DynamoDBMapper(dyaDynamoDB);
    private Gson gson=new Gson();
    public void handleRequest(final SQSEvent event) {
        List<Employee> empLst= event.getRecords().stream().map(record->{
            String message = record.getBody();
            return gson.fromJson(message, Employee.class);
        }).collect(Collectors.toList());
        mapper.batchSave(empLst);
        System.out.println("Total employee saved in data base: "+empLst.size());
    }
}