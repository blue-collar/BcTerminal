package com.telewave.lib.router;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.launcher.ARouter;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseJumper {

    /***标注在Activity 或者服务接口上的 路径  /模块名/功能名/描述名 ***/
    private String path;
    /****携带数据，可为空***/
    private Bundle data;
    /***startActivityForResult****/
    private Activity activity;
    /***startActivityForResult****/
    private int requestCode = -1;
    /***启动模式**/
    private int flag = -1;
    /****动画进入***/
    private int animInRes;
    /****动画出来***/
    private int animOutRes;
    private NavCallback navCallback;

    public static final class Builder {

        private String path;
        private Bundle data;
        private Activity activity;
        private int requestCode = -1;
        private int flag = -1;
        private int animInRes;
        private int animOutRes;
        private NavCallback navCallback;

        /**
         * 路由路径
         *
         * @param path
         */
        public Builder(@NonNull String path) {
            this.path = path;
        }

        /**
         * startActivity 要传递的数据
         *
         * @param data
         * @return
         */
        public Builder data(Bundle data) {
            this.data = data;
            return this;
        }

        /**
         * startActivity forResult
         *
         * @param activity
         * @param requestCode
         * @return
         */
        public Builder activity(Activity activity, int requestCode) {
            this.activity = activity;
            this.requestCode = requestCode;
            return this;
        }

        /**
         * 启动activity 的flag
         *
         * @param flag ps
         *             Intent.FLAG_ACTIVITY_SINGLE_TOP,
         *             Intent.FLAG_ACTIVITY_NEW_TASK,
         * @return
         */
        public Builder flag(int flag) {
            this.flag = flag;
            return this;
        }

        public Builder navCallback(NavCallback navCallback) {
            this.navCallback = navCallback;
            return this;
        }

        /***
         * Activity 启动动画
         * @param animInRes
         * @param animOutRes
         * @return
         */
        public Builder anim(int animInRes, int animOutRes) {
            this.animInRes = animInRes;
            this.animOutRes = animOutRes;
            return this;

        }

        public BaseJumper build() {
            return new BaseJumper(this);
        }

    }

    protected BaseJumper() {
    }

    private BaseJumper(Builder builder) {
        this.path = builder.path;
        this.data = builder.data;
        this.activity = builder.activity;
        this.requestCode = builder.requestCode;
        this.flag = builder.flag;
        this.animInRes = builder.animInRes;
        this.animOutRes = builder.animOutRes;
        this.navCallback = builder.navCallback;

    }

    public void navigation() {
        try {
            //
            Postcard postcard = getPostcardFromPath(path);
            //启动模式
            if (flag != -1) {
                postcard.withFlags(flag);
            }
            // 启动动画
            if (animInRes != -1 && animOutRes != -1) {
                postcard.withTransition(animInRes, animOutRes);
            }
            //携带参数
            if (data != null)
                postcard.with(data);
            //是否需要StartActivityForResult
            jumpForResult(postcard, activity, requestCode, navCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*************以下为常用的静态方法********************/
    /**
     * 获取服务
     *
     * @param path 路径方式  /模块名/interface/服务名
     * @param <T>
     * @return
     */
    public static <T extends IProvider> T getService(String path) {
        try {
            Postcard postcard = getPostcardFromPath(path);
            return (T) postcard.navigation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param path
     * @param paraMap Serializable序列化 键值对
     */
    public static void jumpSeriaARouter(@NonNull String path, @Nullable Map<String, ? extends Serializable> paraMap) {
        jumpSeriaARouter(path, null, -1, paraMap);
    }

    public static void jumpSeriaARouter(@NonNull String path, @Nullable Map<String, ? extends Serializable> paraMap, NavigationCallback callback) {
        jumpSeriaARouter(path, null, -1, paraMap, callback);
    }

    /**
     * startActivityForResult
     *
     * @param path
     * @param context
     * @param requestCode
     * @param paraMap
     */
    public static void jumpSeriaARouter(@NonNull String path, @Nullable Activity context, int requestCode, @Nullable Map<String, ? extends Serializable> paraMap) {

        try {
            Postcard postcard = getPostcardFromPath(path);
            if (paraMap != null && paraMap.size() > 0) {
                for (Map.Entry<String, ? extends Serializable> entry : paraMap.entrySet()) {
                    postcard.withSerializable(entry.getKey(), entry.getValue());
                }
            }
            jumpForResult(postcard, context, requestCode, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void jumpSeriaARouter(@NonNull String path, @Nullable Activity context, int requestCode, @Nullable Map<String, ? extends Serializable> paraMap, NavigationCallback callback) {

        try {
            Postcard postcard = getPostcardFromPath(path);
            if (paraMap != null && paraMap.size() > 0) {
                for (Map.Entry<String, ? extends Serializable> entry : paraMap.entrySet()) {
                    postcard.withSerializable(entry.getKey(), entry.getValue());
                }
            }
            jumpForResult(postcard, context, requestCode, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param path
     * @param paraMap Parcelable序列化 键值对
     */
    public static void jumpParcelARouter(@NonNull String path, @Nullable Map<String, ? extends Parcelable> paraMap) {
        jumpParcelARouter(path, null, -1, paraMap);
    }

    /**
     * startActivityForResult
     *
     * @param path
     * @param context
     * @param requestCode
     * @param paraMap
     */
    public static void jumpParcelARouter(@NonNull String path, @Nullable Activity context, int requestCode, @Nullable Map<String, ? extends Parcelable> paraMap) {
        try {
            Postcard postcard = getPostcardFromPath(path);
            if (paraMap != null && paraMap.size() > 0) {
                for (Map.Entry<String, ? extends Parcelable> entry : paraMap.entrySet()) {
                    postcard.withParcelable(entry.getKey(), entry.getValue());
                }
            }
            jumpForResult(postcard, context, requestCode, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /*************以下为Activity 跳转的*****************/
    public static void jump(@NonNull String path) {
        jump(path, null);
    }

    /**
     * @param path
     * @param data Bundle 键值对
     */
    public static void jump(@NonNull String path, @Nullable Bundle data) {
        jump(path, null, -1, data);
    }

    /**
     * startActivityForResult
     *
     * @param path
     * @param context
     * @param requestCode
     * @param data
     */
    public static void jump(@NonNull String path, @Nullable Activity context, int requestCode, @Nullable Bundle data) {
        try {
            Postcard postcard = getPostcardFromPath(path);
            if (data != null) {
                postcard.with(data);
            }

            jumpForResult(postcard, context, requestCode, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void jumpWithGreenChannel(@NonNull String path) {
        jumpWithGreenChannel(path, null);
    }

    /**
     * @param path
     * @param data Bundle 键值对
     */
    public static void jumpWithGreenChannel(@NonNull String path, @Nullable Bundle data) {
        jumpWithGreenChannel(path, null, -1, data);
    }

    /**
     * startActivityForResult
     *
     * @param path
     * @param context
     * @param requestCode
     * @param data
     */
    public static void jumpWithGreenChannel(@NonNull String path, @Nullable Activity context, int requestCode, @Nullable Bundle data) {
        try {
            Postcard postcard = getPostcardFromPath(path);
            if (data != null) {
                postcard.greenChannel().with(data);
            }

            jumpForResult(postcard, context, requestCode, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 针对startActivityForResult
     *
     * @param postcard
     * @param context
     * @param requestCode
     */
    private static void jumpForResult(Postcard postcard, @Nullable Activity context, int requestCode, NavigationCallback callback) {
        if (context != null) {
            postcard.navigation(context, requestCode, callback);
        } else {
            postcard.navigation(null, callback);
        }
    }

    /***
     *
     * @param path  格式
     *              1 普通模式： /模块名/组件名/功能名  /muduleName/ComponentName/FeatureName
     *                   1.1    ps /login/activity/main
     *              2 url模式：
     *                  2.1 跳转 url 路径   url = "scheme://host" + /muduleName/ComponentName/FeatureName  + parameters
     *                  2.2 ps
     *                  2.3  参数 parameters 不是必须的
     * @return
     */
    private static Postcard getPostcardFromPath(@NonNull String path) {

        if (TextUtils.isEmpty(path)) {
            throw new NullPointerException("ARouter path can't be null");
        }
        /****
         *         查找以 *:// 开头的
         *         ps http://   , https://   , customScheme://
         *
         * ***/
        Pattern p = Pattern.compile(".+?://");
        Matcher m = p.matcher(path);
        boolean urlFlag = m.find();
        Postcard postcard;
        if (urlFlag) {
            Uri uri = Uri.parse(path);
            postcard = ARouter.getInstance().build(uri);
        } else {
            postcard = ARouter.getInstance().build(path);
        }

        return postcard;
    }
}
