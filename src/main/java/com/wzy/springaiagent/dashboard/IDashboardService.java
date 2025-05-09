package com.wzy.springaiagent.dashboard;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IDashboardService {

    Map<String,Integer> getZData();

    Map<String, BigDecimal> getPieData();

}
