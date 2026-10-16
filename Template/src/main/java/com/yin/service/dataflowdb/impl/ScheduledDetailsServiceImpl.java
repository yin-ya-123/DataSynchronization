package com.yin.service.dataflowdb.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.mapper.dataflowdb.ScheduledDetailsMapper;
import com.yin.pojo.dataflowdb.ScheduledDetails;
import com.yin.service.dataflowdb.ScheduledDetailsService;
import org.springframework.stereotype.Service;

@Service
@DS("master")
public class ScheduledDetailsServiceImpl extends ServiceImpl<ScheduledDetailsMapper,ScheduledDetails>
        implements ScheduledDetailsService {
}
