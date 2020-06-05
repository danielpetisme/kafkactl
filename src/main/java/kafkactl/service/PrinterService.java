package kafkactl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import kafkactl.config.OutputFormat;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;

@ApplicationScoped
public class PrinterService {

    ObjectMapper json = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
    ObjectMapper yaml = new ObjectMapper(new YAMLFactory()).setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);


    public void print(Object o, OutputFormat format) throws IOException {
        String output;
        if (format == OutputFormat.json) {
            output = json.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } else {
            output = yaml.writeValueAsString(o);
        }
        System.out.println(output);
    }

}
