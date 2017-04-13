package cz.cvut.fel.aos.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

/**
 * Konfiguracne beany pre aplikaciu
 */
@Configuration
public class WebConfigPage extends WebMvcConfigurerAdapter {

    //TODO dorobit logger a zmenit hardcoded values

    /**
     * Pri strankovani potrebujeme Jacksonu povedat aby serializoval aj objekt typu Page
     *
     * @return serializovany objekt Page (Jackson - Json)
     */
    @Bean
    public Module springDataPageModule() {
        return new SimpleModule().addSerializer(Page.class, new JsonSerializer<Page>() {
            @Override
            public void serialize(Page value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                if (value != null) {
                    System.out.println("===============================================================");
                    System.out.println(value.getClass());
                    System.out.println(value.toString());
                    gen.writeStartObject();
                    gen.writeNumberField("totalElements", value.getTotalElements());

                    if (value.toString().contains("Destination")) {
                        gen.writeFieldName("destination");
                    } else if (value.toString().contains("Flight")) {
                        gen.writeFieldName("flight");
                    } else if (value.toString().contains("Reservation")) {
                        gen.writeFieldName("reservation");
                    } else {
                        gen.writeFieldName("content");
                    }

                    serializers.defaultSerializeValue(value.getContent(), gen);
                    gen.writeEndObject();
                }
            }
        });
    }


}