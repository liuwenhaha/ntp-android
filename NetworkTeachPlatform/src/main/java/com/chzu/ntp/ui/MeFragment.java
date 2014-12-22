package com.chzu.ntp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * 我界面，可以查看学生本人选的课程和一些系统设置
 */
public class MeFragment extends Fragment implements View.OnClickListener{

    private static MeFragment meFragment;
    private TextView login,myCourse,myDownload,setting;//登录、我的课程、我的下载、设置

//    private OnFragmentInteractionListener mListener;

    /**
     *创建单例对象
     */
    public static MeFragment getInstance() {
        if (meFragment==null){
            meFragment=new MeFragment();
        }
        return meFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_me, container, false);
        login= (TextView) view.findViewById(R.id.login);
        myCourse= (TextView) view.findViewById(R.id.myCourse);
        myDownload= (TextView) view.findViewById(R.id.myDownload);
        setting= (TextView) view.findViewById(R.id.setting);
        login.setOnClickListener(this);
        myCourse.setOnClickListener(this);
        myDownload.setOnClickListener(this);
        setting.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login://登录
                //检查是否有登录



                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.myCourse://我的课程

                break;
            case R.id.myDownload://我的下载

                break;
            case R.id.setting://设置
                 startActivity(new Intent(getActivity(),SettingActivity.class));
                break;
        }
    }

//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        public void onFragmentInteraction(Uri uri);
//    }

}
