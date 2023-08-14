package kopo.poly.service;

import kopo.poly.dto.MapDTO;
import kopo.poly.dto.MemoDTO;

import java.util.List;

public interface IMemoService {


//    // 메모 목록(반환 타입 List)
//    List<MemoDTO> getMemoList() throws Exception;

    /*  메모 목록(반환 타입 List)  */
    List<MemoDTO> getMemoList() throws Exception;

    // 메모 검색 목록(반환 타입 List)
    List<MemoDTO> searchMemoList(MemoDTO pDTO) throws Exception;

    // 메모 등록
    void insertMemoInfo(MemoDTO pDTO) throws Exception;

    // 메모 상세보기
    MemoDTO getMemoInfo(MemoDTO pDTO) throws Exception;

    // 메모 수정
    void updateMemoInfo(MemoDTO pDTO) throws Exception;

    // 메모 삭제
    void deleteMemoInfo(MemoDTO pDTO) throws Exception;

    MapDTO getLatLng(MapDTO mapDTO) throws Exception;
}
