package com.example.demo;

import com.microsoft.azure.spring.integration.core.AzureHeaders;
import com.microsoft.azure.spring.integration.core.api.Checkpointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.PostConstruct;

@EnableBinding(Binding.class)
@SpringBootApplication
public class DemoApplication {

    @Autowired
    Binding binding;

	public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(DemoApplication.class, args);
    }

    @PostConstruct
    public void test() {


    }

    @StreamListener(Binding.INPUT)
    public void handleMessage(String message, @Header(AzureHeaders.CHECKPOINTER) Checkpointer checkpointer) {
        System.out.println(String.format("New message received: '%s'", message));
        checkpointer.success().handle((r, ex) -> {
            if (ex == null) {
                System.out.println(String.format("Message '%s' successfully checkpointed", message));
            }
            return null;
        });
    }

}

interface Binding {
    String INPUT = "input";
    String OUTPUT = "output";

    @Input(INPUT)
    SubscribableChannel input();
    @Output
    MessageChannel output();
}

class Person {
    private String name;

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}



