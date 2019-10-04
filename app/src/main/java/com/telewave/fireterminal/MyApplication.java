package com.telewave.fireterminal;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.telewave.battlecommand.MyLifecycleHandler;
import com.telewave.battlecommand.imui.service.IMOperate;
import com.telewave.battlecommand.receiver.TimeTickReceiver;
import com.telewave.battlecommand.sunmi.AidlUtil;
import com.telewave.battlecommand.utils.CrashHandler;
import com.telewave.business.twe.BuildConfig;
import com.telewave.business.twe.R;
import com.telewave.lib.base.AppProxy;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.router.BaseJumper;
import com.telewave.lib.router.RouterManager;
import com.telewave.lib.router.RouterPath;
import com.telewave.lib.router.interceptor.InterceptorDepend;
import com.tencent.qcloud.tim.uikit.BaseApplication;
import com.tencent.smtt.sdk.QbSdk;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.URLConnectionNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class MyApplication extends BaseApplication implements AppProxy.AbnormalStartCallback {
    static final String TAG = "MyApplication";
    private static MyApplication sInstance = null;
    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //AppProxy
        AppProxy.getInstance().init(this, !BuildConfig.DEBUG)
                .setAbnormalStartCallback(this)
                .setIsDebug(BuildConfig.DEBUG)
                .setVERSION_NAME(BuildConfig.VERSION_NAME)
                .setAPPLICATION_ID("com.telewave.battlecommand");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        context = getApplicationContext();
        initRouter();

        initOkGo();
        IMOperate.getInstance().initIM();
        //百度地图api的key验证
        SDKInitializer.initialize(this);
        // 使用HttpURLConnection做网络层。
        NoHttp.initialize(InitializationConfig.newBuilder(this)
                .networkExecutor(new URLConnectionNetworkExecutor())
                .cacheStore(new DBCacheStore(this).setEnable(true))
                .connectionTimeout(30 * 1000)
                .readTimeout(30 * 1000)
                .build());
        Logger.setTag("NoHttpDebugTag");
        // 开始NoHttp的调试模式, 这样就能看到请求过程和日志
        Logger.setDebug(true);

        if (initDirs()) {
            initNavi();
        }
        initTextSpeech();
        /**
         * 2019/07/30新增苏米一体机打印连接服务
         */
        AidlUtil.getInstance().connectPrinterService(this);
        // 捕获异常崩溃信息
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        //全局管理Activity生命周期
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());

        /**
         * 2019/05/20
         * 新增接收Intent.ACTION_TIME_TICK，这个广播每分钟发送一次，我们可以每分钟检查一次Service的运行状态，如果已经被结束了，就重新启动Service。
         */
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        TimeTickReceiver timeTickReceiver = new TimeTickReceiver();
        registerReceiver(timeTickReceiver, filter);

        initQbSdk();

        closeAndroidPDialog();
    }


    public static MyApplication getInstance() {
        if (null == sInstance) {
            sInstance = new MyApplication();
        }
        return sInstance;
    }

    private void initNavi() {
        if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
            return;
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(getApplicationContext(),
                getSdcardDir(), "BNSDKSimpleDemo", new IBaiduNaviManager.INaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                            ToastUtils.toastShortMessage(result);
                        }
                    }

                    @Override
                    public void initStart() {
                    }

                    @Override
                    public void initSuccess() {
//                        Toast.makeText(getApplicationContext(),
//                                "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed(int errCode) {
//                        Toast.makeText(getApplicationContext(),
//                                "百度导航引擎初始化失败 " + errCode, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(getApplicationContext(),
                getSdcardDir(), getResources().getString(R.string.app_name), ConstData.TTSAppID);

    }

    private void initTextSpeech() {
        //初始化TTS
        ConstData.textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // 判断是否转化成功
                if (status == TextToSpeech.SUCCESS) {
                    ConstData.isTextToSpeechEnable = true;
                    //默认设定语言为中文，原生的android貌似不支持中文。
                    int result = ConstData.textToSpeech.setLanguage(Locale.CHINESE);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                        ToastUtils.toastShortMessage("语音服务不可用");
                    } else {
                        //不支持中文就将语言设置为英文
                        ConstData.textToSpeech.setLanguage(Locale.US);
                    }
                }
            }
        });
    }

    private boolean initDirs() {
        String mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, getResources().getString(R.string.app_name));
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    public void initQbSdk() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("onViewInitFinished", " onViewInitFinished is " + arg0);


            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        // 这个函数内是异步执行所以不会阻塞 App 主线程，这个函数内是轻量级执行所以对 App 启动性能没有影响
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }


    public static Context getAppContext() {
        return context;
    }

    /**
     * 重新初始化应用界面，清空当前Activity棧，并启动欢迎页面
     */
    @Override
    public void rbnormalStartReInitApp() {
        Intent intent = new Intent(getAppContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getAppContext().startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void initOkGo() {

        HttpHeaders headers = new HttpHeaders();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        builder.hostnameVerifier(new SafeHostnameVerifier());
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0)                             //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers);                    //全局公共头
    }

    private class SafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                for (X509Certificate certificate : chain) {
                    certificate.checkValidity(); //检查证书是否过期，签名是否通过等
                }
            } catch (Exception e) {
                throw new CertificateException(e);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    //static 代码段可以防止内存泄露
    static {
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });

        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);
            }
        });
    }

    /**
     * 关闭Android 9.0 调试模式弹框
     */
    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initRouter() {
        RouterManager.initARouter(this, BuildConfig.DEBUG);
        RouterManager.instance().setInterceptorDepend(new InterceptorDepend() {
            @Override
            public boolean isLogin() {
                return true;//UserManager.getInstance().isLogin();
            }

            @Override
            public void beforeInterceptor(String path, Bundle bundle) {
                super.beforeInterceptor(path, bundle);
            }

            @Override
            public void gotoLogin(String targetPath, Bundle targetBundle) {
                BaseJumper.jumpWithGreenChannel(RouterPath.LOGIN_MAIN_PATH);
            }
        });
    }
}
