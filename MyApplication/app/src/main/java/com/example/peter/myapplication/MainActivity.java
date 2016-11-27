package com.example.peter.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements BaseListView.DataSource  {
    BaseListView listV;
    private int row = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listV = (BaseListView) findViewById(R.id.mListView);
        listV.setSource(this,this);
        listV.reloadData();
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = "you click item" + position;
                Toast  toast = Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                row = 10;
                listV.reloadData();
            }
        });
    }

    @Override
    public int numberOfSection() {
        return 3;
    }

    @Override
    public int numberOfRowInsection(int section) {
        if (section == 0) {
            return row;
        }
        else
        {
            return 10;
        }
    }

    @Override
    public BaseListView.ViewCallBack viewForHeader(int section, Object holder) {
        if (holder == null) {
            BaseViewHolder.HeaderStyle header = new BaseViewHolder.HeaderStyle();
            String aa = "This is section header" + section;
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View  ConvertView  =  inflater.inflate(R.layout.header_style, null);
            header.textV = (TextView)ConvertView.findViewById(R.id.headerT);
            header.textV.setText(aa);
            return new BaseListView.ViewCallBack<>(header,ConvertView);
        }
        else
        {
            BaseViewHolder.HeaderStyle header = (BaseViewHolder.HeaderStyle)holder;
            String aa = "This is section" + section +"   From Reuse";
            header.textV.setText(aa);
            return null;
        }
    }

    @Override
    public BaseListView.ViewCallBack viewForFooter(int section, Object holder) {
        if (holder == null) {
            BaseViewHolder.FooterStyle footer = new BaseViewHolder.FooterStyle();
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View  ConvertView  =  inflater.inflate(R.layout.footer_style, null);
            String aa = "This is section footer" + section;
            footer.textV = (TextView)ConvertView.findViewById(R.id.footerT);
            footer.textV.setText(aa);
            return new BaseListView.ViewCallBack<>(footer,ConvertView);
        }
        else
        {
            BaseViewHolder.FooterStyle footer = (BaseViewHolder.FooterStyle)holder;
            String aa = "This is section footer" + section +"   From Reuse";
            footer.textV.setText(aa);
            return null;
        }
    }


    @Override
    public BaseListView.ViewCallBack cellForRow(int section, int row, Object holder) {
        if (holder == null) {
            BaseViewHolder.CellStyle cell = new BaseViewHolder.CellStyle();
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View  ConvertView  =  inflater.inflate(R.layout.cell_style, null);
            cell.textV = (TextView)ConvertView.findViewById(R.id.cellT);

            String aa = "This is row" + row;
            cell.textV.setText(aa);
            return new BaseListView.ViewCallBack<>(cell,ConvertView);
        }
        else
        {
            BaseViewHolder.CellStyle cell = (BaseViewHolder.CellStyle)holder;
            String aa = "This is row" + row +"From Reuse";
            cell.textV.setText(aa);
            return null;
        }
    }
}
