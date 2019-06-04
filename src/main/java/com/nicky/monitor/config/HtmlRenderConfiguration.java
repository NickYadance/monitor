package com.nicky.monitor.config;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.function.SerializableFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HtmlRenderConfiguration {
    @Bean
    public SerializableFunction<String, Component> htmlSerializer(){
        return text -> new Html("<div><pre>" + text + "</pre></div>");
    }
}
