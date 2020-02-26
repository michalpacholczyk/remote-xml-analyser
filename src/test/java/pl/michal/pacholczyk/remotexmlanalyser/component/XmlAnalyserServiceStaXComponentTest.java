package pl.michal.pacholczyk.remotexmlanalyser.component;

import com.google.common.net.MediaType;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import pl.michal.pacholczyk.remotexmlanalyser.TestBase;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.RequestDto;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.XmlAnalysationOverviewDto;
import pl.michal.pacholczyk.remotexmlanalyser.service.XmlAnalyserService;

import java.io.IOException;

import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.BinaryBody.binary;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.OK_200;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlAnalyserServiceStaXComponentTest extends TestBase {

    private static final int MOCK_PORT = 1080;
    private static final String LOCALHOST = "127.0.0.1";
    private static final String XML_FILE_URi = "/" + MOCK_XML_FILE;
    private static final String XML_FILE_URL = "http://" + LOCALHOST + ":" + MOCK_PORT + XML_FILE_URi;

    private static ClientAndServer mockServer;

    @Autowired
    private XmlAnalyserService serviceUnderTest;

    @BeforeClass
    public static void startServer() {
        mockServer = startClientAndServer(1080);
    }

    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    public void analyzeUsdExchangeRatesBetweenDatesEndToEndTest() throws Exception {

        RequestDto mockDto = RequestDto.builder().url(XML_FILE_URL).build();

        getRemoteFile_mockServerResponse();

        XmlAnalysationOverviewDto overviewDto = serviceUnderTest.analyseRemoteXml(mockDto);

        assertServiceRespond(overviewDto);
    }

    private void getRemoteFile_mockServerResponse() {

        byte[] pdfBytes = new byte[0];
        try {
            pdfBytes = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream(MOCK_XML_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        new MockServerClient(LOCALHOST, MOCK_PORT).when(
                request()
                        .withPath(XML_FILE_URi)
        ).respond(
                response()
                        .withStatusCode(OK_200.code())
                        .withReasonPhrase(OK_200.reasonPhrase())
                        .withHeaders(
                                header(CONTENT_TYPE.toString(), MediaType.XML_UTF_8.toString())
                        )
                        .withBody(binary(pdfBytes))
        );
    }

    @Test
    public void analyseRemoteXml_shouldThrowResponseStatusException() throws Exception {

        assertThrows(ResponseStatusException.class, () -> {
            serviceUnderTest.analyseRemoteXml(produceInvalidMockDto());
        });
    }
}
