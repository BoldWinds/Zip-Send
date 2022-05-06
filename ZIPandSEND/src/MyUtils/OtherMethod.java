package MyUtils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.File;
import java.util.regex.Pattern;

//这个类用来存储一些静态方法
public class OtherMethod {
    //检查文件夹名称是否合法
    public static boolean nameCheck(String s){
        char c;
        int n=s.length();
        for (int i=0;i<n;i++){
            c = s.charAt(i);
            //文件的名称不能包含下列字符
            if(c=='\\'||c=='/'||c==':'||c=='*'||c=='?'||c=='"'||c=='<'||c=='>'||c=='|'){
                return false;
            }
        }
        return true;
    }

    //弹出警告框
    public static void warning(String s){
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.titleProperty().set("错误");
                alert.headerTextProperty().set(s);
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //弹出消息提示框
    public static void inform(String s){
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.titleProperty().set("消息");
                alert.headerTextProperty().set(s);
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //获取路径对应文件的扩展名
    public static String getExtensionName(String sourcePath){
        int len = sourcePath.length();
        int mark = len-1;
        for(int i=len-1;i>=0;i--){
            if(sourcePath.charAt(i)=='.'){
                mark = i;
                break;
            }
        }
        return sourcePath.substring(mark+1,len);
    }

    //利用正则表达式判断IP地址是否合法
    public static boolean checkIP(String ipAddress){
        String regex = "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])";
        return Pattern.matches(regex,ipAddress);
    }

    //计算文件夹大小
    public static long countSize(String sourcePath){
        File file = new File(sourcePath);
        return countSize(file);
    }

    //计算文件（夹）大小并返回long类型的大小值
    public static long countSize(File file){
        long size = 0;
        if(!file.exists()){
            return 0;
        }
        else{
            if(file.isDirectory()) {
                String[] list = file.list();
                if (file.isDirectory()) {
                    for (String items : list) {
                        String subItem = file.getPath() + File.separator + items;
                        //递归调用.
                        size+=countSize(subItem);
                    }
                }
            }
            else{
                size +=  file.length();
            }
        }
        return size;
    }

    //将文件大小格式化
    public static String formatSize(long size){
        float format;
        float floatSize;
        floatSize = (float) size;
        if(size<1024){
            format = floatSize;
            return format + "B";
        }
        else if(size<1024*1024){
            format = floatSize/1024;
            return format+"KB";
        }
        else if(size<1024*1024*1024){
            format = floatSize/(1024*1024);
            return format + "MB";
        }
        else{
            format = floatSize/(1024*1024*1024);
            return format + "GB";
        }
    }

    //格式化时间
    public static String formatTime(long time){
        float floatTime = (float) time;
        if(floatTime<1000){
            return floatTime+"ms";
        }
        else if(floatTime<1000*60){
            return floatTime/1000 + "s";
        }
        else {
            return floatTime/(1000*60)+"min";
        }
    }

    //预估压缩需要的时间
    public static String zipTime(long size){
        long time = size/35000;
        return formatTime(time);
    }

    //预估解压缩需要的时间
    public static String unzipTime(long size){
        long time = size/100000;
        return formatTime(time);
    }
}
