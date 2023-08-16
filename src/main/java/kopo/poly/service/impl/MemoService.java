package kopo.poly.service.impl;

import kopo.poly.dto.MemoDTO;
import kopo.poly.persistance.mapper.IMemoMapper;
import kopo.poly.service.IMemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class MemoService implements IMemoService {

    // Memo 관련 SQL 사용하기 위한 Mapper 가져오기
    private final IMemoMapper memoMapper;


    /*  메모 목록(반환 타입 List)  */
    @Override
    public List<MemoDTO> getMemoList() throws Exception {

        log.info(this.getClass().getName() + ".Memo 목록 시작");

        return memoMapper.getMemoList();
    }


    /*  메모 검색 목록(반환 타입 List)  */
    @Override
    public List<MemoDTO> searchMemoList(MemoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".Memo 검색 목록");

        // selectMemoList = 검색 목록
        return memoMapper.searchMemoList(pDTO);
    }


    /*  메모 등록  */
    @Transactional
    @Override
    public void insertMemoInfo(MemoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".메모 등록");

        memoMapper.insertMemoInfo(pDTO);
    }


    /*  메모 상세보기  */
    @Transactional
    @Override
    public MemoDTO getMemoInfo(MemoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".메모 상세보기");

        return memoMapper.getMemoInfo(pDTO);
    }


    /*  메모 수정  */
    @Transactional
    @Override
    public void updateMemoInfo(MemoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".메모 수정");

        memoMapper.updateMemoInfo(pDTO);
    }


    /*  메모 삭제  */
    @Transactional
    @Override
    public void deleteMemoInfo(MemoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".메모 삭제");

        memoMapper.deleteMemoInfo(pDTO);
    }

//    @Transactional
//    @Override
//    public MapDTO getLatLng(MapDTO mapDTO) throws Exception {
//        log.info(this.getClass().getName() + ".위경도 가져오기");
//
//        return memoMapper.getLatLng(mapDTO);
//    }
}
