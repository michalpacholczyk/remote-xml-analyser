package pl.michal.pacholczyk.remotexmlanalyser.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.RequestDto;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.XmlAnalysationOverviewDto;
import pl.michal.pacholczyk.remotexmlanalyser.service.XmlAnalyserService;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

@Service
@AllArgsConstructor
public class XmlAnalyserServiceStaX implements XmlAnalyserService {

    private static final String MARKER_ROW = "row";

    private XMLInputFactory xmlInputFactory;

    @Override
    public XmlAnalysationOverviewDto analyseRemoteXml(RequestDto request) {

        InputStream in = null;
        XMLEventReader reader = null;

        AnalyzeProcessor context = AnalyzeProcessor.startProcessing();

        try {

            URL url = new URL(request.getUrl());
            in = url.openStream();
            reader = xmlInputFactory.createXMLEventReader(in);

            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();

                if (nextEvent.isStartElement()) {

                    StartElement startElement = nextEvent.asStartElement();
                    String markerName = startElement.getName().getLocalPart();

                    if (!MARKER_ROW.equalsIgnoreCase(markerName)) {
                        continue;
                    }

                    Iterator<Attribute> attributes = startElement.getAttributes();
                    while (attributes.hasNext()) {
                        Attribute attribute = attributes.next();
                        context.processAttribute(attribute);
                    }
                    context.incrementPostCounter();
                }
            }

        } catch (IOException | XMLStreamException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while .xml processing, ensure given URL is valid!", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occured!", e);
        } finally {
            closeInputs(in, reader);
        }

        return context.asResult();
    }

    private void closeInputs(InputStream in, XMLEventReader reader) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while .xml processing, ensure given URL is valid!", e);
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while .xml processing, ensure given URL is valid!", e);
            }
        }
    }
}
