package com.wzy.springaiagent.fcalling.csdn.infrastructure.client;

import com.wzy.springaiagent.fcalling.csdn.infrastructure.gateway.ICSDNService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CSDNServiceClient {

    private final ICSDNService csdnService;

    public CSDNServiceClient() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://bizapi.csdn.net")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

        csdnService = retrofit.create(ICSDNService.class);
    }

    public ICSDNService getCsdnService() {
        return csdnService;
    }
}
