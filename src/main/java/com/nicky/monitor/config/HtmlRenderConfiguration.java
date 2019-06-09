package com.nicky.monitor.config;

import com.nicky.monitor.constants.HtmlTag;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.function.SerializableFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HtmlRenderConfiguration {
    @Bean
    public SerializableFunction<String, Component> htmlSerializer(){
        return text -> {
            if (text == null){
                text = "";
            }
            return new Html("<div><pre>" + text.replaceAll(System.lineSeparator(), HtmlTag.HTML_BREAK) + "</pre></div>");
        };
    }
}
