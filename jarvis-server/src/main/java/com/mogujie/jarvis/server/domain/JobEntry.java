package com.mogujie.jarvis.server.domain;

import java.util.Map;

import com.mogujie.jarvis.core.expression.ScheduleExpression;
import com.mogujie.jarvis.dto.generate.Job;

/**
 * Created by muming on 15/11/16.
 */
public class JobEntry {

    private Job job;
    private Map<Long, ScheduleExpression> scheduleExpressions;
    private Map<Long, JobDependencyEntry> dependencies;

    public JobEntry(Job job, Map<Long, ScheduleExpression> scheduleExpressions, Map<Long, JobDependencyEntry> dependencies) {
        this.job = job;
        this.scheduleExpressions = scheduleExpressions;
        this.dependencies = dependencies;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Map<Long, ScheduleExpression> getScheduleExpressions() {
        return scheduleExpressions;
    }

    public void setScheduleExpressions(Map<Long, ScheduleExpression> scheduleExpressions) {
        this.scheduleExpressions = scheduleExpressions;
    }

    public void addScheduleExpression(long expressionId, ScheduleExpression scheduleExpression) {
        if (!scheduleExpressions.containsKey(expressionId)) {
            scheduleExpressions.put(expressionId, scheduleExpression);
        }
    }

    public void removeScheduleExpression(long expressionId) {
        scheduleExpressions.remove(expressionId);
    }

    public void updateScheduleExpression(long expressionId, ScheduleExpression newExpression) {
        scheduleExpressions.put(expressionId, newExpression);
    }

    public void clearScheduleExpressions() {
        scheduleExpressions.clear();
    }

    public Map<Long, JobDependencyEntry> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<Long, JobDependencyEntry> dependencies) {
        this.dependencies = dependencies;
    }

    public void addDependency(long preJobId, JobDependencyEntry jobDependencyEntry) {
        dependencies.put(preJobId, jobDependencyEntry);
    }

    public void updateDependency(long preJobId, JobDependencyEntry jobDependencyEntry) {
        dependencies.remove(preJobId);
        dependencies.put(preJobId, jobDependencyEntry);
    }

    public void removeDependency(long preJobId) {
        dependencies.remove(preJobId);
    }

    public void updateJobStatus(int status) {
        job.setStatus(status);
    }
}
