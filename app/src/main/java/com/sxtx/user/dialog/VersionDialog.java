package com.sxtx.user.dialog;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sxtx.user.R;
import com.sxtx.user.dialog.base.PBaseDialog;

public class VersionDialog extends PBaseDialog {

    private TextView tvContent;
    private TextView tvTitle;



    public VersionDialog(Context context, final View.OnClickListener onClickListener) {
        super(context);
        canceledOnTouchOutside(false);
        setCancelable(false);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog_version = inflater.inflate(R.layout.dialog_version, null);
        contentView(dialog_version);
        setDialogSize(dialog_version,context);

        tvContent = this.findViewById(R.id.tv_content);
        tvTitle=this.findViewById(R.id.tv_title);
        this.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(view);
                }
            }
        });
   }

   public VersionDialog setContent(String content,String title) {
        tvContent.setText(content);
       tvTitle.setText(title);
        return this;
    }


}

