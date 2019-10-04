package com.telewave.config;

import com.telewave.lib.base.AppProxy;
import com.telewave.lib.log.TweLogBaseConfig;

/**
 * @Author: rick_tan
 * @Date: 19-7-21
 * @Version: v1.0
 * @Des TweLogConfig
 */
public class TweLogConfig extends TweLogBaseConfig {

    @Override
    public int getLogMode() {
        // TODO Auto-generated method stub
        return TweLogBaseConfig.LOG_BOTH;
    }

    @Override
    public String getPackageName() {
        return AppProxy.getApplication().getPackageName();
    }

    @Override
    public boolean isDebug() {
        return false;
    }

//	@Override
//	public void initTraceModules() {
//		// TODO Auto-generated method stub
//		
//	}

//	@Override
//	public boolean isForceTrace() {
//		// TODO Auto-generated method stub
//		return false;
//	}
}