package com.taotao.order.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class Test extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // TODO Auto-generated method stub
        System.out.println("ceshi 开始");
        System.out.println("ceshi ");
    }

}
