package SendAndReceive;

import MyUtils.OtherMethod;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//接收文件
public class Server implements Runnable{
    //服务端程序需要事先启动等待链接
    private String targetPath;

    private int port = 34567;


    private ExecutorService executorService = Executors.newCachedThreadPool();

    public String getTargetPath() {
        return targetPath;
    }

    public int getPort() {
        return port;
    }

    public Server(String targetPath){
        //默认使用34567端口
        this.targetPath = targetPath;
    }

    //如果要特殊指定端口时使用的构造方法
    public Server(String targetPath, int por){
        this.targetPath = targetPath;
        this.port = port;
    }

    @Override
    public void run(){
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel socketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            //调整serverSocketChannel为非阻塞状态
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(this.port));
            OtherMethod.inform("等待链接");
            while (true){
                socketChannel = serverSocketChannel.accept();
                if(socketChannel!=null){
                    //接收文件
                    Receiver receiver = new Receiver(socketChannel,this.targetPath);
                    //直接让线程池处理后续操作
                    //该线程则继续监听端口,如果有新的请求则会继续调用线程
                    executorService.execute(receiver);
                }
            }
        } catch (IOException e) {
                e.printStackTrace();
                OtherMethod.warning("错误！");
        }finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    OtherMethod.warning("socketChannel关闭错误");
                }
            }
            if (serverSocketChannel!= null) {
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    OtherMethod.warning("serverSocketChannel关闭错误");
                }
            }

        }
    }

}