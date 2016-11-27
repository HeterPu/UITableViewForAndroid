package com.example.peter.myapplication;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * Created by Peter on 25/11/2016.
 *  Manual:
 *     First:  Initialize BaseListView
 *             eg.{
 *              setContentView(R.layout.activity_main);
 *              listV = (BaseListView) findViewById(R.id.mListView);
 *              listV.setSource(this,this);
 *             }
 *     Second: Inplements DataSource interface.
 *              eg.{
 *                implements BaseListView.DataSource.
 *                override several method in dataSource.
 *                Use ViewLoder to custom your view and get higer proficiency.
 *              }
 *    Third: when your datas are prepared already.call reloadData to refresh UI in ListView.
 *
 *    Good luck , enjoy yourself !!!
 */

public class BaseListView extends ListView {


    /**
     *  Datasource is a interface use to load data from activity.For inner use;
     */
    private DataSource  source;

    /**
     * The dataArray is applied to access inner datasate;
     */
    private  ArrayList<Indexpath> dataArray = new ArrayList<>();


    /**
     * Custom Arrayadapter to bridge data from datasource.
     */
    private  CustomArrayAdapter adapter;


    /**
    *  Three static constant to flag position.
    */
    private static  final  int TYPE_HEADER = -1;
    private static  final  int TYPE_Cell = 0;
    private static  final  int TYPE_FOOTER = -2;


    /**
     * Constructor
     * @param context  From outside
     * @param attrs  I don't know exactly.
     */
    public BaseListView (Context context, AttributeSet attrs){
        super(context, attrs);
    }


    /**
     * Morhod to set Context and datasouce.
     * @param context import context from outside.
     * @param source  a class implements dataSouse interface.
     */
    public void setSource( Context context, DataSource source) {
        this.source = source;
        CustomArrayAdapter   arrayAdapter = new CustomArrayAdapter(context,0,dataArray);
        setAdapter(arrayAdapter);
        this.adapter = arrayAdapter;
    }


    /**
     * When outside datasource change,you can call this method to notify inner refresh data.
     */
    public void reloadData(){
        initailizedDataSource();
    }



    /**
     * Datasourse interface.
     */
    public interface DataSource {

        /**
         * Fetch number of section.
         * @return section number.
         */
        int numberOfSection();

        /**
         * Fetch row number from a concrete section.
         * @param section  section number starts from 0.
         * @return
         */
        int numberOfRowInsection(int section);

        /**
         * Fetch Header's ViewCallBack From Outside.
         * @param section  Section number starts from 0.
         * @param holder Object Holder for reuse.
         * @return  ATTENTION:Reture null stands for no sectionheader view.
         */
        ViewCallBack viewForHeader(int section,Object holder);

        /**
         * Fetch footer's ViewCallBack From Outside.
         * @param section Section number starts from 0.
         * @param holder  Object Holder for reuse.
         * @return  ATTENTION:Reture null stands for no sectionfooter view.
         */
        ViewCallBack viewForFooter(int section,Object holder);

        /**
         * Fetch cell's ViewCallBack From Outside.
         * @param section  Section number starts from 0.
         * @param row Row number starts from 0.
         * @param holder  Object Holder for reuse.
         * @return  A ViewCallBack for cell.
         */
        ViewCallBack cellForRow(int section, int row ,Object holder);
    }




    /**
     *
     * Use for store Header,Footer and Cell state.
     */
    private class Indexpath {
        int section;
        int row;
        int rec_type;
        Indexpath(int section,int row,int rectype){
            this.section = section;
            this.row = row;
            this.rec_type = rectype;
        }
        boolean isEqualType(Indexpath index) {
            return    (this.section == index.section)&&(this.rec_type == index.rec_type);
        }
    }


    /**
     * Use for storing ViewHolder imformation.
     * @param <T> a custom view holder.
     */
    public static class ViewCallBack<T> {
        Indexpath indexpath;
        View view;
        T holder;
        ViewCallBack(T holder,View view){
            this.holder = holder;
            this.view = view;
        }
    }


    /**
     * Initialized dataSoure after call reloadData().
     */
    private void initailizedDataSource(){
        dataArray.clear();
        int section = source.numberOfSection();
        for (int i = 0 ; i < section; i++) {
            if (source.viewForHeader(0,null)!=null) {
                Indexpath header = new Indexpath(i, TYPE_HEADER, TYPE_HEADER);
                dataArray.add(header);
            }
            int row = source.numberOfRowInsection(i);
            for(int j = 0;j < row;j++) {
                Indexpath index = new Indexpath(i,j,TYPE_Cell);
                dataArray.add(index);
                if (j == row - 1) {
                    if (source.viewForFooter(0,null)!=null) {
                        Indexpath footer = new Indexpath(i,TYPE_FOOTER,TYPE_FOOTER);
                        dataArray.add(footer);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }



    /**
     * Custom adpter for inner user.
     * */
    private  class CustomArrayAdapter extends ArrayAdapter<Indexpath> {

        CustomArrayAdapter(Context context, int resource, ArrayList<Indexpath> list) {
            super(context, resource, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Indexpath path =  getItem(position);
            if (convertView == null) {
                return getViewFrom(path);
            } else {
                ViewCallBack callBack = (ViewCallBack) convertView.getTag(R.id.TAG_ID);
                if (!callBack.indexpath.isEqualType(path)) {
                    return getViewFrom(path);
                } else {
                    switch (path.rec_type) {

                        case TYPE_HEADER:
                            source.viewForHeader(path.section, callBack.holder);
                            break;
                        case TYPE_FOOTER:
                            source.viewForFooter(path.section, callBack.holder);
                            break;
                        default:
                            source.cellForRow(path.section, path.row, callBack.holder);
                            break;
                    }
                    return convertView;
                }
            }
        }

        private View getViewFrom(Indexpath path) {
            ViewCallBack bv;
            switch (path.rec_type) {
                case TYPE_HEADER:
                    bv = source.viewForHeader(path.section, null);
                    bv.indexpath = path;
                    break;
                case TYPE_FOOTER:
                    bv = source.viewForFooter(path.section, null);
                    bv.indexpath = path;
                    break;
                default:
                    bv = source.cellForRow(path.section, path.row, null);
                    bv.indexpath = path;
                    break;
            }
            View convertView = bv.view;
            convertView.setTag(R.id.TAG_ID,bv);
            return convertView;
        }
    }
}


