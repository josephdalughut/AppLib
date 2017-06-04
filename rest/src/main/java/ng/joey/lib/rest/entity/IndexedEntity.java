package ng.joey.lib.rest.entity;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;

import ng.joey.lib.java.util.Value;


public abstract class IndexedEntity extends Entity {
    /**
     * @return the names of the index to which this document is to be added
     */
    public abstract String indexName();

    public abstract String indexId();

    public abstract Document.Builder documentBuilder();

    public void onSave() throws InterruptedException {
        Document.Builder builder = documentBuilder();
        if(Value.IS.nullValue(builder)){
            onDelete();
            return;
        }
        index(indexName(), documentBuilder().setId(indexId()).build());
    }

    public void onDelete(){
        delete(this);
    }

    public static void index(String indexName, Document document)
            throws InterruptedException {
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

        final int maxRetry = 3;
        int attempts = 0;
        int delay = 2;
        while (true) {
            try {
                index.put(document);
            } catch (PutException e) {
                if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())
                        && ++attempts < maxRetry) { // retrying
                    Thread.sleep(delay * 1000);
                    delay *= 2; // easy exponential backoff
                    continue;
                } else {
                    throw e; // otherwise throw
                }
            }
            break;
        }
    }

    public static void delete(IndexedEntity entity){
        entity.index().deleteAsync(entity.indexId());
    }

    public Index index() {
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName()).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        return index;
    }


}
