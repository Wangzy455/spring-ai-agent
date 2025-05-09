package com.wzy.springaiagent.controller;

import com.wzy.springaiagent.common.constants.Response;
import com.wzy.springaiagent.dashboard.IDashboardService;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/dashboard/")
@Slf4j
public class DashboardController {

    @Resource
    private IDashboardService dashboardService;
    /**
     * 获取柱状图数据
     * @return
     */
    @RequestMapping(value="zdata",method = RequestMethod.POST)
    public Response<Map<String,Integer>> getZData(){
        Map<String, Integer> zData = dashboardService.getZData();
        log.info("获取柱状图数据:{}",zData);
        return Response.<Map<String,Integer>>builder().code("200").info("数据查询成功").data(zData).build();
    }
    /**
     * 获取饼状图数据
     * @return
     */
    @RequestMapping(value="piedata",method = RequestMethod.POST)
    public Response<Map<String,BigDecimal>> getPieData(){
        Map<String, BigDecimal> pieData = dashboardService.getPieData();
        log.info("获取饼状图数据:{}",pieData);
        return Response.<Map<String,BigDecimal>>builder().code("200").info("数据查询成功").data(pieData).build();
    }
}
