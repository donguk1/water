package kopo.poly.persistance.mapper;


import kopo.poly.dto.MemoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMemoMapper {

    // 메모 목록(반환 타입 List)
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

//    MapDTO getLatLng(MapDTO mapDTO) throws Exception;
}
