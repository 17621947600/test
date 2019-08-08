package com.example.demo.util;

import java.io.*;

import javax.servlet.http.HttpServletRequest;

/**
 * upload Img Utils
 *
 * @author admin
 *
 */
public class UpImgUtils {

    /*
     * 思路 1、从request当中获取流信息
     *  2、新建一个临时文件,用输出流指向这个文件
     *  3、关闭流
     */
    public static void keepFile(HttpServletRequest request) throws IOException {

        // 1、从request当中获取流信息
        InputStream fileSource = request.getInputStream();

        /*
         * 临时文件的存储路径(我们在webContent下新建一个temp文件夹，发布项目的时候很可能因为temp为空，
         * 没在tomcat中建立一个文件夹，到时候自己在发布的项目中添加一个即可)
         */
       // String tempFileName = request.getServletContext().getRealPath("/") + "temp/tempfile.txt";
        String tempFileName = "D:/image/temp/tempfile.txt";
        //2、新建一个临时文件,用输出流指向这个文件

        //建一个文件
        File tempFile = new File( tempFileName );

        //用输出流指向这个文件

        FileOutputStream outputStream = new FileOutputStream( tempFile );

        //我们就每次读写10K,我们的文件小，这个就已经够用了
        byte[] b = new byte[1024*10];

        int n = 0 ;

        //读写文件,-1标识为空
        while( (n = fileSource.read(b) ) != -1 ) {
            outputStream.write(b, 0, n);
        }

        //3、关闭流
        fileSource.close();
        outputStream.close();

        //第二部分......................................................
        /**
         * 思路
         * 1、获取文件的名称，并解决中文乱码
         * 2、获取文件的内容
         * 3、保存文件
         */

        //第二部分 1、获取文件的名称，并解决中文乱码

        RandomAccessFile randomFile = new RandomAccessFile(tempFile,"r");
        randomFile.readLine();//先读取一行
        String str = randomFile.readLine();//读取第二行
        int beginIndex = str.lastIndexOf("filename=\"") + 10;//定位到文件名开始的地方
        int endIndex = str.lastIndexOf("\"");//定位到文件名结尾的地方

        String filename = str.substring(beginIndex, endIndex);

        //判断文件名是全路径名还是只是文件名（google和火狐是只是文件名，微软系列是全路径名）
        endIndex = filename.lastIndexOf("\\") + 1;
        if( endIndex > -1 ) {
            filename = filename.substring(endIndex);
        }
        //经过上面的这几步，我们就已经获取到了文件名，我们还需要解决一下中文名乱码的问题

        //解决上传文件中文名字乱码
        filename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");

        System.out.println("filename: " + filename );

        //第二部分 2、获取文件的内容
        //重新定位文件指针到文件头
        randomFile.seek(0);
        long startPosition = 0L;//正文开始的位置
        int i = 1;
        while( ( n = randomFile.readByte() ) != -1 && i <=4 ) {
            if( n == '\n') {
                startPosition = randomFile.getFilePointer();
                i++;
            }
        }
        //
        startPosition = randomFile.getFilePointer() - 1 ;

        //获取文件内容，结束位置
        randomFile.seek(randomFile.length() );//指针定位到尾部
        long endPosition = randomFile.getFilePointer();
        int j = 1;
        while( endPosition >= 0 && j <=2 ) {
            endPosition--;
            randomFile.seek(endPosition);
            if(randomFile.readByte() == '\n' ) {
                j++;
            }
        }

        endPosition = endPosition - 1;

        //第二部分 3、保存文件
        //设置保存上传文件的路径，我们好保存到temp中
        String realPath = request.getServletContext().getRealPath("/") + "temp";
        File fileupload = new File( realPath );

        File saveFile = new File(realPath,filename);
        RandomAccessFile randomAccessFile = new RandomAccessFile(saveFile,"rw");
        //
        //从临时文件当中读取文件内容（根据起止位置获取）
        randomFile.seek(startPosition);
        while(startPosition < endPosition ) {
            randomAccessFile.write(randomFile.readByte());
            startPosition = randomFile.getFilePointer();
        }
        //
        //关闭输入输出流、删除临时文件
        randomAccessFile.close();
        randomFile.close();
        //tempFile.delete();

    }
}
