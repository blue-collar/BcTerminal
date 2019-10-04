package com.telewave.battlecommand.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.telewave.business.twe.R;
import com.tencent.qcloud.tim.uikit.component.photoview.PhotoView;
import com.tencent.qcloud.tim.uikit.component.photoview.PhotoViewAttacher;

/**
 * <p>类说明: 实现图片分页加载</p>
 *
 * @author 张军
 * @version 1.0
 * @since 2019-07-26
 */

public class ImageFragment extends Fragment {


    private static final String IMAGE_URL = "image";
    private PhotoView image;
    private String imageUrl;
    private PhotoViewAttacher mAttacher;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String param1) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        image = view.findViewById(R.id.image);
        Glide.with(getContext()).load(imageUrl).into(image);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //得到Fragment的根布局并使该布局可以获得焦点
        getView().setFocusableInTouchMode(true);
        //得到Fragment的根布局并且使其获得焦点
        getView().requestFocus();
        //对该根布局View注册KeyListener的监听
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    getActivity().finish();
                    getActivity().onBackPressed();
//                    getFragmentManager().popBackStackImmediate();
//                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }


}
