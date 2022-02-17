package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.service.GuestbookServiceImpl;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Autowired
    private GuestbookServiceImpl service;

    @Test
    @DisplayName("더미 테스트")
    public void insertDummies() {

        IntStream.rangeClosed(1, 300).forEach(i -> {

            Guestbook guestbook = Guestbook.builder()
                    .title("Title...." + i)
                    .content("Content...." + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    @DisplayName("수정 테스트")
    public void updateTest() {

        Optional<Guestbook> result = guestbookRepository.findById(300L);    //존재하는 번호로 테스트

        if (result.isPresent()) {
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title...");
            guestbook.changeContent("Changed Content...");

            guestbookRepository.save(guestbook);
        }
    }

    @Test
    @DisplayName("제목에 1이 있는 엔티티 검색")
    public void testQuery1() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno")
                .descending());

        QGuestbook qGuestbook = QGuestbook.guestbook; // 1
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder(); // 2
        BooleanExpression expression = qGuestbook.title.contains(keyword); // 3
        builder.and(expression); // 4
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); // 5

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    @Test
    @DisplayName("제목 혹은 내용에 특장한 키워드가 있고, gno가 0보다 큰 엔티티 검색")
    public void testQuery2() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno")
                .descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.title.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);

        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    @Test
    @DisplayName("엔티티 객체 -> DTO 객체 변환 테스트")
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1)
                .size(10).build();
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }
    }

}