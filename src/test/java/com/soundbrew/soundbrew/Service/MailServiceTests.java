package com.soundbrew.soundbrew.Service;

import com.soundbrew.soundbrew.service.mail.MailService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class MailServiceTests {

    @Autowired
    private MailService mailService;

    @Test
    public void testSendMail(){
        String email = "inwon.private@icloud.com";

        mailService.send(email,"Test","Test");
    }
}
