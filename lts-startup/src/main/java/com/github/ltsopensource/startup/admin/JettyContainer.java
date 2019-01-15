package com.github.ltsopensource.startup.admin;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Robert HG (254963746@qq.com) on 9/1/15.
 */
public class JettyContainer {

    public static void main(String[] args) {
        try {
            String confPath = args[0];

            confPath = confPath.trim();
            InputStream is = new FileInputStream(new File(confPath + "/conf/admin/lts-admin.cfg"));
            Properties conf = new Properties();
            conf.load(is);
            String port = conf.getProperty("port");
            if (port == null || port.trim().equals("")) {
                port = "8081";
            }

            String contextPath = conf.getProperty("contextPath");
            if (contextPath == null || contextPath.trim().equals("")) {
                contextPath = "/";
            }

            Server server = new Server(Integer.parseInt(port));
            WebAppContext webapp = new WebAppContext();
            webapp.setWar(confPath + "/war/lts-admin.war");
            webapp.setContextPath(contextPath);
            webapp.setInitParameter("lts.admin.config.path", confPath + "/conf/admin/lts-admin.cfg");
            webapp.setInitParameter("lts.admin.log.config.path", confPath + "conf/tasktracker/log4j.properties");
            server.setHandler(webapp);
            server.setStopAtShutdown(true);
            server.start();

            System.out.println("LTS-Admin started. http://" + NetUtils.getLocalHost() + ":" + port + (contextPath == "/" ? "" : contextPath) + "/index.htm");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
