package com.github.ltsopensource.debug.monitor;

import com.github.ltsopensource.core.commons.utils.CollectionUtils;
import com.github.ltsopensource.core.commons.utils.StringUtils;
import com.github.ltsopensource.monitor.CfgException;
import com.github.ltsopensource.monitor.MonitorAgent;
import com.github.ltsopensource.monitor.MonitorCfg;
import com.github.ltsopensource.monitor.MonitorCfgLoader;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Robert HG (254963746@qq.com) on 3/5/16.
 */
public class MonitorAgentDebug {

    private final static MonitorAgent agent = new MonitorAgent();
    private static final AtomicBoolean started = new AtomicBoolean(false);

    public static void main(String[] args) {
        String confPath = args[0];

        String cfgPath = confPath + "/lts-monitor.cfg";
        String log4jPath = confPath + "/log4j.properties";

        start(cfgPath, log4jPath);
    }

    private static void start(String cfgPath, String log4jPath) {
        if (!started.compareAndSet(false, true)) {
            return;
        }
        try {
            MonitorCfg properties = MonitorCfgLoader.load(cfgPath, log4jPath);
            agent.setRegistryAddress(properties.getRegistryAddress());
            if (StringUtils.isNotEmpty(properties.getClusterName())) {
                agent.setClusterName(properties.getClusterName());
            }
            if (StringUtils.isNotEmpty(properties.getIdentity())) {
                agent.setIdentity(properties.getIdentity());
            }
            if (StringUtils.isNotEmpty(properties.getBindIp())) {
                agent.setBindIp(properties.getBindIp());
            }
            if (CollectionUtils.isNotEmpty(properties.getConfigs())) {
                for (Map.Entry<String, String> entry : properties.getConfigs().entrySet()) {
                    agent.addConfig(entry.getKey(), entry.getValue());
                }
            }
            agent.start();
        } catch (CfgException e) {
            System.err.println("Monitor Startup Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
