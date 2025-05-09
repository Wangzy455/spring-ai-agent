package com.wzy.springaiagent.dashboard;

import com.wzy.springaiagent.common.constants.Tags;
import com.wzy.springaiagent.mapper.IDashboardMapper;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 仪表盘服务
 */
@Service
@Slf4j
public class DashboardServiceImpl implements IDashboardService{

    @Resource
    private IDashboardMapper dashboardMapper;


    /**
     * 获取柱状图的数据
     * @return
     */
    @Override
    public Map<String,Integer> getZData(){
       Map<String,Integer> data = new HashMap<>();
       data.put(Tags.FILE_TAG,dashboardMapper.getZData(Tags.FILE_TAG));
       data.put(Tags.KNOWLEDGE_TAG,dashboardMapper.getZData(Tags.KNOWLEDGE_TAG));
       data.put(Tags.IMG_TAG,dashboardMapper.getZData(Tags.IMG_TAG));
       data.put(Tags.HUISHOU_TAG,dashboardMapper.getZData(Tags.HUISHOU_TAG));
       data.put(Tags.UNKNOWN_TAG,dashboardMapper.getZData(Tags.UNKNOWN_TAG));
       data.put(Tags.ZIP_TAG,dashboardMapper.getZData(Tags.ZIP_TAG));
       data.put(Tags.VIDEO_TAG,dashboardMapper.getZData(Tags.VIDEO_TAG));

       return data;
    }

    /**
     * 获取饼状图数据
     * @return
     */
    @Override
    public Map<String, BigDecimal> getPieData() {
        Map<String, BigDecimal> data = new HashMap<>();

        BigDecimal all = BigDecimal.valueOf(dashboardMapper.getAll());
        System.out.println("all = " + all);

        BigDecimal fileData = BigDecimal.valueOf(dashboardMapper.getZData(Tags.FILE_TAG));
        BigDecimal imgData = BigDecimal.valueOf(dashboardMapper.getZData(Tags.IMG_TAG));
        BigDecimal videoData = BigDecimal.valueOf(dashboardMapper.getZData(Tags.VIDEO_TAG));
        BigDecimal zipData = BigDecimal.valueOf(dashboardMapper.getZData(Tags.ZIP_TAG));
        BigDecimal huishouData = BigDecimal.valueOf(dashboardMapper.getZData(Tags.HUISHOU_TAG));
        BigDecimal unknownData = BigDecimal.valueOf(dashboardMapper.getZData(Tags.UNKNOWN_TAG));
        BigDecimal knowledgeData = BigDecimal.valueOf(dashboardMapper.getZData(Tags.KNOWLEDGE_TAG));

        int scale = 4; // 保留4位小数
        RoundingMode roundingMode = RoundingMode.HALF_UP;

        if (all.compareTo(BigDecimal.ZERO) > 0) {
            data.put(Tags.FILE_TAG, fileData.divide(all, scale, roundingMode));
            data.put(Tags.IMG_TAG, imgData.divide(all, scale, roundingMode));
            data.put(Tags.VIDEO_TAG, videoData.divide(all, scale, roundingMode));
            data.put(Tags.ZIP_TAG, zipData.divide(all, scale, roundingMode));
            data.put(Tags.HUISHOU_TAG, huishouData.divide(all, scale, roundingMode));
            data.put(Tags.UNKNOWN_TAG, unknownData.divide(all, scale, roundingMode));
            data.put(Tags.KNOWLEDGE_TAG, knowledgeData.divide(all, scale, roundingMode));
        } else {
            // 若总值为0，则全部设为0
            data.put(Tags.FILE_TAG, BigDecimal.ZERO);
            data.put(Tags.IMG_TAG, BigDecimal.ZERO);
            data.put(Tags.VIDEO_TAG, BigDecimal.ZERO);
            data.put(Tags.ZIP_TAG, BigDecimal.ZERO);
            data.put(Tags.HUISHOU_TAG, BigDecimal.ZERO);
            data.put(Tags.UNKNOWN_TAG, BigDecimal.ZERO);
            data.put(Tags.KNOWLEDGE_TAG, BigDecimal.ZERO);
        }

        return data;
    }


}
