package kopo.poly.service.impl;

import kopo.poly.dto.MapDTO;
import kopo.poly.persistance.mapper.IMapMapper;
import kopo.poly.service.IMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class MapService implements IMapService {
    private final IMapMapper mapMapper;

    @Override
    @Transactional
    public void saveMapData(MapDTO mapDTO) throws Exception {

        log.info("saveMapData start");

        MapDTO rDTO = new MapDTO();
        rDTO.setLevel(mapDTO.getLevel());
        rDTO.setLat(mapDTO.getLat());
        rDTO.setLng(mapDTO.getLng());
        rDTO.setMloc(mapDTO.getMloc());

        log.info(rDTO.getLat().toString());
        log.info(rDTO.getLng().toString());
        log.info(rDTO.getLevel().toString());
        log.info(rDTO.getMloc());

        mapMapper.insertMapData(rDTO);

        log.info("saveMapData End");
    }
}
