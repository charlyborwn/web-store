package pti.test.server.config.startup;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.stereotype.Component;

@Component
public class ServletCustomizer implements EmbeddedServletContainerCustomizer {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("woff","application/x-font-woff");
        mappings.add("eot","application/x-font-eot");
        mappings.add("woff2","application/x-font-woff2");
        mappings.add("ttf","application/x-font-ttf");

        container.setMimeMappings(mappings);
    }
}