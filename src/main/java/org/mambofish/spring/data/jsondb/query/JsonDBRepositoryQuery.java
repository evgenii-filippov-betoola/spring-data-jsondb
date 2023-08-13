package org.mambofish.spring.data.jsondb.query;

import io.jsondb.JsonDBTemplate;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vince
 */
public class JsonDBRepositoryQuery implements RepositoryQuery {

    private final JsonDBQueryMethod queryMethod;
    private final JsonDBTemplate template;

    private final Pattern findByPattern = Pattern.compile("^findBy(.+)$");
    private final Pattern findAllByPattern = Pattern.compile("^findAllBy(.+)$");
    private final boolean findAll;
    private final String fieldName;

    public JsonDBRepositoryQuery(JsonDBQueryMethod queryMethod, JsonDBTemplate template) {
        this.queryMethod = queryMethod;
        this.template = template;

        Matcher findByMatcher = findByPattern.matcher(queryMethod.getName());
        if (findByMatcher.find()) {
            this.findAll = false;
            this.fieldName = StringUtils.uncapitalize(findByMatcher.group(1));
        } else {
            Matcher findAllByMatcher = findAllByPattern.matcher(queryMethod.getName());
            if (findAllByMatcher.find()) {
                this.findAll = true;
                this.fieldName = StringUtils.uncapitalize(findAllByMatcher.group(1));
            } else {
                throw new IllegalArgumentException("Unable to parse field name from method name '%s'"
                        .formatted(queryMethod.getName()));
            }
        }
    }

    @Override
    public Object execute(Object[] objects) {
        String param = null;
        if (objects.length > 0 && objects[0] != null) {
            param = objects[0].toString().replace("\"", "&quot;");
        }
        String jxQuery = "/.[%s=\"%s\"]".formatted(fieldName, param);
        List<?> list = template.find(jxQuery, queryMethod.getReturnedObjectType());
        if (findAll) {
            return list;
        }

        if (list.isEmpty()) {
            return Optional.empty();
        }
        if (list.size() > 1) {
            throw new IllegalStateException("More than one record found by '%s': %s"
                    .formatted(queryMethod.getName(), list.size()));
        }

        return Optional.ofNullable(list.get(0));
    }

    @Override
    public QueryMethod getQueryMethod() {
        return queryMethod;
    }
}
