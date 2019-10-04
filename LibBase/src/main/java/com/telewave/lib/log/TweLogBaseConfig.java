package com.telewave.lib.log;

/**
 * @Author: rick_tan
 * @Date: 19-7-20
 * @Version: v1.0
 * @Des 配置Log输出参数的配置基类.
 * 1. TweLogBaseConfig是抽象类，不能直接初始化。
 * 2. 在项目中配置Log参数时，需要在 src/twe/citizencard/config 中声明 <b>TweLogConfig.java</b> 继承于<em><strong>TweLogBaseConfig</strong></em>，然后实现所有的abstract抽象方法，配置所需的参数。<br/>
 * 3. 如果在项目中缺少 TweLogConfig.java ，会发生RuntimeException。
 */

public abstract class TweLogBaseConfig {

    /**
     * Log输出模式常量：不打印任何日志
     */
    public static final int LOG_NONE = 0;
    /**
     * Log输出模式常量：将日志打印到控制台
     */
    public static final int LOG_CONSOLE = 1;
    /**
     * Log输出模式常量：将日志打印到文件
     */
    public static final int LOG_FILE = 2;
    /**
     * Log输出模式常量：将日志同时打印到控制台和文件
     */
    public static final int LOG_BOTH = 3;


    protected TweLogBaseConfig() {
    }

    /**
     * 设置日志打印模式
     * 注：行为等同Logcat，在AndroidManifest.xml文件中Debugable为false时不输入任何日志
     * （参见Log输出模式常量）
     */
    public abstract int getLogMode();

    /**
     * 设置日志的模块的包名
     * （第三方模块请将其配置成自己对应的包名）
     */
    public abstract String getPackageName();

    public abstract boolean isDebug();
}
