package kopo.poly.service;

import kopo.poly.dto.RiverCCDTO;

public interface IRiverCCService {
    RiverCCDTO getCCData(String num) throws Exception;
}
