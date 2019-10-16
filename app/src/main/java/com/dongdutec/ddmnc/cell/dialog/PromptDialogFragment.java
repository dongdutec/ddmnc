package com.dongdutec.ddmnc.cell.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;


public class PromptDialogFragment extends DialogFragment {
    private String title;
    private String content;
    private String cancel = "";
    private String ok = "";
    private OnCommitClickListener onCommitClickListener;
    private OnCancelClickListener onCancelClickListener;
    private TextView contentTv,titleTv;
    private int gravity;
    private Dialog dialog;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //为了样式统一和兼容性，可以使用 V7 包下的 AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_prompt, null);
        Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        if (!TextUtils.isEmpty(cancel)) {
            btn_cancle.setText(cancel);
        }
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        if (!TextUtils.isEmpty(ok)) {
            btn_ok.setText(ok);
        }
         titleTv = view.findViewById(R.id.title_name_tv);
        titleTv.setText(title);
        contentTv = view.findViewById(R.id.content_tv);
        if (!TextUtils.isEmpty(content)) {
            contentTv.setText(content);
        }
        contentTv.setGravity(gravity);
        final Dialog dialog = new Dialog(getActivity(), R.style.DialNumberDialog);
        // 关闭标题栏，setContentView() 之前调用
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCancelClickListener != null) {
                    onCancelClickListener.onCancel();
                    dismiss();
                }

            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onCommitClickListener!=null){
                    onCommitClickListener.onCommit();
                    dismiss();
                }
            }
        });
        return dialog;
    }
    @Override
    public void onDestroyView() {
        if (getView() instanceof ViewGroup) {
            ((ViewGroup)getView()).removeAllViews();
        }
        super.onDestroyView();
    }
    /**
     * 设置title
     */
    public void setTitle(String s) {
        this.title = s;
        if(titleTv!=null && !TextUtils.isEmpty(title)){
            titleTv.setText(title);
        }
    }

    /**
     * 设置内容
     */
    public void setContent(String s) {
        this.content = s;
    }

    /**
     * 设置内容重心，默认left
     *
     * @param gravity
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    /**
     * 设置取消按钮文字
     *
     * @param s
     */
    public void setCancelBtnText(String s) {
        this.cancel = s;
    }

    /**
     * 设置确认按钮文字
     *
     * @param s
     */
    public void setCommitBtnText(String s) {
        this.ok = s;
    }

    public interface OnCancelClickListener {
        public void onCancel();
    }

    public interface OnCommitClickListener {
        public void onCommit();
    }

    public void setOnCommitClickListener(OnCommitClickListener onCommitClickListener) {
        this.onCommitClickListener = onCommitClickListener;
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }
    public void dimissDialog(){
        dialog.dismiss();
    }
    public void showDialog(){
        dialog.show();
    }
}
