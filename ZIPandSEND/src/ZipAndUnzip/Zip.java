package ZipAndUnzip;

import MyUtils.OtherMethod;

import java.io.*;
import java.nio.file.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip implements Runnable{
    //几点注意：
    //1.关闭流的时候要自下而上逐次关闭
    //2.压缩方法要有递归实现
    private String SourcePath;  //文件源绝对路径
    private String TargetPath;  //zip压缩包绝对路径的父文件夹
    private String ZipName;        //压缩包名
    //get&set方法
    public String getSourcePath() {
        return SourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.SourcePath = sourcePath;
    }

    public String getTargetPath() {
        return TargetPath;
    }

    public void setTargetPath(String targetPath) {
        this.TargetPath = targetPath;
    }

    public String getDirectoryName() {
        return ZipName;
    }

    public void setDirectoryName(String name) {
        this.ZipName = name;
    }

    public Zip(String sourcePath,String targetPath,String name){
        this.SourcePath = sourcePath;
        this.TargetPath = targetPath+'\\'+name+".zip";
        this.ZipName = name;
    }

    //压缩方法
    @Override
    public void run(){
        long time1 = System.currentTimeMillis();
        //创建zip输出流
        OutputStream outputStream = null;
        CheckedOutputStream checkedOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        try {
            outputStream = new FileOutputStream(this.TargetPath);
            checkedOutputStream = new CheckedOutputStream(outputStream, new CRC32());
            zipOutputStream = new ZipOutputStream(checkedOutputStream);
            //创建源File对象
            File sourceFile = new File(this.SourcePath);
            //递归压缩
            Zipit(sourceFile,zipOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            OtherMethod.warning("压缩进程错误！");
        }finally {
            //依次关闭输出流
            if (zipOutputStream!=null){
                try {
                    zipOutputStream.finish();
                } catch (IOException e) {
                    e.printStackTrace();
                    OtherMethod.warning("流关闭错误！");
                }
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    OtherMethod.warning("流关闭错误！");
                }
            }
            if(checkedOutputStream!=null){
                try {
                    checkedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    OtherMethod.warning("流关闭错误！");
                }
            }
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    OtherMethod.warning("流关闭错误！");
                }
            }
        }
        long time2 = System.currentTimeMillis();
        OtherMethod.inform("压缩完毕，总用时："+OtherMethod.formatTime(time2-time1));
    }

    //需要调用该类的字段SourcePath，所以设为private,不要拿到其他类中用
    private String getRelativePath(File file){
        File source = new File(this.SourcePath);
        String DirectoryName = source.getName();
        //获取文件路径在文件夹名之后的字串
        return file.getPath().substring(file.getPath().indexOf(DirectoryName));
    }

    //递归压缩
    public void Zipit(File File,ZipOutputStream zipOutputStream){
        System.out.println("正在压缩："+File.getName());
        long time1 = System.currentTimeMillis();
        //是文件夹，进行递归压缩
        if(File.isDirectory()) {
            try {
                ZipEntry zipEntry = new ZipEntry(getRelativePath(File)+"\\");
                //错误语句：thr.putNextEntry(new ZipEntry(sourceFile.getPath()));
                zipOutputStream.putNextEntry(zipEntry);
                //使用PPT"Java文件与文件夹操作"中提供的方法遍历子文件
                Path path = Paths.get(File.getPath());
                DirectoryStream<Path> children = Files.newDirectoryStream(path);
                for(Path child:children){
                    //    System.out.println(child.toString());
                    File file = new File(child.toString());
                    Zipit(file,zipOutputStream);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        //是文件，直接压缩
        else{
            InputStream inputStream = null;
            BufferedInputStream bufferedInputStream = null;
            try{
                ZipEntry zipEntry = new ZipEntry(getRelativePath(File));
                zipOutputStream.putNextEntry(zipEntry);
                inputStream = new FileInputStream(File);
                bufferedInputStream = new BufferedInputStream(inputStream);
                //字节数组,每次读取1024个字节
                byte[] b = new byte[1024];
                //循环读取，边读边写
                while(bufferedInputStream.read(b)!=-1)
                {
                    zipOutputStream.write(b);//写入压缩文件
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if (inputStream != null){
                    try {
                        inputStream.close();
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
            }

        }
        long time2 = System.currentTimeMillis();
        System.out.println("压缩"+File.getName()+"用时："+(time2-time1)+"ms");
    }

}