package com.sung.guidedemo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sung.guidedemo.view.VariableDirectionProgressbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PagerFragment1 extends Fragment {
    private TextView value1,value2,value3,value4;
    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                case 0:
                    value1.setText(""+msg.arg2);
                    break;
                case 1:
                    value2.setText(""+msg.arg2);
                    break;
                case 2:
                    value3.setText(""+msg.arg2);
                    break;
                case 3:
                    value4.setText(""+msg.arg2);
                    break;
            }
        }
    };

    public PagerFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_fragment1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        value1 = view.findViewById(R.id.tv_value1);
        value2 = view.findViewById(R.id.tv_value2);
        value3 = view.findViewById(R.id.tv_value3);
        value4 = view.findViewById(R.id.tv_value4);

        VariableDirectionProgressbar progressbar1 = view.findViewById(R.id.vpb_progress1);
        VariableDirectionProgressbar progressbar2 = view.findViewById(R.id.vpb_progress2);
        VariableDirectionProgressbar progressbar3 = view.findViewById(R.id.vpb_progress3);
        VariableDirectionProgressbar progressbar4 = view.findViewById(R.id.vpb_progress4);

        value1.setText(""+progressbar1.getCurrentProgress());
        value2.setText(""+progressbar2.getCurrentProgress());
        value3.setText(""+progressbar3.getCurrentProgress());
        value4.setText(""+progressbar4.getCurrentProgress());

        progressbar1.addOnProgressChangeListener(new VariableDirectionProgressbar.OnProgressChangeListener() {
            @Override
            public void onProgressChange(int progress) {
                Message msg = new Message();
                msg.arg1 = 0;
                msg.arg2 = progress;
                uiHandler.handleMessage(msg);
            }
        });
        progressbar2.addOnProgressChangeListener(new VariableDirectionProgressbar.OnProgressChangeListener() {
            @Override
            public void onProgressChange(int progress) {
                Message msg = new Message();
                msg.arg1 = 1;
                msg.arg2 = progress;
                uiHandler.handleMessage(msg);
            }
        });
        progressbar3.addOnProgressChangeListener(new VariableDirectionProgressbar.OnProgressChangeListener() {
            @Override
            public void onProgressChange(int progress) {
                Message msg = new Message();
                msg.arg1 = 2;
                msg.arg2 = progress;
                uiHandler.handleMessage(msg);
            }
        });
        progressbar4.addOnProgressChangeListener(new VariableDirectionProgressbar.OnProgressChangeListener() {
            @Override
            public void onProgressChange(int progress) {
                Message msg = new Message();
                msg.arg1 = 3;
                msg.arg2 = progress;
                uiHandler.handleMessage(msg);
            }
        });
    }
}
