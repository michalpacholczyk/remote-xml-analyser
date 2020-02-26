package pl.michal.pacholczyk.remotexmlanalyser;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.RequestDto;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.XmlAnalysationOverviewDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public abstract class TestBase {

    protected static final String MOCK_XML_FILE = "mockxml.xml";
    protected static final String BAD_URL = "some bad url";

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected RequestDto produceInvalidMockDto() {
        return RequestDto.builder().url(BAD_URL).build();
    }

    protected void assertServiceRespond(XmlAnalysationOverviewDto overviewDto) {
        assertThat(overviewDto).isNotNull();
        assertThat(overviewDto.getDetails()).isNotNull();
        assertThat(overviewDto.getDetails().getTotalPosts()).isEqualTo(6L);
        assertThat(overviewDto.getDetails().getTotalAcceptedPosts()).isEqualTo(1L);
        assertThat(overviewDto.getDetails().getFirstPost()).isEqualTo(LocalDateTime.parse("2015-07-14T18:39:27.757"));
        assertThat(overviewDto.getDetails().getLastPost()).isEqualTo(LocalDateTime.parse("2015-07-14T20:06:44.950"));
        assertThat(overviewDto.getDetails().getAvgScore()).isEqualTo(new BigDecimal("2.000"));
    }
}
