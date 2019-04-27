package blcs.lwb.utils.fragment;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import blcs.lwb.lwbtool.base.BasePresenter;
import blcs.lwb.lwbtool.bean.VersionBean;
import blcs.lwb.lwbtool.retrofit.MyObserver;
import blcs.lwb.lwbtool.retrofit.use.RequestUtils;
import blcs.lwb.lwbtool.utils.AppUtils;
import blcs.lwb.lwbtool.utils.LinDownloadAPk;
import blcs.lwb.lwbtool.utils.LinPermission;
import blcs.lwb.lwbtool.utils.LogUtils;
import blcs.lwb.lwbtool.utils.RxToast;
import blcs.lwb.lwbtool.utils.dialog.dialogFragment.LinCustomDialogFragment;
import blcs.lwb.utils.Constants;
import blcs.lwb.utils.R;
import blcs.lwb.utils.manager.FramentManages;
import butterknife.BindView;
import butterknife.OnClick;

public class VersionUpdateFragment extends BaseFragment {
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_version_update;
    }

    @Override
    protected void initView() {
        tvVersionName.setText(AppUtils.getAppName(activity));
        tvVersionCode.setText("Version " + AppUtils.getVersionName(activity));

    }

    @Override
    public void setMiddleTitle(Toolbar title) {

    }

    @Override
    protected BasePresenter bindPresenter() {
        return null;
    }

    @Override
    public void popBackListener(int returnCode, Bundle bundle) {

    }

    @OnClick({R.id.ll_version_update_function, R.id.ll_version_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_version_update_function:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.Item_Name,FramentManages.FunciotnIntro);
                addFrament(FramentManages.FunciotnIntro,bundle);
                break;
            case R.id.ll_version_update:
                RequestUtils.getVersion(this, new MyObserver<VersionBean>(activity) {
                    @Override
                    public void onSuccess(VersionBean result) {
                        //判断是否更新
                        upVersion(result.getVersionName(),result.getApkUrl());
                    }
                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
                break;
        }
    }

    public void upVersion(String name,String url){
        LogUtils.e("====="+AppUtils.getVersionName(activity));
        if(AppUtils.getVersionName(activity).equals(name)){
            RxToast.info(activity,"已经是最新版本");
        }else{
            if (LinPermission.checkPermission(activity, 7)) {
                LinCustomDialogFragment.init().setTitle("发现新版本"+name)
                        .setContent("是否更新?")
                        .setType(LinCustomDialogFragment.TYPE_CANCLE)
                        .setOnClickListener(new LinCustomDialogFragment.OnSureCancleListener() {
                            @Override
                            public void clickSure(String EdiText) {
                                    LinDownloadAPk.downApk(activity, url);
                            }

                            @Override
                            public void clickCancle() {

                            }
                        }).show(getFragmentManager());
            } else {
                LinPermission.requestPermission(activity, 7);
            }
        }
    }
}
