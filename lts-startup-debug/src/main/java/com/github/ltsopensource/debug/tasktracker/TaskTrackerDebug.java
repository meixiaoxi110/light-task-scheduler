package com.github.ltsopensource.debug.tasktracker;

import com.github.ltsopensource.startup.tasktracker.*;
import com.github.ltsopensource.tasktracker.TaskTracker;

/**
 * @author Robert HG (254963746@qq.com) on 9/11/15.
 */
public class TaskTrackerDebug {

    public static void main(String[] args) {
        String cfgPath = args[0];
        start(cfgPath);
    }

    private static void start(String cfgPath) {
        try {
            TaskTrackerCfg cfg = TaskTrackerCfgLoader.load(cfgPath);
            final TaskTracker taskTracker;
            if (cfg.isUseSpring()) {
                taskTracker = SpringStartup.start(cfg, cfgPath);
            } else {
                taskTracker = DefaultStartup.start(cfg);
            }
            taskTracker.start();
            Runtime.getRuntime().addShutdownHook(new Thread(taskTracker::stop));
        } catch (CfgException e) {
            System.err.println("TaskTracker Startup Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
