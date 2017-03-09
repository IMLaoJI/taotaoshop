package com.taotao.portal.service;

import java.util.Map;

public interface UcenterService {

    Map<String, Object> queryOrders(Integer page, Integer rows);

    Map<String, Object> search(String keyWords, Integer page, Integer rows);

}
