package kopo.poly.persistance.mapper;

import kopo.poly.dto.MapDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMapMapper {
    void insertMapData(MapDTO mapDTO) throws Exception;
}
