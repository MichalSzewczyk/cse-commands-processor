package com.mszewczyk.cp.configuration.consul;

import com.mszewczyk.cp.controller.configuration.ControllerConfiguration;

//TODO: Implement consul based configuration source.
public class ConsulConfiguration implements ControllerConfiguration {
    @Override
    public String addToGroupPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String removeFromGroupPath() {
        throw new UnsupportedOperationException();
    }
}
