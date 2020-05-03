package com.mszewczyk.cp.producer;

import com.mszewczyk.cp.model.Command;
import com.mszewczyk.cp.service.commands.CommandProducer;
import lombok.extern.slf4j.Slf4j;

// TODO: implement, use external model instead of the internal one
@Slf4j
public class KafkaEventsProducer implements CommandProducer {
    @Override
    public void produce(Command command) {
        log.info("Event produced. [command={}]", command);
    }
}
