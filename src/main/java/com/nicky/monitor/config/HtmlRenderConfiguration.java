package com.nicky.monitor.config;

import com.nicky.monitor.constants.HtmlTag;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HtmlRenderConfiguration {
    @Bean
    public Renderer<String> htmlRender(){
        return new ComponentRenderer<>(rawText -> {
            rawText = rawText.replaceAll("\n", HtmlTag.HTML_BREAK);
            return new Html("<div><pre>" + rawText + "</pre></div>");
        });
    }
}
