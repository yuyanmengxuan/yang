package com.wei.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.wei.config.ConfigURL;
import com.wei.config.WebLog;
import com.wei.mapper.TradeMapper;
import com.wei.pojo.Trade;
import com.wei.service.TradeService;
import com.wei.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/weijie/v1")
@Controller
@ResponseBody
public class TradeController {
   @Autowired
    private TradeService tradeService;
   @Autowired
   private ConfigURL configURL;
   //@Autowired
   //private TradeMapper tradeMapper;
    @PostMapping("/hehe")
    public String doTest(){
       Trade t=new Trade();
       t.setOrder("123456");
       t.setNo(123456789);
       t.setCreateTime(new Date());
        tradeService.insert(t);
        System.out.println("插入数据库");

        return "200";
    }
    @PostMapping("/hehe1")
    public String doTest1(){
        Trade t=new Trade();
        t.setOrder("789456");
        t.setNo(123456789);
        Wrapper<Trade> wr=new EntityWrapper<>();
        wr.eq("id","3");
        boolean update = tradeService.update(t, wr);

        System.out.println("更新数据库"+t.toString()+"前:update="+update);
        Trade id = tradeService.selectOne(new EntityWrapper<Trade>().eq("id", 3));
        System.out.println("修改后的数据:"+id.toString());
        return "200";
    }
    @PostMapping("/hehe2")
    public String doTest2(){


        String u = configURL.getUrl();
        System.out.println("url:"+u);
        return "200";
    }

    @PostMapping("/select")
    @WebLog(description = "请求了用户登录接口")
    public String selete(){

        Wrapper<Trade> wr=new EntityWrapper<>();
        wr.gt("id",5);


      wr.gt("create_time", ""+DateUtil.getDate(-40));
     wr.lt("create_time",""+DateUtil.getDate(40));
         List<Trade> trades = tradeService.selectList(wr);
        for (int i = 0; i <trades.size() ; i++) {
            System.out.println( trades.get(i).toString()+"\r\n");
        }

        return "200";
    }
}
