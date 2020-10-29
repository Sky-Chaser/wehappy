package cn.chasers.wehappy.sms.service;

/**
 * 发送邮件的服务接口
 *
 * @author lollipop
 */
public interface IMailService {
    /**
     * 发送文本邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param cc      抄送地址
     */
    void sendSimpleMail(String to, String subject, String content, String... cc);

    /**
     * 发送注册验证码邮件
     *
     * @param to   邮件地址
     * @param code 验证码
     */
    void sendRegisterCode(String to, String code);
}
