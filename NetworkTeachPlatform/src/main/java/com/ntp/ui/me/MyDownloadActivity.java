package com.ntp.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntp.ui.R;
import com.ntp.util.PathConstant;
import com.ntp.dao.DownloadHistoryDao;
import com.ntp.util.OpenFileUtil;
import com.ntp.view.MySelectDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的下载
 * @author yanxing
 */
public class MyDownloadActivity extends Activity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private ListView myDownload;
    private ArrayAdapter<String> arrayAdapter;
    private DownloadHistoryDao downloadHistoryDao;
    private List<String> list=new ArrayList<String>();
    private static final String TAG="MyDownloadActivity";
    private static final int REQUEST=1;
    private static String fileName;//保存listView长按时item的文本

    private String fileWord[]=new String[]{".doc",".docx"};//word文件后缀
    private String fileExcel[]=new String[]{".xls",".xlsx"}; //excel文件后缀
    private String filePPT[]=new String[]{".ppt",".pptx"}; //ppt文件后缀

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_download);
        myDownload= (ListView) findViewById(R.id.myDownload);
        arrayAdapter=new ArrayAdapter<String>(this, R.layout.listview_item_mydownload,list);
        myDownload.setAdapter(arrayAdapter);
        downloadHistoryDao=new DownloadHistoryDao(getApplicationContext());
        myDownload.setOnItemClickListener(this);
        myDownload.setOnItemLongClickListener(this);
        new LoadFileTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String fileName=((TextView)view).getText().toString();
        File file=new File(PathConstant.SAVE_PATH+fileName);
        if (isFileWord(fileName)){//如果是word文件
            Intent intent=OpenFileUtil.getWordFileIntent(file);
            startActivity(intent);
        }else if (isFileExcel(fileName)){//如果是excel文件
            Intent intent=OpenFileUtil.getExcelFileIntent(file);
            startActivity(intent);
        }else if (isFilePPT(fileName)){
            Intent intent=OpenFileUtil.getPPTFileIntent(file);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"没有安装打开此类文件的应用",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        fileName=((TextView)view).getText().toString();
        Intent intent=new Intent(getApplicationContext(), MySelectDialog.class);
        intent.putExtra("itemNumber",false);
        Bundle bundle=new Bundle();
        bundle.putString("firstOption","删除");
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST);
        return true;
    }

    /**
     * 检查文件是否是Excel文件
     * @param fileName 文件名
     * @return true 是Excel文件,false不是
     */
    public boolean isFileExcel(String fileName){
        String suffixStr=fileName.substring(fileName.lastIndexOf("."),fileName.length());
        for (int i=0;i<fileExcel.length;i++){
            if (suffixStr.equals(fileExcel[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查文件是否是PPT文件
     * @param fileName 文件名
     * @return true 是PPT文件,false不是
     */
    public boolean isFilePPT(String fileName){
        String suffixStr=fileName.substring(fileName.lastIndexOf("."),fileName.length());
        for (int i=0;i<filePPT.length;i++){
            if (suffixStr.equals(filePPT[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查文件是否是word文件
     * @param fileName 文件名
     * @return true 是word文件,false不是
     */
    public boolean isFileWord(String fileName){
        String suffixStr=fileName.substring(fileName.lastIndexOf("."),fileName.length());
        for (int i=0;i<fileWord.length;i++){
            if (suffixStr.equals(fileWord[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * 读取下载的课件线程
     */
    private class LoadFileTask extends AsyncTask<Void,Void,List<String>>{

        @Override
        protected List<String> doInBackground(Void... params) {
            if (null==downloadHistoryDao){
                downloadHistoryDao=new DownloadHistoryDao(getApplicationContext());
            }
            List<String> downloadHistoryList=downloadHistoryDao.findAll();
            for (String str:downloadHistoryList){
                File file=new File(PathConstant.SAVE_PATH+str);
                if (!file.exists()){//如果下载目录文件已经不存在，则删除数据库中的记录
                    downloadHistoryDao.delete(str);
                    continue;
                }
                list.add(file.getName());
            }
            downloadHistoryDao.close();
            return list;
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            arrayAdapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_item_mydownload,list);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //删除文件
        if (requestCode==REQUEST&&resultCode==MySelectDialog.RESULT_ITEM1){
            Log.i(TAG,fileName);
            File file=new File(PathConstant.SAVE_PATH+fileName);
            file.delete();
            //从list集合中移除删除的详
            for (int i=0;i<list.size();i++){
                if (list.get(i).equals(fileName)){
                    list.remove(i);
                    break;
                }
            }
            arrayAdapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_item_mydownload,list);
            arrayAdapter.notifyDataSetChanged();
            myDownload.setAdapter(arrayAdapter);
        }
    }
}
