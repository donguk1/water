package kopo.poly.service.impl;

import kopo.poly.dto.RiverCCDTO;
import kopo.poly.persistance.mapper.IRiverCCMapper;
import kopo.poly.service.IRiverCCService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RIverCCService implements IRiverCCService {
    private final IRiverCCMapper riverCCMapper;

    @Override
    public RiverCCDTO getCCData(String num) throws Exception {
        log.info(this.getClass().getName() + ".문화관정보 가져오기 시작");
        RiverCCDTO rDTO = riverCCMapper.getCCData(num);
        return rDTO;
    }
}
