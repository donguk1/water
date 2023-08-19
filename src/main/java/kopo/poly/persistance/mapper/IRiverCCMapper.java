package kopo.poly.persistance.mapper;

import kopo.poly.dto.RiverCCDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface IRiverCCMapper {
    RiverCCDTO getCCData(@Param("num") String num);
}
