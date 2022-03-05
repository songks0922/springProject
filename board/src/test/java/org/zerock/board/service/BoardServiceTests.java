package org.zerock.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceTests {

    @Autowired
    private BoardService bootService;

    @Test
    @DisplayName("등록 테스트")
    void testRegister() {

        BoardDTO dto = BoardDTO.builder()
                .title("Test.")
                .content("Test...")
                .writerEmail("user55@aaa.com") //지금 DB에 존재하는 회원 이메일
                .build();

        Long bno = bootService.register(dto);
    }

    @Test
    @DisplayName("목록 처리 테스트")
    void testList() {

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = bootService
                .getList(pageRequestDTO);

        for (BoardDTO boardDTO : result.getDtoList()) {
            System.out.println(boardDTO);
        }
    }

    @Test
    @DisplayName("게시물 조회 테스트")
    void testGet() {

        Long bno = 100L;

        BoardDTO boardDTO = bootService.get(bno);

        System.out.println(boardDTO);
    }

    @Test
    @DisplayName("삭제 테스트")
    void testRemove() {
        Long bno = 1L;

        bootService.removeWithReplies(bno);
    }

    @Test
    @DisplayName("수정 테스트")
    void testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(1L)
                .title("제목 변경합니다.")
                .content("내용 변경합니다.")
                .build();
        bootService.modify(boardDTO);
    }
}