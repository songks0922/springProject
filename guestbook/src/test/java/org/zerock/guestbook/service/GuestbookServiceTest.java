package org.zerock.guestbook.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookServiceTest {

    @Autowired
    private GuestbookService service;

    @Test
    @DisplayName("서비스 등록 테스트")
    void testRegister() {
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("Sample Title...")
                .build();
        System.out.println(service.register(guestbookDTO));
    }
}