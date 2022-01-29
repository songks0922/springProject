package org.zerock.ex2.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    @DisplayName("MemoRepository 정상 주입 테스트")
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    @DisplayName("등록 작업 테스트")
    public void testInsertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    @DisplayName("조회 작업 테스트 findById")
    public void testSelect() {
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("=================================");

        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Transactional
    @Test
    @DisplayName("조회 작업 테스트 getOne")
    public void testSelect2() {
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);

        System.out.println("=================================");

        System.out.println(memo);
    }

    @Test
    @DisplayName("수정 작업 테스트")
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));
    }

    @Test
    @DisplayName("삭제 작업 테스트")
//    @Transactional
    public void testDelete() {

        Long mno = 100L;

        memoRepository.deleteById(mno);
    }
    
    @Test
    @DisplayName("페이징 처리 테스트")
    public void testPageDefault() {
        
        //1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println("result = " + result);

        System.out.println("Total Pages: " + result.getTotalPages());
        System.out.println("Total Count: " + result.getTotalElements());
        System.out.println("Page Number: " + result.getNumber());
        System.out.println("Page Size: " + result.getSize());
        System.out.println("has next page?: " + result.hasNext());
        System.out.println("first page?: " + result.isFirst());

        System.out.println("=================================");

        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    @Test
    @DisplayName("정렬 조건 추가 테스트")
    public void testSort() {

        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);

        Pageable pageable = PageRequest.of(0, 10, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    @DisplayName("다중 조건 쿼리 메서드 테스트")
    public void testQueryMethods() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for (Memo memo : list) {
            System.out.println(memo);
        }
    }

    @Test
    @DisplayName("쿼리 메서드와 Pageable 결합 테스트")
    public void testQueryMethodWithPageable() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(memo -> System.out.println(memo));
    }

    @Test
    @Transactional
    @Commit
    @DisplayName("deleteBy 테스트")
    public void testDeleteQueryMethods() {
        memoRepository.deleteMemoByMnoLessThan(10L);
    }

    @Test
    @DisplayName("@Query 어노테이션 테스트")
    public void testQueryAnnotation() {
        List<Memo> list = memoRepository.getListDesc();

        for (Memo memo : list) {
            System.out.println(memo);
        }
    }

}
