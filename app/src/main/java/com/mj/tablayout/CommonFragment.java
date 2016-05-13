package com.mj.tablayout;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by vienan on 16/4/10.
 */
public class CommonFragment extends BaseFragment {

    private static final String FRAGMENT_INDEX = "fragment_index";
    private int mCurIndex = -1;
    LayoutInflater inflater;
    private View mFragmentView;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private RecyclerView recycleview;
    private MyAdapter adapter;
    private List<String> list =new ArrayList<>();
    private ProgressDialog pd;

    public static CommonFragment newInstance(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, index);
        CommonFragment fragment = new CommonFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater=inflater;
        pd = new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);


        if (mFragmentView == null) {
            mFragmentView= inflater.inflate(R.layout.fragment_common, container, false);

            recycleview = (RecyclerView) mFragmentView.findViewById(R.id.recycleview);

             recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));


            if (getArguments() != null) mCurIndex = getArguments().getInt(FRAGMENT_INDEX,1);
            isPrepared = true;
            lazyLoad();

        }
        //共用一个视图，需要先移除以前的view
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null) parent.removeView(mFragmentView);
        return mFragmentView;

    }


    class MyAdapter extends RecyclerView.Adapter{

        private List<String> list;
        public MyAdapter(List<String> list) {
            this.list=list;
        }



        @Override
        public int getItemViewType(int position) {
            if (position%2==0)return 0;
            return 1;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType==0){
                return new BlueViewHolder(inflater.inflate(R.layout.item,parent,false));
            }else{
                return new PinkViewHolder(inflater.inflate(R.layout.item,parent,false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof BlueViewHolder) {
                ((BlueViewHolder) holder).tv.setText(String.valueOf(list.get(position)));
                if (position%2==0)
                ((BlueViewHolder) holder).iv.setImageResource(R.mipmap.hb_0);
                else
                ((BlueViewHolder) holder).iv.setImageResource(R.mipmap.hb_1);
            }else{
                ((PinkViewHolder) holder).tv.setText(String.valueOf(list.get(position)));
                if (position%2==0)
                    ((PinkViewHolder) holder).iv.setImageResource(R.mipmap.hb_0);
                else
                ((PinkViewHolder) holder).iv.setImageResource(R.mipmap.hb_1);
            }
//            adapter.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            if (list.isEmpty()&&list==null)
                return 0;
            return list.size();
        }


        class BlueViewHolder extends RecyclerView.ViewHolder{
             ImageView iv;
            TextView tv;
            public BlueViewHolder(View itemView) {
                super(itemView);
                iv= (ImageView) itemView.findViewById(R.id.iv);
                tv = (TextView) itemView.findViewById(R.id.tv);
                tv.setBackgroundResource(R.color.colorPrimary);
            }
        }
        class PinkViewHolder extends RecyclerView.ViewHolder{
             ImageView iv;
            TextView tv;
            public PinkViewHolder(View itemView) {
                super(itemView);
                iv= (ImageView) itemView.findViewById(R.id.iv);
                tv = (TextView) itemView.findViewById(R.id.tv);
                tv.setBackgroundResource(R.color.colorAccent);
            }
        }

        private void add(List<String> t){
            list.clear();
            list.addAll(t);
        }
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared||!isVisible || mHasLoadedOnce) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pd!=null && !pd.isShowing())
                                pd.show();
                        }
                    });
                    Thread.sleep(2000);
                    initList(mCurIndex);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        adapter = new MyAdapter(list);
        recycleview.setAdapter(adapter);

}

    private void initList(int mCurIndex) {

        for (int i=0;i<mCurIndex*20;i++)
        {
            list.add(String.valueOf(i));
        }
        mHasLoadedOnce = true;
        ((MainActivity) getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pd != null && pd.isShowing())
                    pd.dismiss();


                if (adapter!=null)
                    adapter.notifyDataSetChanged();
            }
        });
    }


}
