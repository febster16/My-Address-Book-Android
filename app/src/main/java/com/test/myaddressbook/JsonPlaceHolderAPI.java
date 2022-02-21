package com.test.myaddressbook;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderAPI {
    @GET("stage2/people/?nim=2301942406&nama=FebryanStefanusTandian")
//    Call<List<Employee>> getEmployee();
    Call<JsonObject> getEmployee();
}
