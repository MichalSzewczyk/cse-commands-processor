package com.mszewczyk.cp.producer;

import com.mszewczyk.cp.service.commands.CommandProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.function.Function;

@Slf4j
public class KafkaEventsProducer<T> implements CommandProducer<T> {
    private final Producer<String, String> producer;
    private final Function<T, String> toStringMapper;
    private final Function<T, String> keyExtractor;
    private final String topic;

    public KafkaEventsProducer(Function<T, String> toStringMapper, Function<T, String> keyExtractor, ProducerConfiguration configuration) {
        this.toStringMapper = toStringMapper;
        this.keyExtractor = keyExtractor;
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.brokers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, configuration.producerId());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
        topic = configuration.topic();
    }

    @Override
    public void produce(T value) {
        log.info("Event produced. [value={}]", value);
        ProducerRecord<String, String> producerRecord = getProducerRecord(value, keyExtractor.apply(value));
        producer.send(producerRecord);
    }

    private ProducerRecord<String, String> getProducerRecord(T value, String key) {
        String serializedMessage = toStringMapper.apply(value);
        return new ProducerRecord<>(topic, key, serializedMessage);
    }
}
