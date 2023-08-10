package kopo.poly.service.impl;

import kopo.poly.dto.MailDTO;
import kopo.poly.service.IMailService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MailService implements IMailService {

    private final JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}")
    private String fromMail;

    /*  메일 발송  */
    @Override
    public int doSendMail(MailDTO pDTO) {

        log.info(this.getClass().getName() + "메일 발송 실행");

        int res = 1;    // 메일 발송 성공 : 1 / 실패 : 0

        if (pDTO == null) {
            pDTO = new MailDTO();   // DTO 객체가 메모리에 올라가지않아 Null 발생될수 있으므로 에러방지차원 if문 사용
        }

        String toMail = CmmUtil.nvl(pDTO.getToMail());    // 받는이
        String title = CmmUtil.nvl(pDTO.getTitle());      // 메일제목
        String contents = CmmUtil.nvl(pDTO.getContents());// 메일내용

        log.info("toMail : " + toMail);
        log.info("title : " + title);
        log.info("contents : " + contents);

        // 메일 발송 메시지 구조(파일 첨부 가능)
        MimeMessage message = mailSender.createMimeMessage();

        // 메일 발송 메시지 구조를 쉽게 생성하게 도와주는 객체
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");

        try {
            messageHelper.setTo(toMail);    // 수신자
            messageHelper.setFrom(fromMail);// 발신자
            messageHelper.setSubject(title);// 메일제목
            messageHelper.setText(contents);// 메일내용

            mailSender.send(message);

        } catch (Exception e) { // 모든 에러 다 잡기
            res = 0; // 메일 발송 실패로 0으로 초기화
            log.info("[ERROR].doSendMail : " + e);

        }

        log.info(this.getClass().getName() + "메일 발송 종료");

        return res;
    }

    /*  임시 비번 생성  */
    @Override
    public String getTmpPassword() {

        log.info(this.getClass().getName() + "임시 비번 생성 시작");

        char[] charSet = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
                                    , 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N'
                                    , 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
                                    , 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n'
                                    , 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        String pwd = "";

        /*  문자 배열 길이의 값을 랜덤으로 10개 뽑아 조함  */
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            pwd += charSet[idx];
        }

        log.info("임시 비번 : " + pwd);
        log.info(this.getClass().getName() + "임시 비번 생성 완료");

        return pwd;
    }
}
