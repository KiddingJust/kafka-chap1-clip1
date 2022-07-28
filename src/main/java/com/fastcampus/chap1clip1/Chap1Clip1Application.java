package com.fastcampus.chap1clip1;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.Map;

@SpringBootApplication
public class Chap1Clip1Application {

    public static void main(String[] args) {
        SpringApplication.run(Chap1Clip1Application.class, args);
    }

//    @Bean
//    public ApplicationRunner runner(KafkaTemplate<String, String> kafkaTemplate){
//        return args -> {
////            kafkaTemplate.send("quickstart-events", "hello-world-message");
//        };
//    }
    @Bean
    public ApplicationRunner runner(AdminClient adminClient){
        return args -> {
            Map<String, TopicListing> topics = adminClient.listTopics().namesToListings().get();
            for(String topicName : topics.keySet()){
                TopicListing topicListing = topics.get(topicName);  //topicListing 객체 리턴함.
                System.out.println(topicListing);

                //토픽 세부 정보 보기
                Map<String, TopicDescription> description = adminClient.describeTopics(Collections.singleton(topicName)).all().get();
                System.out.println(description);    //토픽 이름과 파티션 정보 , replica 정보까지 모두 확인 가능

                //토픽 삭제 - consumer_offsets 만 빼고 모두 지워짐. 이는 모든 토픽의 오프셋 정보가 담기는 것.
                if(!topicListing.isInternal()){
                    adminClient.deleteTopics(Collections.singleton(topicName));
                }
            }
        };
    }
}
