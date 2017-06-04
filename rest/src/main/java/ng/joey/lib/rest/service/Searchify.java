package ng.joey.lib.rest.service;

import com.google.appengine.api.search.Cursor;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;

import java.util.logging.Logger;

import javax.annotation.Nonnull;

import ng.joey.lib.java.util.Value;


/**
 * https://cloud.google.com/appengine/docs/java/search/query_strings
 * @param <T>
 */
public class Searchify<T> {

    public static final Logger logger = Logger.getLogger(Searchify.class.getSimpleName());

    private String queryString = "";
    private Class<T> tClass;
    private QueryOptions.Builder queryOptionsBuilder;

    private SortOptions.Builder sortOptionsBuilder;

    public Searchify(String queryString){
        this.queryString = queryString;
    }

    public static <T> Searchify<T> search(String queryString){
        //logger.info("searchify instance started for query string: "+queryString);
        return new Searchify<>(queryString);
    }

    public Searchify<T> sort(@Nonnull String field, @Nonnull SortExpression.SortDirection direction,
                             String defaultValue){
        if(Value.IS.nullValue(sortOptionsBuilder))
            sortOptionsBuilder = SortOptions.newBuilder();
        SortExpression.Builder sortExpression = SortExpression.newBuilder();
        sortExpression.setExpression(field)
                .setDirection(direction);
        if(!Value.IS.nullValue(defaultValue))
            sortExpression.setDefaultValue(defaultValue);
        sortOptionsBuilder.addSortExpression(sortExpression.build());
        return this;
    }

    public Searchify<T> sort(@Nonnull String field, @Nonnull SortExpression.SortDirection direction,
                             Number defaultValue){
        if(Value.IS.nullValue(sortOptionsBuilder))
            sortOptionsBuilder = SortOptions.newBuilder();
        SortExpression.Builder sortExpression = SortExpression.newBuilder();
        sortExpression.setExpression(field)
                .setDirection(direction);
        if(!Value.IS.nullValue(defaultValue))
            sortExpression.setDefaultValueNumeric(Value.TO.doubleValue(defaultValue));
        sortOptionsBuilder.addSortExpression(sortExpression.build());
        return this;
    }

    public Searchify<T> limit(int limit){
        if(Value.IS.nullValue(queryOptionsBuilder))
            queryOptionsBuilder = QueryOptions.newBuilder();
        queryOptionsBuilder.setLimit(limit);
        //logger.info("searchify limit set to "+limit);
        return this;
    }

    public Searchify<T> limitToFields(String... fieldsToReturn){
        if(Value.IS.nullValue(queryOptionsBuilder))
            queryOptionsBuilder = QueryOptions.newBuilder();
        queryOptionsBuilder.setFieldsToReturn(fieldsToReturn);
        return this;
    }

    public Searchify<T> startAt(Cursor cursor){
        if(Value.IS.nullValue(queryOptionsBuilder))
            queryOptionsBuilder = QueryOptions.newBuilder();
        queryOptionsBuilder.setCursor(cursor);
        return this;
    }

    public Searchify<T> from(Class<T> tClass){
        this.tClass = tClass;
        //logger.info("searchify class: "+tClass.getSimpleName());
        return this;
    }

    public Results<ScoredDocument> get(){
        Query.Builder queryBuilder = Query.newBuilder();
        if(!Value.IS.nullValue(sortOptionsBuilder)){
            if(Value.IS.nullValue(queryOptionsBuilder))
                queryOptionsBuilder = QueryOptions.newBuilder();
            queryOptionsBuilder.setSortOptions(sortOptionsBuilder.build());
            queryBuilder.setOptions(queryOptionsBuilder.build());
        }
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(tClass.getSimpleName()).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        Results<ScoredDocument> result = index.search(queryBuilder.build(queryString));
        //logger.info("searchify results size: "+result.getNumberReturned());
        return result;
    }

}
