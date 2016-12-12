package task.jack.me.shanbay.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * Created by zjchai on 2016/12/12.
 */
@Data
@Builder
public class ImageModel {

    private String url;
    private boolean refresh = false;

    @Tolerate
    public ImageModel() {
    }
}
