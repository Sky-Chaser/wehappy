package cn.chasers.wehappy.sms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 发送邮件服务的实现类
 *
 * @author lollipop
 */
@Service
public class MailServiceImpl implements IMailService {
    private final JavaMailSender mailSender;

    private final String QUOTATION = "\"";

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendSimpleMail(String to, String subject, String content, String... cc) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailMessage.setCc(cc);
        mailSender.send(mailMessage);
    }

    @Override
    public void sendRegisterCode(String to, String code) {
        if (to.contains(QUOTATION)) {
            to = to.substring(1, to.length() - 1);
        }

        sendSimpleMail(to, "wehappy 注册验证码", code);
    }
}
