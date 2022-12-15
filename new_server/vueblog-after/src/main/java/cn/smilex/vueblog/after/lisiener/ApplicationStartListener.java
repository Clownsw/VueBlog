package cn.smilex.vueblog.after.lisiener;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/12/15/17:01
 */
@Component
public class ApplicationStartListener {

    private Client client;

    @Autowired
    public void setClient(Client client) {
        this.client = client;
    }

    public void handler() {
        try {
            client.createIndex("blog");
        } catch (MeilisearchException ignore) {
        }
    }
}
