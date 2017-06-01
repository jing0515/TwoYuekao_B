package com.bawei.lvwenjing.twoyuekao_b;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    SpringView springView;
    ListView lv;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private List<Bean.AppBean> app;
    List<Bean.AppBean> list = new ArrayList<Bean.AppBean>();
    private Adapters adapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Bean.AppBean> geturl = geturl();
        springView = (SpringView) findViewById(R.id.SpringView);
        lv = (ListView) findViewById(R.id.lv);
        adapters = new Adapters(geturl, MainActivity.this);
        lv.setAdapter(adapters);
        adapters.notifyDataSetChanged();
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setType(SpringView.Type.FOLLOW);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("网络选择");
                View view1 = View.inflate(MainActivity.this, R.layout.aler01, null);
                CheckBox wifi = (CheckBox) view1.findViewById(R.id.wifi);
                CheckBox intent = (CheckBox) view1.findViewById(R.id.intent);
                intent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                            startActivity(wifiSettingsIntent);
                        }
                    }
                });
                wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        View view2 = View.inflate(MainActivity.this, R.layout.aler02, null);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setTitle("版本更新");
                        Button bt = (Button) view2.findViewById(R.id.btxiazai);
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getdata();
                            }
                        });
                        builder1.setView(view2);

                        builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder1.show();
                    }
                });
                builder.setView(view1);
                builder.show();
            }
        });
     springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                adapters.notifyDataSetChanged();
                List<Bean.AppBean> geturls = geturl();
                Adapters adaptersa = new Adapters(geturls, MainActivity.this);
                lv.setAdapter(adaptersa);
                adaptersa.notifyDataSetChanged();
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                List<Bean.AppBean> geturly = geturl();
                list.addAll(geturly);
                adapters.notifyDataSetChanged();
                springView.onFinishFreshAndLoad();
            }
        });

    }


    private void getdata() {
        String url = "http://imtt.dd.qq.com/16891/E4E087B63E27B87175F4B9BC7CFC4997.apk?fsname=com.tencent.qlauncher_6.0.2_64170111.apk&csr=97c2";
        RequestParams params = new RequestParams(url);
        //自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
        params.setSaveFilePath(Environment.getExternalStorageDirectory() + "/myapp/");
        //自动为文件命名
        params.setAutoRename(true);

        x.http().post(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                //apk下载完成后，调用系统的安装方法
             /*   Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
                startActivity(intent);*/

                Intent intent = new Intent(Intent.ACTION_VIEW);
//判断是否是AndroidN以及更高的版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", result);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("sssssssssssssssss");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }

            //网络请求之前回调
            @Override
            public void onWaiting() {
            }

            //网络请求开始的时候回调
            @Override
            public void onStarted() {
            }

            //下载的时候不断回调的方法
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //当前进度和文件总大小
                Log.i("下载中", "current：" + current + "，total：" + total);
            }
        });

    }


    public List<Bean.AppBean> geturl() {
        try {
            String s = new Asytask().execute("http://mapp.qzone.qq.com/cgi-bin/mapp/mapp_subcatelist_qq?yyb_cateid=-10&categoryName=%E8%85%BE%E8%AE%AF%E8%BD%AF%E4%BB%B6&pageNo=1&pageSize=20&type=app&platform=touch&network_type=unknown&resolution=412x732").get();
            String substring = s.substring(0, s.length() - 1);
            Gson gson = new Gson();
            Bean bean = gson.fromJson(substring, Bean.class);
            app = bean.getApp();
            list.addAll(app);
            return list;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

}
