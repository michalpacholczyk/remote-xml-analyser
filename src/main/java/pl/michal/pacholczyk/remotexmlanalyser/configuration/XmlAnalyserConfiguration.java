package pl.michal.pacholczyk.remotexmlanalyser.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.stream.XMLInputFactory;

@Configuration
public class XmlAnalyserConfiguration {

    @Bean
    public XMLInputFactory getXmlInputFactory() {
        return XMLInputFactory.newInstance();
    }

}
