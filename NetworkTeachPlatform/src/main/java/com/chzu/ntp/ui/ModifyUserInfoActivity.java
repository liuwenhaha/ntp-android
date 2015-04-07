package com.chzu.ntp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chzu.ntp.model.User;
import com.chzu.ntp.widget.MyTitleView;

/**
 * 修改用户信息：邮箱、密码
 * @author yanxing
 */
public class ModifyUserInfoActivity extends Activity{
    private MyTitleView myTitleView;
    private EditText editText;
    private static String type;
    private static Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);
        myTitleView= (MyTitleView) findViewById(R.id.myTitleView);
        editText= (EditText) findViewById(R.id.edit_text);
        bundle=getIntent().getExtras();
        type=bundle.getString(MeInformationActivity.MODIFY_TYPE);
        if (type.equals(MeInformationActivity.MODIFY_EMAIL)){//修改邮箱
            myTitleView.setTitle("邮箱修改");
            String email=bundle.getString("email");
            editText.setText(email);
        }else if (type.equals(MeInformationActivity.MODIFY_PWD)){//密码修改
            editText.setHint("新密码");
            myTitleView.setTitle("密码修改");
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 保存
     */
    public void save(View view){
         if (view.getId()==R.id.save){
             String edit=editText.getText().toString().trim();
             if (edit.equals("")){
                 Toast.makeText(getApplicationContext(), "请输入", Toast.LENGTH_SHORT).show();
                 return;
             }
             else if (type.equals(MeInformationActivity.MODIFY_EMAIL)){//修改邮箱
                 if (User.isEmail(edit)){//输入的邮箱合法
                     String email=bundle.getString("email");
                     if (email.equals(edit)){//没有修改
                         setResult(RESULT_CANCELED);
                     }else {
                         Bundle bundle1=new Bundle();
                         bundle1.putString("email",editText.getText().toString());
                         setResult(RESULT_OK,new Intent().putExtras(bundle1));
                     }
                 }else{
                     Toast.makeText(getApplicationContext(),"邮箱格式不正确",Toast.LENGTH_SHORT).show();
                 }
             }
             else if (type.equals(MeInformationActivity.MODIFY_PWD)){//修改密码
                     Bundle bundle1=new Bundle();
                     bundle1.putString("pwd",editText.getText().toString());
                     setResult(RESULT_OK,new Intent().putExtras(bundle1));
             }
         }
    }
}
