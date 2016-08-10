package learn.example.pile;

import android.content.Context;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/8/6.
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private String dirName="exception_catch";
    private File mDir;

    private Thread.UncaughtExceptionHandler androidDefExceptionHandler;

    public ExceptionHandler(Context context) {
        androidDefExceptionHandler=Thread.getDefaultUncaughtExceptionHandler();

        File externalFile=context.getExternalFilesDir(null);
        mDir=new File(externalFile,dirName);
        if (!mDir.exists())
        {
           if (!mDir.mkdir()){
               mDir=null;
           }
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
          if (mDir!=null)
          {
              long len=0;
              for (File file:mDir.listFiles())
              {
                  len+=file.length();
              }
              if (len>=10485760)//10M
              {
                  for (File file:mDir.listFiles())
                  {
                      file.delete();
                  }
              }

              String name=TimeUtil.formatTimeFull(TimeUtil.getTime()/1000);
              File newFile=new File(mDir,name);
              try {
                  PrintStream stream=new PrintStream(newFile,"utf-8");
                  ex.printStackTrace(stream);
              } catch (FileNotFoundException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
         //交给系统处理
         androidDefExceptionHandler.uncaughtException(thread,ex);
    }
}
