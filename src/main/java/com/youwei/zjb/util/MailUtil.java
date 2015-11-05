package com.youwei.zjb.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtil {

	private static final String senderAccont = "runningship@163.com";
	private static final String senderPwd = "yexinzhou@123";
//	private static final String senderPwd = "dafenqi3.0";
	public static void send_email(List<String> toList  , String subject , String content) throws IOException, AddressException, MessagingException{
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.163.com");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");
        Authenticator authenticator = new Email_Authenticator(senderAccont, senderPwd);
        javax.mail.Session sendMailSession = javax.mail.Session.getDefaultInstance(properties, authenticator);
        MimeMessage mailMessage = new MimeMessage(sendMailSession);
        mailMessage.setFrom(new InternetAddress(senderAccont));
        // Message.RecipientType.TO属性表示接收者的类型为TO
        List<InternetAddress> recips = new ArrayList<InternetAddress>();
        for(String to : toList ){
        	recips.add(new InternetAddress(to));
        }
        mailMessage.setRecipients(Message.RecipientType.TO, recips.toArray(new InternetAddress[]{}));
        mailMessage.setSubject(subject, "UTF-8");
        mailMessage.setSentDate(new Date());
        // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
        Multipart mainPart = new MimeMultipart();
        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart html = new MimeBodyPart();
        html.setContent(content.trim(), "text/html; charset=utf-8");
        mainPart.addBodyPart(html);
        mailMessage.setContent(mainPart);
        Transport.send(mailMessage);
    }
}
