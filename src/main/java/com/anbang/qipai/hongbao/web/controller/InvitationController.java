package com.anbang.qipai.hongbao.web.controller;

import com.anbang.qipai.hongbao.conf.GongZhongHaoConfig;
import com.anbang.qipai.hongbao.conf.MemberInvitationRecordState;
import com.anbang.qipai.hongbao.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.hongbao.plan.service.MemberInvitationRecordService;
import com.anbang.qipai.hongbao.util.CommonVoUtil;
import com.anbang.qipai.hongbao.util.QrCodeCreateUtil;
import com.anbang.qipai.hongbao.web.vo.CommonVO;
import com.anbang.qipai.hongbao.web.vo.InvitationQuery;
import com.google.zxing.WriterException;
import com.highto.framework.web.page.ListPage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/invitation")
public class InvitationController {

    @Autowired
    private MemberAuthService memberAuthService;

    @Autowired
    private MemberInvitationRecordService memberInvitationRecordService;

    @RequestMapping("/listInvitationRecord")
    public CommonVO listInvitationRecord(InvitationQuery query) {
        if (query.getPage() == null || query.getSize() == null) {
            query.setPage(1);
            query.setSize(10);
        }
        if (StringUtils.isBlank(query.getMemberId()) || StringUtils.isBlank(query.getState())) {
            return CommonVoUtil.lackParameter();
        }
        Map data = new HashMap();
        ListPage listPage = memberInvitationRecordService.listMemberInvitationRecordByQuery(query);
        int totalInvitationNum = (int)memberInvitationRecordService.countMemberInvitationRecordByMemberId(query.getMemberId());
        data.put("listPage", listPage);
        data.put("totalInvitationNum", totalInvitationNum);

        if (MemberInvitationRecordState.SUCCESS.equals(query.getState())) {
            data.put("validInvitationNum", listPage.getTotalItemsCount());
        } else {
            data.put("validInvitationNum", totalInvitationNum - listPage.getTotalItemsCount());
        }

        return CommonVoUtil.success(data, "success");
    }


    /**
     * 朋友圈分享url
     */
    @RequestMapping(value = "/share")
    @ResponseBody
    public CommonVO share(String token) {

        if (token == null) {
            token = "";
        }
        String memberId = memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            CommonVO vo = new CommonVO();
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }
        String url = generateImg(memberId);
        return CommonVoUtil.success(url, "success");
    }


    private String generateImg(String memberId){
        String URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
        URL += "?appid=" + GongZhongHaoConfig.APPID;
        String REDIRECT_URI = null;
        try {
            REDIRECT_URI = URLEncoder.encode("http://3cs.3cscy.com/hongbao/member/memberlogin", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        URL += "&redirect_uri=" + REDIRECT_URI;
        URL += "&response_type=code";
        URL += "&scope=snsapi_userinfo";
        URL += "&state=" + memberId;
        URL += "#wechat_redirect";

        String host = "http://47.96.20.47:100/invitation/getImg?imgName=";
        String filePath = "/data/tomcat/apache-tomcat-9.0.10/webapps/hongbao/member_invite/";
        String imgName =  memberId + ".jpg";
        String imgPath = filePath + imgName;
        File file = new File(imgPath);
        if (file.exists()) {
            return host + imgName;
        }

        // 合成二维码
        try {
            // 生成二维码
            BufferedImage qrCodeImg = QrCodeCreateUtil.createQrCode(URL, 1000);
            // 获取背景
            BufferedImage background = ImageIO.read(new File("/data/tomcat/apache-tomcat-9.0.10/webapps/hongbao/background0.jpg"));
            // 合成图片
            QrCodeCreateUtil.mergeImag(background, qrCodeImg, 219, 694, 201, 201);

            // 保存
            OutputStream out = new FileOutputStream(file);
            ImageIO.write(background, "jpg", out);
            out.close();

            return host + imgName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping("/getImg")
    public void getImg (String imgName, HttpServletResponse response) {
        String filePath = "/data/tomcat/apache-tomcat-9.0.10/webapps/hongbao/member_invite/";
        String imgPath = filePath + imgName;
        File file = new File(imgPath);
        try {
            BufferedImage image = ImageIO.read(new File(imgPath));

            if (file.exists()) {
                ImageIO.write(image, "jpg", response.getOutputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------
//    public static void main(String[] args) {
//        try {
//            String URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
//            URL += "?appid=" + GongZhongHaoConfig.APPID;
//            String REDIRECT_URI = null;
//            try {
//                REDIRECT_URI = URLEncoder.encode("http://3cs.3cscy.com/hongbao/member/memberlogin", "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            URL += "&redirect_uri=" + REDIRECT_URI;
//            URL += "&response_type=code";
//            URL += "&scope=snsapi_userinfo";
//            URL += "&state=" + "123";
//            URL += "#wechat_redirect";
//
//            //二维码
//            BufferedImage qrCodeImg = QrCodeCreateUtil.createQrCode(URL, 1000);
//            // 获取背景
//            BufferedImage background = ImageIO.read(new File("C:/Users/YaphetS/Desktop/background0.jpg"));
//            // 合成图片
//            QrCodeCreateUtil.mergeImag(background, qrCodeImg, 219, 695, 201, 201);
//            File file = new File("C:/Users/YaphetS/Desktop/test.jpg");
//            OutputStream out = new FileOutputStream(file);
//            ImageIO.write(background, "jpg", out);
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
