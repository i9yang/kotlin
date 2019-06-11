package ssg.search.query;

import ssg.search.parameter.Parameter;
import ssg.search.result.Result;

public interface QueryBuilder {
    public void set(Parameter parameter);
    public void execute();
    public void close();
    public void debug();
    public Result result(Result result);
}