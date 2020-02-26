package pl.michal.pacholczyk.remotexmlanalyser.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class XmlAnalysationOverviewDto {

    private LocalDateTime analyseDate;

    private Long processingTimeInMs;

    private XmlAnalysationDetailsDto details;

}
