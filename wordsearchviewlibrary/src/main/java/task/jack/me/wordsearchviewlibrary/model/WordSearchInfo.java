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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordSearchInfo)) return false;

        WordSearchInfo that = (WordSearchInfo) o;

        if (id != that.id) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (pronunciation != null ? !pronunciation.equals(that.pronunciation) : that.pronunciation != null)
            return false;
        if (definition != null ? !definition.equals(that.definition) : that.definition != null)
            return false;
        return audioUrl != null ? audioUrl.equals(that.audioUrl) : that.audioUrl == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (pronunciation != null ? pronunciation.hashCode() : 0);
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        result = 31 * result + (audioUrl != null ? audioUrl.hashCode() : 0);
        return result;
    }
}
