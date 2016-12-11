package task.jack.me.wordsearchviewlibrary.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zjchai on 2016/12/11.
 */

public class WordSearchInfo {

    @SerializedName("id")
    int id;

    @SerializedName("content")
    String content;

    @SerializedName("pronunciation")
    String pronunciation;

    @SerializedName("definition")
    String definition;

    @SerializedName("audio")
    String audioUrl;

    public int getId() {
        return id;
    }

    public WordSearchInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public WordSearchInfo setContent(String content) {
        this.content = content;
        return this;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public WordSearchInfo setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
        return this;
    }

    public String getDefinition() {
        return definition;
    }

    public WordSearchInfo setDefinition(String definition) {
        this.definition = definition;
        return this;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public WordSearchInfo setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
        return this;
    }

    public static WordSearchInfo fromJson(String json) {
        return new Gson().fromJson(json, WordSearchInfo.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
