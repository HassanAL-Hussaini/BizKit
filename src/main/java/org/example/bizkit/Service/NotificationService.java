//package org.example.bizkit.Service;
//
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//
//public class NotificationService {
//    private final JavaMailSender javaMailSender;
//    /**
//     * إرسال إيميل بسيط (بدون HTML)
//     *  to إيميل المستلم
//     *  subject عنوان الرسالة
//     *  body نص الرسالة
//     */
//    public void sendSimpleEmail(String to, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//
////      message.setFrom(System.getenv("MAIL_USERNAME")); // يطلع اسم المرسل
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//
//        javaMailSender.send(message);
//    }
//}


package org.example.bizkit.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    /**simple text without HTML template*/
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);
            mailSender.send(msg);
        } catch (MailException ex) {
            // سجّل ولا تفجّر: ما نبغى نخرب منطق الطلب بسبب فشل إيميل
            System.err.println("Failed to send simple email: " + ex.getMessage());
        }
    }

    /** HTML */
    public void sendHtmlEmail(String to, String subject, String htmlBody, @Nullable String... cc) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(to);
            if (cc != null && cc.length > 0) helper.setCc(cc);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true => HTML
            mailSender.send(mimeMessage);
        } catch (MessagingException | MailException ex) {
            System.err.println("Failed to send HTML email: " + ex.getMessage());
        }
    }

    /** قالب HTML بسيط للطلبات */
    public String buildOrderHtml(String title, String greeting, String body, String ctaText, String ctaUrl, String footer) {
        return """
            <!doctype html>
            <html lang="en">
            <head>
              <meta charset="UTF-8" />
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
              <title>%s</title>
            </head>
            <body style="margin:0;padding:0;background:#f6f7fb;font-family:Arial,Helvetica,sans-serif;">
              <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background:#f6f7fb;padding:24px 0;">
                <tr>
                  <td align="center">
                    <table role="presentation" width="600" cellspacing="0" cellpadding="0" style="background:#ffffff;border-radius:12px;overflow:hidden">
                      <tr>
                        <td style="background:#111827;color:#fff;padding:20px 24px;font-size:18px;font-weight:700;">
                          %s
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:24px;color:#111827;">
                          <p style="margin:0 0 12px 0;font-size:16px;">%s</p>
                          <p style="margin:0 0 16px 0;font-size:14px;color:#374151">%s</p>
                          <p style="margin:0 0 24px 0;">
                            <a href="%s" style="display:inline-block;background:#2563eb;color:#ffffff;text-decoration:none;padding:10px 16px;border-radius:8px;font-weight:600;">
                              %s
                            </a>
                          </p>
                          <p style="margin:0;color:#6b7280;font-size:12px">%s</p>
                        </td>
                      </tr>
                      <tr>
                        <td style="background:#f3f4f6;padding:16px;color:#6b7280;text-align:center;font-size:12px">
                          © %d BizKit
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.formatted(title, title, greeting, body, ctaUrl, ctaText, footer, java.time.Year.now().getValue());
    }
}

