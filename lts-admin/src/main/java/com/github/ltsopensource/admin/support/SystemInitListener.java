package com.github.ltsopensource.admin.support;

import com.github.ltsopensource.core.commons.file.FileUtils;
import com.github.ltsopensource.core.commons.utils.PlatformUtils;
import com.github.ltsopensource.core.commons.utils.StringUtils;
import com.github.ltsopensource.core.compiler.AbstractCompiler;
import com.github.ltsopensource.core.constant.ExtConfig;
import com.github.ltsopensource.core.json.JSONFactory;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.monitor.MonitorAgentStartup;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Robert HG (254963746@qq.com) on 9/2/15.
 */
public class SystemInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        String confPath = servletContextEvent.getServletContext().getInitParameter("lts.admin.config.path");
        String logConfPath = servletContextEvent.getServletContext().getInitParameter("lts.admin.log.config.path");
        if (StringUtils.isNotEmpty(confPath)) {
            System.out.println("lts.admin.config.path : " + confPath);
        }
        AppConfigurer.load(confPath);

        String compiler = AppConfigurer.getProperty("configs." + ExtConfig.COMPILER);
        if (StringUtils.isNotEmpty(compiler)) {
            AbstractCompiler.setCompiler(compiler);
        }

        String jsonAdapter = AppConfigurer.getProperty("configs." + ExtConfig.LTS_JSON);
        if (StringUtils.isNotEmpty(jsonAdapter)) {
            JSONFactory.setJSONAdapter(jsonAdapter);
        }

        String loggerAdapter = AppConfigurer.getProperty("configs." + ExtConfig.LTS_LOGGER);
        if (StringUtils.isNotEmpty(loggerAdapter)) {
            LoggerFactory.setLoggerAdapter(loggerAdapter);
        }

        if (FileUtils.exist(logConfPath)) {
            //  log4j 配置文件路径
            PropertyConfigurator.configure(logConfPath);
        }

        boolean monitorAgentEnable = Boolean.valueOf(AppConfigurer.getProperty("lts.monitorAgent.enable", "true"));
        if (monitorAgentEnable) {
            MonitorAgentStartup.start(confPath, logConfPath);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        MonitorAgentStartup.stop();
    }
}
