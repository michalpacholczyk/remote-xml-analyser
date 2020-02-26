package pl.michal.pacholczyk.remotexmlanalyser.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.RequestDto;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.XmlAnalysationOverviewDto;
import pl.michal.pacholczyk.remotexmlanalyser.service.XmlAnalyserService;

import javax.validation.Valid;

@RestController
@Validated
@AllArgsConstructor
public class XmlAnalyserGatewayController {

    private XmlAnalyserService analyserService;

    @PostMapping("/analyze")
    public XmlAnalysationOverviewDto analyseRemoteXml(@Valid @RequestBody RequestDto requestDto) {
        return analyserService.analyseRemoteXml(requestDto);
    }
}
