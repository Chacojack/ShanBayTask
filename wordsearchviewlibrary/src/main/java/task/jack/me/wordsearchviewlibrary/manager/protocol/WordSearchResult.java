package task.jack.me.wordsearchviewlibrary.manager.protocol;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import task.jack.me.wordsearchviewlibrary.model.WordSearchInfo;

/**
 * Created by zjchai on 2016/12/11.
 */

public class WordSearchResult {

    @SerializedName("status_code")
    int statusCode;

    @SerializedName("msg")
    String msg;

    @SerializedName("data")
    WordSearchInfo data;

    public int getStatusCode() {
        return statusCode;
    }

    public WordSearchResult setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public WordSearchResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public WordSearchInfo getData() {
        return data;
    }

    public WordSearchResult setData(WordSearchInfo data) {
        this.data = data;
        return this;
    }

    public static WordSearchResult fromJson(String json) {
        return new Gson().fromJson(json,WordSearchResult.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
