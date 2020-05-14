package com.mszewczyk.cp.configuration;

import com.mszewczyk.cp.controller.ControllerConfiguration;
import com.mszewczyk.cp.persistance.DatabaseConfiguration;
import com.mszewczyk.cp.producer.ProducerConfiguration;

public interface ApplicationConfiguration extends ControllerConfiguration,
        DatabaseConfiguration, ProducerConfiguration {
}
