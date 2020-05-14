package com.mszewczyk.cp.producer;

public interface ProducerConfiguration {
    String brokers();

    String producerId();

    String topic();
}
