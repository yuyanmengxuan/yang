package com.wei.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wei.mapper.TradeMapper;
import com.wei.pojo.Trade;
import org.springframework.stereotype.Service;

@Service
public class TradeServiceImpl extends ServiceImpl<TradeMapper, Trade > implements TradeService  {
}
