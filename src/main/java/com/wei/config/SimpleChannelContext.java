/*
 * Copyright (C) 2011-2018 ShenZhen iBOXPAY Information Technology Co.,Ltd.
 *
 * All right reserved.
 *
 * This software is the confidential and proprietary
 * information of iBoxPay Company of China.
 * ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only
 * in accordance with the terms of the contract agreement
 * you entered into with iBoxpay inc.
 *
 *//*


package com.wei.config;

import static com.iboxpay.nf.payment.gateway.channel.enums.StatusCode.CHANNEL_NOT_SUPPORT_EVENT;
import static com.iboxpay.nf.payment.gateway.channel.enums.StatusCode.MISSING_ALL_REQUIRED_PARAMETER;
import static com.iboxpay.nf.payment.gateway.channel.enums.StatusCode.NOT_SUPPORT_CHANNEL;
import static com.iboxpay.nf.payment.gateway.channel.enums.StatusCode.NOT_SUPPORT_CHANNEL_EVENT;
import static com.iboxpay.nf.payment.gateway.channel.enums.StatusCode.SYSTEM_ERROR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.iboxpay.nf.payment.base.dto.GenericRequestDto;
import com.iboxpay.nf.payment.gateway.channel.enums.Channel;
import com.iboxpay.nf.payment.gateway.channel.enums.ChannelEvent;
import com.iboxpay.nf.payment.gateway.channel.enums.ProcessType;
import com.iboxpay.nf.payment.gateway.channel.exception.ChannelException;
import com.iboxpay.nf.payment.gateway.channel.log.ProcessTracker;
import com.iboxpay.nf.payment.gateway.channel.mapper.ChannelEventMapping;
import com.iboxpay.nf.payment.gateway.channel.mapper.ChannelMapping;
import com.iboxpay.nf.payment.gateway.common.util.StringUtils;
import com.iboxpay.nf.payment.gateway.constans.ChannelConstants;

*/
/**
 * The class SimpleChannelContext.
 *
 * Description: 通道上下文，提供通道初始化、通道路由、通道执行、线程缓存功能 
 *
 * @author: 
 * @since: 2018年7月30日
 *
 *//*

@Component
public class SimpleChannelContext implements ChannelContext, ChannelProperty, ProcessTracker {

  private static final Logger logger = LoggerFactory.getLogger(SimpleChannelContext.class);

  private ApplicationContext ctx;

  */
