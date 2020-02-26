package pl.michal.pacholczyk.remotexmlanalyser.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class XmlAnalysationDetailsDto {

    private LocalDateTime firstPost;

    private LocalDateTime lastPost;

    private Long totalPosts;

    private Long totalAcceptedPosts;

    private BigDecimal avgScore;
}
