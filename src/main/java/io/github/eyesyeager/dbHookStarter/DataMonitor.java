package io.github.eyesyeager.dbHookStarter;

import io.github.eyesyeager.dbHookStarter.constants.MonitorResultTypeEnum;
import io.github.eyesyeager.dbHookStarter.constants.MonitorStateTypeEnum;
import io.github.eyesyeager.dbHookStarter.entity.MonitorEntity;
import io.github.eyesyeager.dbHookStarter.entity.StateEntity;
import io.github.eyesyeager.dbHookStarter.utils.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * @author eyesYeager
 * data 2025/1/18 15:59
 */

public class DataMonitor {
    private String stateQuerySql = "SELECT * FROM $ WHERE executor = ?";

    private String stateUpdateSql = "REPLACE INTO $(executor,state) VALUES(?,?)";

    private final String executorName;

    private final MonitorEntity monitorEntity;

    private final MonitorEntity.State state;

    private final boolean enabledState;

    private final DataSource dataSource;

    public DataMonitor(HookConfig hookConfig, String executorName, MonitorEntity entity, String stateTableName) {
        this.dataSource = hookConfig.getDataSource();
        this.executorName = executorName;
        this.monitorEntity = entity;
        this.state = entity.getState();
        this.enabledState = this.state != null && Boolean.TRUE.equals(state.getEnabled());
        this.stateQuerySql = this.stateQuerySql.replace("$", stateTableName);
        this.stateUpdateSql = this.stateUpdateSql.replace("$", stateTableName);
    }

    public List<Object> doMonitor() throws SQLException, ReflectiveOperationException {
        Connection conn = dataSource.getConnection();
        return enabledState ? monitorWithState(conn) : monitorWithoutState(conn);
    }

    public List<Object> monitorWithState(Connection conn) throws SQLException, ReflectiveOperationException {
        // 获取任务状态值
        StateEntity stateEntity = getState(conn);
        Object stateValue = stateEntity == null ? state.getDefaultValue() : stateEntity.getState();
        Integer fieldType = MonitorStateTypeEnum.getValue(state.getFieldType());
        if (fieldType == null) {
            throw new IllegalArgumentException("fieldType is illegal: " + state.getFieldType());
        }
        // 执行监控sql
        PreparedStatement ps = conn.prepareStatement(monitorEntity.getJudgeSql());
        ps.setObject(1, stateValue, fieldType);
        ResultSet resultSet = ps.executeQuery();
        Class<?> clazz = Class.forName(monitorEntity.getResultClass());
        List<Object> resultList = new ArrayList<>();
        Object maxState = stateValue;
        while (resultSet.next()) {
            // 获取任务执行结果
            Object result = getResultObject(resultSet, clazz);
            resultList.add(result);
            // 更新任务状态
            Object target = resultSet.getObject(state.getFieldName());
            if (MonitorStateTypeEnum.compare(target, maxState, state.getFieldType())) {
                maxState = target;
            }
        }
        // 更新任务状态
        if (maxState != stateValue) {
            setState(conn, maxState, fieldType);
        }
        return resultList;
    }

    public List<Object> monitorWithoutState(Connection conn) throws SQLException, ReflectiveOperationException {
        PreparedStatement ps = conn.prepareStatement(monitorEntity.getJudgeSql());
        ResultSet resultSet = ps.executeQuery();
        Class<?> clazz = Class.forName(monitorEntity.getResultClass());
        List<Object> resultList = new ArrayList<>();
        while (resultSet.next()) {
            Object result = getResultObject(resultSet, clazz);
            resultList.add(result);
        }
        return resultList;
    }

    private StateEntity getState(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(stateQuerySql);
        ps.setString(1, executorName);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            return new StateEntity(
                    resultSet.getInt("id"),
                    resultSet.getString("executor"),
                    resultSet.getString("state"),
                    resultSet.getDate("create_time"),
                    resultSet.getDate("update_time"),
                    resultSet.getDate("delete_time")
            );
        }
        return null;
    }

    private void setState(Connection conn, Object maxState, int fieldType) throws SQLException {
        PreparedStatement psUpdate = conn.prepareStatement(stateUpdateSql);
        psUpdate.setString(1, executorName);
        psUpdate.setObject(2, maxState, fieldType);
        psUpdate.executeUpdate();
    }

    private Object getResultObject(ResultSet resultSet, Class<?> clazz)
            throws SQLException, ReflectiveOperationException {
        String resultType = monitorEntity.getResultType();
        if (MonitorResultTypeEnum.FIELD.getValue().equals(resultType)) {
            return DbUtils.resultSetToField(resultSet, clazz);
        }
        if (MonitorResultTypeEnum.ENTITY.getValue().equals(resultType)) {
            return DbUtils.resultSetToEntity(resultSet, clazz);
        }
        throw new IllegalArgumentException("resultType is illegal: " + resultType);
    }
}
