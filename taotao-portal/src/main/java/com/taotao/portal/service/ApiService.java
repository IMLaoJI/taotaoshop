package com.taotao.portal.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.portal.bean.HttpResult;



/**
 * 负责和外部系统做接口的交互
 */
@Service
public class ApiService {

    // 创建Httpclient对象
    @Autowired(required = false)
    private CloseableHttpClient httpclient;

    @Autowired(required = false)
    private RequestConfig requestConfig;

    /**
     * 执行GET请求
     * 
     * @param url
     * @return 请求到的内容
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public String doGet(String url) throws URISyntaxException, ClientProtocolException, IOException {
        return doGet(url, null);
    }

    /**
     * 执行GET请求
     * 
     * @param url
     * @param params 请求中的参数
     * @return 请求到的内容
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public String doGet(String url, Map<String, Object> params) throws URISyntaxException,
            ClientProtocolException, IOException {
        URI uri = null;
        if (params != null) {
            // 定义请求的参数
            URIBuilder builder = new URIBuilder(url);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
            }
            uri = builder.build();
        }

        // 创建http GET请求
        HttpGet httpGet = null;
        if (uri != null) {
            httpGet = new HttpGet(uri);
        } else {
            httpGet = new HttpGet(url);
        }

        httpGet.setConfig(this.requestConfig);

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * 发送POST请求
     * 
     * @param url
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResult doPost(String url, Map<String, Object> params) throws ClientProtocolException, IOException {
        // 创建http GET请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);
        if (null != params) {
            // 构造表单
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parameters,"UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
        }
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpPost);
            HttpResult httpResult = new HttpResult();
            httpResult.setCode(response.getStatusLine().getStatusCode());
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                httpResult.setContent(EntityUtils.toString(response.getEntity(), "UTF-8"));
            }
            return httpResult;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
    
    /**
     * 提交json的POST请求
     * @param url
     * @param json
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResult doPostJson(String url, String json) throws ClientProtocolException, IOException {
        // 创建http GET请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);
        if (null != json) {
            // 构造字符串
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
        }
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpPost);
            HttpResult httpResult = new HttpResult();
            httpResult.setCode(response.getStatusLine().getStatusCode());
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                httpResult.setContent(EntityUtils.toString(response.getEntity(), "UTF-8"));
            }
            return httpResult;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}