/**
   * 保存通道执行过程的线程上线文信息
   *//*

  private SimpleLocalContext simpleLocalContext = new SimpleLocalContext();

  private Map<Channel, Map<ChannelEvent, ChannelTarget>> eventTargetMap = new HashMap<>();

  @PostConstruct
  @Override
  public void init() {
    Map<String, Object> tempChannelMap = ctx.getBeansWithAnnotation(ChannelMapping.class);
    Channel channel;
    Method[] methods;
    Map<ChannelEvent, ChannelTarget> channelTargetMap;

    // 加载所有通道
    for (Object channelObj : tempChannelMap.values()) {
      channel = channelObj.getClass().getAnnotation(ChannelMapping.class).code();
      if (eventTargetMap.containsKey(channel)) {
        channelTargetMap = eventTargetMap.get(channel);
      } else {
        channelTargetMap = new HashMap<>();
        eventTargetMap.put(channel, channelTargetMap);
      }

      // 加载所有通道通道事件
      methods = channelObj.getClass().getDeclaredMethods();
      if (methods != null && methods.length > 0) {
        for (Method m : methods) {
          ChannelEventMapping mapping = m.getDeclaredAnnotation(ChannelEventMapping.class);
          if (mapping != null) {
            if (!channelTargetMap.containsKey(mapping.value())) {
              logger.info("Channel {} binding event {} ", channel.getCode(),
                  mapping.value().getCode());
              m.setAccessible(true);
              channelTargetMap.put(mapping.value(), new ChannelTarget(channelObj, m));
            } else {
              throw new ChannelException("Channel [%s] can't repeat binding event [%s]",
                  channel.getCode(), mapping.value().getCode());
            }
          }
        }
      }
    }
    tempChannelMap.clear();
  }

  @PreDestroy
  @Override
  public void destroy() {
    eventTargetMap.clear();
    eventTargetMap = null;
  }

  @Override
  public <T> T route(Channel channel, ChannelEvent event, GenericRequestDto req, Class<T> requireType) throws InvocationTargetException, IllegalAccessException {
    if (req == null) {
      throw new ChannelException(MISSING_ALL_REQUIRED_PARAMETER);
    }

    // 获取交易通道,当前仅支持小程序
    if (null == channel) {
      String channleName = req.getStringData(ChannelConstants.CHANNEL_KEY);
      if (StringUtils.isBlank(channleName)) {
        throw new ChannelException(NOT_SUPPORT_CHANNEL, channleName);
      }
      
      Optional<Channel> channelOptional = Channel.of(channleName);
      if (!channelOptional.isPresent()) {
        throw new ChannelException(NOT_SUPPORT_CHANNEL, channleName);
      }
      channel = channelOptional.get();
    }

    // 获取交易事件（下单/回调/查询）
    if (null == event) {
      String eventName = req.getStringData(ChannelConstants.EVENT_KEY);
      Optional<ChannelEvent> channelEventOptional = ChannelEvent.of(eventName);
      if (!channelEventOptional.isPresent()) {
        throw new ChannelException(NOT_SUPPORT_CHANNEL_EVENT, eventName);
      }
      event = channelEventOptional.get();
    }

    // 找到交易目标执行方法
    ChannelTarget target = this.getMappingTarget(channel, event);
    if (target == null) {
      trackingError(channel, event, getProcessType(), "target_error",
          "Can't find the target object");
      throw new ChannelException(SYSTEM_ERROR);
    }
    setChannel(channel);
    setChannelEvent(event);
    return (T) target.execute(req);
  }

  */
/**
   * 获取通道执行对象
   * 
   * @param channel
   * @param event
   * @return
   * @throws ChannelException
   *//*

  private ChannelTarget getMappingTarget(Channel channel, ChannelEvent event)
      throws ChannelException {
    if (!eventTargetMap.containsKey(channel)) {
      trackingError(channel, event, getProcessType(), "target_mapping",
          String.format(
              "Can't not find the relational mapping events according to the channel [%s]",
              channel.getCode()));
      throw new ChannelException(SYSTEM_ERROR);
    }
    Map<ChannelEvent, ChannelTarget> ccMap = eventTargetMap.get(channel);
    if (!ccMap.containsKey(event)) {
      trackingError(channel, event, getProcessType(), "target_mapping", String.format(
          "Can't not find the relational mapping channel target according to the channel event [%s]",
          event.getCode()));
      throw new ChannelException(CHANNEL_NOT_SUPPORT_EVENT, channel.getCode(), event.getCode());
    }
    return ccMap.get(event);
  }

  @Override
  public void setApplicationContext(ApplicationContext ctx) throws BeansException {
    //    if (ctx.getParent() == null) {}
    this.ctx = ctx;
  }

  @Override
  public Channel getChannel() {
    return simpleLocalContext.getProperty("currentchannel", Channel.class).orElse(null);
  }

  @Override
  public ProcessType getProcessType() {
    return ProcessType.ROUTE;
  }

  @Override
  public void setChannel(Channel channel) {
    simpleLocalContext.setProperty("currentchannel", channel);
  }

  @Override
  public ChannelEvent getChannelEvent() {
    return simpleLocalContext.getProperty("currentchannelevent", ChannelEvent.class).orElse(null);
  }

  @Override
  public void setChannelEvent(ChannelEvent channelEvent) {
    simpleLocalContext.setProperty("currentchannelevent", channelEvent);
  }

  @Override
  public Map<String, String> getReqParam() {
    return simpleLocalContext.getProperty("currentrequestparam", Map.class).orElse(null);
  }

  @Override
  public void setReqParam(Map<String, String> reqParam) {
    simpleLocalContext.setProperty("currentrequestparam", reqParam);
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

}
*/
