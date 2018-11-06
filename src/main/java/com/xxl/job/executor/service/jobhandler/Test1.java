package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

@Component
@JobHandler(value="Test")
public class Test1 extends IJobHandler {


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        ReturnT r = new ReturnT<String>("success");
        r.setMsg("IDEA_eroo");
        return r;
    }
}
