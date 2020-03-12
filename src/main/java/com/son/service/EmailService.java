package com.son.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ResourceUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private final Configuration freemarker;
    private final JavaMailSender javaMailSender;

    public EmailService(Configuration freemarker, JavaMailSender javaMailSender) {
        this.freemarker = freemarker;
        this.javaMailSender = javaMailSender;
    }

    @Value("${son.url}")
    private String appUrl;

    public void sendWelcomeMail() {
        Map<String, Object> model = new HashMap<>();
        model.put("username", "sonthh.jpeg");
        model.put("link", appUrl + "/explore");
        try {
            Template template = freemarker.getTemplate("emails/welcome.ftl");
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("tranhuuhongson@gmail.com");
            mimeMessageHelper.setTo("tranhuuhongson+welcome@gmail.com");
            mimeMessageHelper.setSubject("Welcome sonthh!");
            mimeMessageHelper.setText(text, true);

            // file attachments
            File file = ResourceUtils.getFile("classpath:static/images/sonthh.jpeg");
            mimeMessageHelper.addAttachment("sondeptrai.png", file);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
