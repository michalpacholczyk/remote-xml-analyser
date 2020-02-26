package pl.michal.pacholczyk.remotexmlanalyser.service;

import pl.michal.pacholczyk.remotexmlanalyser.common.dto.RequestDto;
import pl.michal.pacholczyk.remotexmlanalyser.common.dto.XmlAnalysationOverviewDto;

public interface XmlAnalyserService {

    XmlAnalysationOverviewDto analyseRemoteXml(RequestDto requestDto);

}
