package com.bawei.lvwenjing.twoyuekao_b;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lenovo-pc on 2017/5/28.
 */

public class Adapters extends BaseAdapter {
    List<Bean.AppBean> list;
    Context context;
    private InforHarder inforHarder;

    public Adapters(List<Bean.AppBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inforHarder = new InforHarder();
            convertView = View.inflate(context, R.layout.item, null);
            inforHarder.tv = (TextView) convertView.findViewById(R.id.iv);
            inforHarder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            convertView.setTag(inforHarder);
        } else {
            inforHarder = (InforHarder) convertView.getTag();
        }
        inforHarder.tv.setText(list.get(position).getSecCate());
        inforHarder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    list.get(position).setApkId(0);
                } else {
                    list.get(position).setApkId(1);
                }

            }

        });
        if (list.get(position).getApkId()==0){
            inforHarder.cb.setChecked(true);
        }else {
            inforHarder.cb.setChecked(false);
        }



        return convertView;
    }

    class InforHarder {
        TextView tv;
        CheckBox cb;

    }
}
