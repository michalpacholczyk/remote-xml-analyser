package pl.michal.pacholczyk.remotexmlanalyser.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.server.ResponseStatusException;
import pl.michal.pacholczyk.remotexmlanalyser.TestBase;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.RequestDto;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.XmlAnalysationOverviewDto;
import pl.michal.pacholczyk.remotexmlanalyser.service.impl.XmlAnalyserServiceStaX;

import javax.xml.stream.XMLInputFactory;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class XmlAnalyserServiceStaXTest extends TestBase {

    @Spy
    private XMLInputFactory factory = XMLInputFactory.newInstance();

    @InjectMocks
    private XmlAnalyserServiceStaX serviceUnderTest;

    @Test
    public void analyseRemoteXml_shouldReturnAnalyzeDto() throws Exception {

        Resource resource = new ClassPathResource(MOCK_XML_FILE);
        RequestDto mockDto = RequestDto.builder().url("file:///" + resource.getFile().getPath()).build();

        XmlAnalysationOverviewDto overviewDto = serviceUnderTest.analyseRemoteXml(mockDto);

        assertServiceRespond(overviewDto);
    }

    @Test
    public void analyseRemoteXml_shouldThrowResponseStatusException() throws Exception {

        assertThrows(ResponseStatusException.class, () -> {
            serviceUnderTest.analyseRemoteXml(produceInvalidMockDto());
        });
    }
}
