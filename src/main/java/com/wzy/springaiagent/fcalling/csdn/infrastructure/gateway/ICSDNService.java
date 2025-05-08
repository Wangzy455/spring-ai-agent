package com.wzy.springaiagent.fcalling.csdn.infrastructure.gateway;

import com.wzy.springaiagent.fcalling.csdn.infrastructure.gateway.dto.ArticleRequestDTO;
import com.wzy.springaiagent.fcalling.csdn.infrastructure.gateway.dto.ArticleResponseDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@Service
public interface ICSDNService {

    @Headers({
        "accept: application/json, text/plain, */*",
        "accept-language: zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
        "content-type: application/json;",
        "origin: https://mpbeta.csdn.net",
        "priority: u=1, i",
        "referer: https://mpbeta.csdn.net/",
        "sec-ch-ua: \"Chromium\";v=\"134\", \"Not:A-Brand\";v=\"24\", \"Google Chrome\";v=\"134\"",
        "sec-ch-ua-mobile: ?1",
        "sec-ch-ua-platform: \"Android\"",
        "sec-fetch-dest: empty",
        "sec-fetch-mode: cors",
        "sec-fetch-site: same-site",
        "user-agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Mobile Safari/537.36",
        "x-ca-key: 203803574",
        "x-ca-nonce: d3e81814-a563-4d1e-92ea-ab2bd1a0e5e9",
        "x-ca-signature: 88oTFaXpKEyh2BaNnJKW28u4HiPVd7/qOMtoAKT00AE=",
        "x-ca-signature-headers: x-ca-key,x-ca-nonce",
    })
    @POST("/blog-console-api/v1/postedit/saveArticle")
    Call<ArticleResponseDTO> saveArticle(@Body ArticleRequestDTO request, @Header("Cookie") String cookieValue);

}