package com.soundbrew.soundbrew.service.mail;

public interface MailService {

//    SimpleMailMessage 메서드 이름이 to, subject, text
//    이에 따라 매개변수 이름도 맞춰 작성
    void send(String to, String subject, String text);
}
