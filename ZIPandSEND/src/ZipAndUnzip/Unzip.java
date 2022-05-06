package ZipAndUnzip;

import MyUtils.OtherMethod;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.*;


public class Unzip implements Runnable{
    private String SourcePath;  //要解压的zip的绝对路径
    private String TargetPath;  //要解压到的地址
    private String Name;        //解压名称

    public String getSourcePath() {
        return SourcePath;
    }

    public void setSourcePath(String sourcePath) {
        SourcePath = sourcePath;
    }

    public String getTargetPath() {
        return TargetPath;
    }

    public void setTargetPath(String targetPath) {
        TargetPath = targetPath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    //构造方法
    public Unzip(String SourcePath, String TargetPath, String Name){
        this.SourcePath = SourcePath;
        this.TargetPath = TargetPath;
        this.Name = Name;
    }

    //解压缩方法
    @Override
    public void run(){
        this.TargetPath = this.TargetPath+"\\"+this.Name;
        long time1 = System.currentTimeMillis();
        //创建zip输入流和zipEntry
        File SourceFile = new File(SourcePath);
        ZipFile zipFile = null;
        FileInputStream fileInputStream = null;
        ZipInputStream zipInputStream = null;
        try {
            zipFile = new ZipFile(SourceFile, Charset.forName("GBK"));
            fileInputStream = new FileInputStream(SourceFile);
            zipInputStream = new ZipInputStream(fileInputStream, Charset.forName("GBK"));
            ZipEntry zipEntry = null;
            //创建父文件夹
            CreateDirectory();
            //进行解压缩
            Unzipit(zipEntry,zipInputStream,zipFile);
        }catch(IOException e){
            e.printStackTrace();
            OtherMethod.warning("解压缩进程出错！");
        }
        finally {
            //关闭输入输出流，防止资源泄露
            if(zipInputStream!=null){
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    OtherMethod.warning("zipInputStream关闭错误");
                }
            }
            if (fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    OtherMethod.warning("fileInputStream关闭错误");
                }
            }
        }
        long time2 = System.currentTimeMillis();
        OtherMethod.inform("解压缩完成！总用时："+OtherMethod.formatTime(time2-time1));
    }

    //创建父文件夹
    void CreateDirectory(){
        File file = new File(this.TargetPath);
        if(!file.exists()){
            if(file.mkdir()){
                System.out.println("父文件夹"+file.getPath()+"创建成功！");
            }
        }
    }

    //最后一位是'/'就认为是文件夹
    boolean isDirectory(ZipEntry zipEntry){
        String s = zipEntry.getName();
        System.out.println(s);
        return s.charAt(s.length() - 1) == 92;
    }

    //检查父文件夹是否全部创建完毕,如果没有创建完毕则创建
    void createPath(String parentPath){
        File parent = new File(parentPath);
        if(parent.getParent()!=null){
            createPath(parent.getParent());
        }
        parent.mkdir();
    }

    void Unzipit(ZipEntry zipEntry,ZipInputStream zipInputStream,ZipFile zipFile){
        try {
            //结束条件
            if((zipEntry = zipInputStream.getNextEntry())==null){
                return;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        long time1 = System.currentTimeMillis();
        System.out.println(this.TargetPath+"\\"+zipEntry.getName());
        File file = new File(this.TargetPath+"\\"+zipEntry.getName());
        //如果是文件夹就创建
        System.out.println("正在解压缩:"+zipEntry.getName());
       createPath(file.getParent());
        //如果是文件就进行解压操作
        if(isDirectory(zipEntry)){
            if(!file.exists()){
                if(!file.mkdir()){
                    OtherMethod.warning("文件夹建立失败！");
                    return;
                }
            }
        }
        else {
            InputStream inputStream = null;
            BufferedInputStream bufferedInputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                System.out.println(file.getPath());
                if (!file.exists()) {
                    if(!file.createNewFile()){
                        OtherMethod.warning("文件创建失败！");
                        return;
                    }
                }
                inputStream = zipFile.getInputStream(zipEntry);
                bufferedInputStream = new BufferedInputStream(inputStream);
                fileOutputStream = new FileOutputStream(file);
                byte[] b = new byte[1024];//一次写1024字节
                while (bufferedInputStream.read(b) != -1) {
                    fileOutputStream.write(b);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        long time2 = System.currentTimeMillis();
        System.out.println("解压缩"+zipEntry.getName()+"完毕，用时："+(time2-time1)+"ms");
        Unzipit(zipEntry,zipInputStream,zipFile);
    }


}