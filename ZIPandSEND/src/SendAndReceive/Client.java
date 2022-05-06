package SendAndReceive;

import MyUtils.OtherMethod;
import ZipAndUnzip.Zip;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

//发送文件
public class Client implements Runnable{
    private String address;

    private String sourcePath;      //要发送的文件夹的路径

    private int port = 34567;

    public String getSourcePath() {
        return sourcePath;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    //IP为服务端的ip地址
    public Client(String address, String sourcePath){
        //默认使用34567端口
        this.address = address;
        this.sourcePath = sourcePath;
    }

    //如果要特殊指定端口时使用的构造方法
    public Client(String address, String sourcePath, int port){
        this.address = address;
        this.sourcePath = sourcePath;
        this.port = port;
    }

    @Override
    public void run(){
        if(!check()){
            OtherMethod.warning("您输入的信息有误，请重新输入!");
            return;
        }

        //创建socket和file通道
        SocketChannel socketChannel = null;
        FileChannel fileChannel = null;
        //创建输入流与输出流
        FileInputStream fileInputStream = null;
        try{
            //打开socket通道
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            //建立链接
            socketChannel.connect(new InetSocketAddress(this.address,this.port));

            //如果没有链接成功就一直在下面的while中循环
            while(!socketChannel.finishConnect()){
            }
            //如果服务器拒绝了请求就直接退出
            if(!getReply(socketChannel)){
                OtherMethod.warning("服务端拒绝了您的请求！");
                return;
            }
            //服务器接受请求，准备发送文件
            OtherMethod.inform("服务端接受了您的请求！");
            //判断要发送的文件是否是zip类，如果不是那就进行压缩
            if(!OtherMethod.getExtensionName(sourcePath).equals("zip")){
                File file = new File(sourcePath);
                Zip zip = new Zip(sourcePath,file.getParent(),"result");
                zip.run();
                this.sourcePath = file.getParent()+"\\result.zip";
            }
            fileInputStream = new FileInputStream(sourcePath);
            fileChannel = fileInputStream.getChannel();
            //开始向外写出
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);//分配1024
            while(fileChannel.read(byteBuffer)!=-1){//只要byteBuffer中还有未读内容就进行循环
                byteBuffer.flip();
                //System.out.println("writing");
                while(byteBuffer.hasRemaining()){
                    socketChannel.write(byteBuffer);
                }
                byteBuffer.clear();
            }
            OtherMethod.inform("文件发送完毕！");
        }catch (IOException e){
            e.printStackTrace();
            OtherMethod.warning("发送错误！");
        } catch (Exception e){
            e.printStackTrace();
            OtherMethod.warning("服务端拒绝了您的请求");
        }
        finally {
            if (socketChannel!=null){
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    OtherMethod.warning("socketChannel关闭错误！");
                }
            }
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
                OtherMethod.warning("fileInputStream关闭错误！");
            }
        }

    }

    //向服务端发送请求并返回服务端是否接受请求
    public boolean getReply(SocketChannel socketChannel){
        //channel只能发送ByteBuffer，应该就没法发送字符串了，所以用socket得了
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String reply = null;
        try {
            serverSocket = new ServerSocket(34567);
            socket = serverSocket.accept();
            inputStream = socket.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            while (reply==null){
                reply = bufferedReader.readLine();
            }
            System.out.println(reply);
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
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
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (serverSocket != null) {
                try {
                    serverSocket.close();
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

    private boolean check(){
        File file = new File(this.sourcePath);
        //要发送的文件确认
        if(!file.exists()){
            return false;
        }
        //端口确认
        if(this.port>65535||this.port<0){
            return false;
        }
        //使用正则表达式判断IP地址是否合法
        if(this.address.length()>0&&this.address.length()<16){
            return OtherMethod.checkIP(this.address);
        }
        return true;
    }


}