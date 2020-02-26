package pl.michal.pacholczyk.remotexmlanalyser.service.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.XmlAnalysationDetailsDto;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.XmlAnalysationOverviewDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import javax.xml.stream.events.Attribute;

@Getter
@NoArgsConstructor
public class AnalyzeProcessor {

    private static final String ATTRIBUTE_CREATION_DATE = "CreationDate";
    private static final String ATTRIBUTE_ACCEPTED_ANSWER_ID = "AcceptedAnswerId";
    private static final String ATTRIBUTE_SCORE = "Score";

    private LocalDateTime processingDateTime;
    private long startTime = 0;

    private String firstPostDateTime;
    private String lastPostDateTime;

    private long postCounter = 0;
    private long acceptedAnswerCounter = 0;
    private long postsScore = 0;

    public static AnalyzeProcessor startProcessing() {
        AnalyzeProcessor context = new AnalyzeProcessor();
        context.startTime = System.currentTimeMillis();
        context.processingDateTime = LocalDateTime.now();
        return context;
    }

    public void incrementPostCounter() {
        postCounter++;
    }

    private void incrementAcceptedPostCounter() {
        acceptedAnswerCounter++;
    }

    private void incrementPostsScore(String value) {
        attributeConsumer(value, v -> {
            postsScore += Long.parseLong(value);
        });
    }

    private boolean isFirstDateAssigned() {
        return firstPostDateTime != null;
    }

    private void assignLastPostDate(String value) {
        attributeConsumer(value, v -> {
            this.lastPostDateTime = value;
        });
    }

    private void assignFirstPostDate(String value) {
        attributeConsumer(value, v -> {
            this.firstPostDateTime = value;
        });
    }

    private void attributeConsumer(String t, Consumer<String> consumer) {
        consumer.accept(t);
    }

    public void processAttribute(Attribute attribute) {
        String attributeName = attribute.getName().toString();
        switch (attributeName) {
            case ATTRIBUTE_ACCEPTED_ANSWER_ID:
                incrementAcceptedPostCounter();
                break;
            case ATTRIBUTE_SCORE:
                incrementPostsScore(attribute.getValue());
                break;
            case ATTRIBUTE_CREATION_DATE:
                if (!isFirstDateAssigned()) {
                    assignFirstPostDate(attribute.getValue());
                }
                assignLastPostDate(attribute.getValue());
                break;
        }
    }

    public XmlAnalysationOverviewDto asResult() {

        XmlAnalysationDetailsDto details = XmlAnalysationDetailsDto.builder()
                .firstPost(LocalDateTime.parse(firstPostDateTime))
                .lastPost(LocalDateTime.parse(lastPostDateTime))
                .totalPosts(postCounter)
                .totalAcceptedPosts(acceptedAnswerCounter)
                .avgScore(calulateAvgScore(postCounter, postsScore))
                .build();

        return XmlAnalysationOverviewDto.builder()
                .analyseDate(processingDateTime)
                .processingTimeInMs(System.currentTimeMillis() - startTime)
                .details(details)
                .build();
    }

    private BigDecimal calulateAvgScore(long postCounter, long postsScore) {
        return postCounter != 0 ? new BigDecimal(postsScore).divide(new BigDecimal(postCounter), 3, RoundingMode.CEILING) : new BigDecimal(0);
    }

}
