package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        return "upload";
    }
        // 因为uploadPage.jsp 在WEB-INF下，不能直接从浏览器访问，所以要在这里加一个uploadPage跳转，这样就可以通过
        @RequestMapping("/uploadPage")
        public String uploadPage() {
            return "uploadPage";   //过度跳转页
        }

        @PostMapping("/upload") // 等价于 @RequestMapping(value = "/upload", method = RequestMethod.POST)
        public String uplaod(HttpServletRequest req, @RequestParam("file") MultipartFile file, Model m) {//1. 接受上传的文件  @RequestParam("file") MultipartFile file
            try {
                //2.根据时间戳创建新的文件名，这样即便是第二次上传相同名称的文件，也不会把第一次的文件覆盖了
                String fileName = System.currentTimeMillis() + file.getOriginalFilename();
                //3.通过req.getServletContext().getRealPath("") 获取当前项目的真实路径，然后拼接前面的文件名
                //String destFileName = req.getServletContext().getRealPath("") + "uploaded" + File.separator + fileName;
                String destFileName = "D:/image/uploaded" + File.separator + fileName;
                //4.第一次运行的时候，这个文件所在的目录往往是不存在的，这里需要创建一下目录（创建到了webapp下uploaded文件夹下）
                File destFile = new File(destFileName);
                destFile.getParentFile().mkdirs();
                //5.把浏览器上传的文件复制到希望的位置
                file.transferTo(destFile);
                //6.把文件名放在model里，以便后续显示用
                m.addAttribute("fileName", fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            return "/showImg";
        }

    @RequestMapping("/showImg")
    public String showImg(Model m) {
        m.addAttribute("fileName", "1564559100293测试图片2.png");
        return "showImg";   //查看图片
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";   //测试hello
    }

    @RequestMapping("/showDownloadFile")
    public String showDownloadFile() {
        return "downloadFile";   //图片下载
    }

    @RequestMapping("/downloadOnlineLearnMaterials")
    public String downloadFile(String fileName,HttpServletRequest request, HttpServletResponse response) {
       // String fileName = "1564558920986.jpg";// 设置文件名，根据业务需要替换成要下载的文件名
        if (fileName != null) {
            //设置文件路径
            String realPath = "D:/image/uploaded";
            File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
}