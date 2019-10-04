package com.telewave.battlecommand.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.telewave.battlecommand.activity.ChatPicShowActivity;
import com.telewave.battlecommand.bean.ReservePlan;
import com.telewave.battlecommand.http.HttpResponseUtil;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.utils.OpenWPSUtil;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.FileUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

/**
 * 重点单位附件 适配器
 *
 * @author liwh
 * @date 2019/1/11
 */
public class ReservePlanFuJianAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReservePlan.FilesBean> filesBeanList;

    public ReservePlanFuJianAdapter(Context mContext, List<ReservePlan.FilesBean> filesBeanList) {
        super();
        this.mContext = mContext;
        this.filesBeanList = filesBeanList;
    }

    @Override
    public int getCount() {
        return this.filesBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.filesBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.policy_guifan_fujian_item, null);
            holder = new ViewHolder();
            holder.fujianFileName = (TextView) convertView.findViewById(R.id.fujian_filename);
            holder.fujianUpdateTime = (TextView) convertView.findViewById(R.id.fujian_updatetime);
            holder.tishi_text = (TextView) convertView.findViewById(R.id.tishi_text);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.jindu);
            holder.jindu_text = (TextView) convertView.findViewById(R.id.jindu_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ReservePlan.FilesBean fileListBean = filesBeanList.get(position);
        holder.fujianFileName.setText(fileListBean.getFilename());
        holder.fujianUpdateTime.setText(fileListBean.getUpdatetime());

        File file = new File(ConstData.IMPOTANT_UNIT_DIR + fileListBean.getFilename());
        if (file.exists()) {
            holder.tishi_text.setVisibility(View.GONE);
        } else {
            holder.tishi_text.setVisibility(View.VISIBLE);
        }
        holder.tishi_text.setOnClickListener(new OnClickTextListener(holder, fileListBean.getFilename(), fileListBean.getSavepath()));

        // 点击 打开文件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(ConstData.IMPOTANT_UNIT_DIR + fileListBean.getFilename());
                if (file.exists()) {
                    String extensionName = FileUtils.getExtensionName(fileListBean.getFilename());
                    if ("pdf".equals(extensionName) || "doc".equals(extensionName) || "docx".equals(extensionName) || "xls".equals(extensionName) || "xlsx".equals(extensionName)
                            || "ppt".equals(extensionName) || "pptx".equals(extensionName)) {
                        OpenWPSUtil.openFile(mContext, file.getPath());
                    } else if ("jpg".equals(extensionName) || "png".equals(extensionName) || "jpeg".equals(extensionName) || "bmp".equals(extensionName)) {
                        Intent intent = new Intent(mContext, ChatPicShowActivity.class);
                        intent.putExtra("PicPath", file.getPath());
                        mContext.startActivity(intent);
                    }
                } else {
                    ToastUtils.toastShortMessage("文件不存在，请先下载");
                }

            }

        });
        return convertView;

    }

    class OnClickTextListener implements View.OnClickListener {
        private ViewHolder viewHolder;
        private String fileName;
        private String downloadUrl;

        public OnClickTextListener(ViewHolder viewHolder, String fileName, String downloadUrl) {
            this.viewHolder = viewHolder;
            this.fileName = fileName;
            this.downloadUrl = downloadUrl;
        }

        @Override
        public void onClick(View v) {
            File file = new File(ConstData.IMPOTANT_UNIT_DIR + fileName);
            if (file.exists()) {
                FileUtils.deleteFile(file);
            }
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            viewHolder.tishi_text.setVisibility(View.GONE);
            String httpUrlBase = downloadUrl.substring(0, downloadUrl.lastIndexOf("/") + 1);
            String HTTP_URL = null;
            try {
                String fileNameTemp = URLEncoder.encode(fileName, "utf-8").replaceAll("\\+", "%20");
                HTTP_URL = httpUrlBase + fileNameTemp;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(httpUrlBase)) {
                //从服务器下载预案文档
                downLoadFile(mContext, viewHolder, ConstData.urlManager.baseFilesURL + downloadUrl, ConstData.IMPOTANT_UNIT_DIR, fileName);
            } else {
                //从服务器下载预案文档
                downLoadFile(mContext, viewHolder, ConstData.urlManager.baseFilesURL + HTTP_URL, ConstData.IMPOTANT_UNIT_DIR, fileName);
            }

        }

    }

    public void downLoadFile(final Context context, final ViewHolder viewHolder, String downLoadUrl, final String storagePath, final String filename) {
        DownloadRequest request = NoHttp.createDownloadRequest(downLoadUrl, storagePath, filename, true, false);
        if (!FileUtils.createFilepath(storagePath)) {
            ToastUtils.toastLongMessage("外部存储不可用");
            return;
        } else {
            NoHttpManager.getDownloadInstance().add(0, request, new DownloadListener() {
                @Override
                public void onDownloadError(int what, Exception exception) {
                    HttpResponseUtil.downloadErrorNotice(exception, (Activity) context, "[" + filename + "]文档", false);
                }

                @Override
                public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {

                }

                @Override
                public void onProgress(int what, int progress, long fileCount, long speed) {
                    viewHolder.progressBar.setProgress(progress);
                    viewHolder.jindu_text.setText(progress + "%");
                }

                @Override
                public void onFinish(int what, String filePath) {
                    if (TextUtils.isEmpty(filePath)) {
                        ToastUtils.toastShortMessage("文件不存在");
                        return;
                    }
                    long filesize = 0;
                    File file = new File(filePath);
                    try {
                        filesize = FileUtils.getFileSize(file);
                        if (filesize == 0) {
                            ToastUtils.toastShortMessage("暂无预案文档");
                            FileUtils.deleteFile(file);
                        } else {
//                            DownLoadUtil.openFile(context, filePath);
                            ToastUtils.toastShortMessage("下载完成，可以点击打开查看了");
                        }
                        viewHolder.progressBar.setVisibility(View.GONE);
                        viewHolder.jindu_text.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancel(int what) {
                    ToastUtils.toastShortMessage("取消下载文件");
                }
            });
        }
    }

    static class ViewHolder {
        private TextView fujianFileName;
        private TextView fujianUpdateTime;
        public TextView tishi_text;
        public ProgressBar progressBar;
        public TextView jindu_text;
    }


}
