package SendAndReceive;

import MyUtils.OtherMethod;
import ZipAndUnzip.Unzip;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Receiver implements Runnable {

    private SocketChannel socketChannel;

    private String targetPath;

    Receiver(SocketChannel socketChannel, String targetPath) {
        this.socketChannel = socketChannel;
        this.targetPath = targetPath;
    }

    @Override
    public void run() {
        //如果接受了就开始接收文件，否则直接return
        if (sendBack()) {
            receive();
        }
    }

    //接收客户端的某个请求字符串并返回是否接受
    public boolean sendBack() {
        String targetIP = socketChannel.socket().getInetAddress().toString().substring(1);
        System.out.println(targetIP);
        Socket socket = null;
        OutputStream outputStream = null;
        PrintWriter printWriter = null;
        String reply = null;
        try {
            // 定义一个FutureTask，然后 Plateform.runLater() 这个futuretask
            final FutureTask<String> query = new FutureTask<String>(new Callable<String>() {
                @Override
                public String call(){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.titleProperty().set("确认");
                    alert.headerTextProperty().set("您是否要接受来自"+targetIP+"的请求");
                    Optional<ButtonType> result = alert.showAndWait();
                    String reply = null;
                    if (result.get()==ButtonType.OK){
                        reply = "accept";
                    }
                    else {
                        reply = "reject";
                    }
                    return reply;
                }
            });
            Platform.runLater(query);
            // 这样就能获取返回值
            reply = query.get();
            System.out.println(reply);
            //将reply发送给客户端
            socket= new Socket(targetIP,34567);
            outputStream = socket.getOutputStream();
            printWriter = new PrintWriter(outputStream);
            printWriter.write(reply);
            printWriter.flush();
            socket.shutdownOutput();
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {//关闭
            if (printWriter != null) {
                printWriter.close();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(reply.equals("accept")){
            return true;
        }
        return false;
    }

    //接收文件
    public void receive() {
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        try {
            if (socketChannel != null) {
                fileOutputStream = new FileOutputStream(targetPath + "\\Test.zip");
                fileChannel = fileOutputStream.getChannel();
                //创建缓冲
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                //循环读取来确保buffer中的内容被读完
                while (socketChannel.read(byteBuffer) != -1) {
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) {
                        fileChannel.write(byteBuffer);
                    }
                    byteBuffer.clear();
                }
                //这里正常调用方法即可，多线程会出错
                Unzip unzip = new Unzip(targetPath + "\\Test.zip", targetPath, "result");
                unzip.run();
                //压缩完毕之后应删除相应压缩包
                File file = new File(targetPath + "\\Test.zip");
                if (file.exists()) {
                    if (!file.delete()) {
                        System.out.println("删除失败");
                    }
                }
                OtherMethod.inform("来自" + socketChannel.socket().getInetAddress() + "的文件接收完成");
            }
        } catch (IOException e) {
            e.printStackTrace();
            OtherMethod.warning("在接受来自" + socketChannel.socket().getInetAddress() + "的文件时出错！");
        } finally {
            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    OtherMethod.warning("fileChannel关闭错误");
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    OtherMethod.warning("fileOutputStream关闭错误");
                }
            }
        }
    }
}
