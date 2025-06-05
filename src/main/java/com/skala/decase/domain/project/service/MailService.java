package com.skala.decase.domain.project.service;

import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.domain.ProjectInvitation;
import com.skala.decase.domain.project.exception.ProjectException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final static String MAIL_SUBJECT_CONTENT = "Decase 프로젝트 초대 메일입니다.";
    private final static String MAIL_WELCOME_CONTENT = "Decase와 함께 하시는 것을 진심으로 환영합니다.";
    private final static String WEB_URL = "http://localhost:8080.com/invitations/";

    /*
        메일 전송
     */

    public void sendMail(ProjectInvitation projectInvitation) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, false, "UTF-8");

            mimeMessageHelper.setTo(projectInvitation.getEmail()); // 수신자
            mimeMessageHelper.setSubject(MAIL_SUBJECT_CONTENT); // 제목

            String content = getContent(projectInvitation);
            mimeMessageHelper.setText(content, true); // 내용

            javaMailSender.send(mimeMailMessage);
        } catch (MessagingException e) {
            throw new ProjectException("수신자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private String getContent(ProjectInvitation projectInvitation) {
        String link = WEB_URL + projectInvitation.getToken();

        return """
            <html>
            <body>
                <p>안녕하세요,</p>

                <p><strong>Decase</strong> 프로젝트에 초대되었습니다.</p>

                <p>
                    아래 링크를 클릭하시면 프로젝트에 참여하실 수 있습니다:<br/>
                    <a href="%s">프로젝트 초대 수락하기</a>
                </p>

                <p>감사합니다.<br/>Decase 팀 드림</p>
            </body>
            </html>
            """.formatted(link);
    }

    public void sendWelcomeMail(Member newMember, Project project) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, false, "UTF-8");

            mimeMessageHelper.setTo(newMember.getEmail()); // 수신자
            mimeMessageHelper.setSubject(MAIL_WELCOME_CONTENT); // 제목

            String content = getWelcomeContent(newMember, project);
            mimeMessageHelper.setText(content, true); // 내용

            javaMailSender.send(mimeMailMessage);
        } catch (MessagingException e) {
            throw new ProjectException("수신자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private String getWelcomeContent(Member member, Project project) {
        return String.format(
                "%s님,\n\n" +
                        "안녕하세요, Decase입니다.\n\n" +
                        "귀하께서 '%s' 프로젝트에 성공적으로 참여하셨음을 알려드립니다.\n" +
                        "앞으로 Decase와 함께하시며 많은 성과와 발전이 있기를 진심으로 기원합니다.\n\n" +
                        "프로젝트와 관련된 문의 사항이나 도움이 필요하시면 언제든지 연락 주시기 바랍니다.\n\n" +
                        "감사합니다.\n" +
                        "Decase 드림",
                member.getName(),
                project.getName()
        );
    }
}
