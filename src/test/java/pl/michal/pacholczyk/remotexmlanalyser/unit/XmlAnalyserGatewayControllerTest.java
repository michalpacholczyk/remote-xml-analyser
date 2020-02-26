package pl.michal.pacholczyk.remotexmlanalyser.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.michal.pacholczyk.remotexmlanalyser.TestBase;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.RequestDto;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.XmlAnalysationDetailsDto;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.XmlAnalysationOverviewDto;
import pl.michal.pacholczyk.remotexmlanalyser.controller.XmlAnalyserGatewayController;
import pl.michal.pacholczyk.remotexmlanalyser.service.XmlAnalyserService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(XmlAnalyserGatewayController.class)
public class XmlAnalyserGatewayControllerTest extends TestBase {

    private static final String VALID_URL = "http://localhost:8080/mockxml.xml";
    private static final String INVALID_URL = "http://localhost:8080/mockxml.";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private XmlAnalyserService service;

    @Test
    public void analyseRemoteXml_shouldReturnStatusOK() throws Exception {

        RequestDto mockDto = RequestDto.builder().url(VALID_URL).build();

        LocalDateTime analyseDateTime = LocalDateTime.now();

        XmlAnalysationDetailsDto details = XmlAnalysationDetailsDto.builder()
                .totalAcceptedPosts(10L)
                .totalPosts(100L)
                .build();

        XmlAnalysationOverviewDto mockServiceResponse = XmlAnalysationOverviewDto.builder()
                .processingTimeInMs(1000L)
                .analyseDate(analyseDateTime)
                .details(details)
                .build();

        given(service.analyseRemoteXml(any(RequestDto.class))).willReturn(mockServiceResponse);

        mvc.perform(post("/analyze")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.processingTimeInMs").value((1000L)))
                .andExpect(jsonPath("$.details.totalPosts").value((100L)))
                .andExpect(jsonPath("$.details.totalAcceptedPosts").value((10L)));
    }

    @Test
    public void analyseRemoteXml_shouldReturnStatus400() throws Exception {

        RequestDto mockDto = RequestDto.builder().url(INVALID_URL).build();

        mvc.perform(post("/analyze")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockDto)))
                .andExpect(status().is4xxClientError());
    }

}
