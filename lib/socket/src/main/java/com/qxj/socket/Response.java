package com.qxj.socket;

import kotlin.Result;
import org.json.JSONException;

public interface Response {
    void response(Result<String> result) throws JSONException;
}
