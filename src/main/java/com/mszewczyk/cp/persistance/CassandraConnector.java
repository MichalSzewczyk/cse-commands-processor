package com.mszewczyk.cp.persistance;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.mszewczyk.cp.model.Command;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;

public class CassandraConnector implements DatabaseConnector<Command> {
    public static final String EVENTS_TABLE = "events_table";
    public static final String GROUP_ID = "group_id";
    public static final String USER_ID = "user_id";
    public static final String EVENT_ID = "event_id";
    public static final String OPERATION = "operation";
    private final CqlSession session;
    private final String keySpace;
    private final SimpleStatement insertCommandStatement;

    public CassandraConnector(String dataCenter, String dbUrl, int port, String keySpace) {
        this.keySpace = keySpace;
        insertCommandStatement = buildInsertCommandStatement();
        session = buildCluster(dataCenter, dbUrl, port);
    }

    @Override
    public void initializeDataStructure() {
        CreateKeyspace createKeyspace = SchemaBuilder.createKeyspace(keySpace)
                .ifNotExists()
                .withSimpleStrategy(3);

        session.execute(createKeyspace.build());
        session.execute("USE " + CqlIdentifier.fromCql(keySpace));
        if (!isEventsTableAlreadyCreated()) {
            CreateTable createTable = buildCreateTableStatement();
            session.execute(createTable.build());
        }
    }

    private boolean isEventsTableAlreadyCreated() {
        Select selectTableNames = QueryBuilder.selectFrom("system_schema", "tables").all();
        ResultSet resultSet = session.execute(selectTableNames.build());
        List<String> tableNames = resultSet.map(row -> row.getString("table_name")).all();
        return tableNames.contains(EVENTS_TABLE);
    }

    @Override
    public Collection<Command> getAll() {
        Select select = QueryBuilder.selectFrom(EVENTS_TABLE).all();

        ResultSet resultSet = session.execute(select.build());
        return resultSet.map(this::getCommandFromRow).all();
    }

    @Override
    public void store(Command value) {
        PreparedStatement preparedStatement = session.prepare(insertCommandStatement);

        BoundStatement statement = preparedStatement.bind()
                .setString(0, value.getGroup())
                .setString(1, value.getUser())
                .setLong(2, System.currentTimeMillis())
                .setString(3, value.getOperation().toString());

        session.execute(statement);
    }

    @Override
    public void close() {
        session.close();
    }

    private CqlSession buildCluster(String dbUrl, String dataCenter, int port) {
        CqlSessionBuilder builder = CqlSession.builder();
        builder.withLocalDatacenter(dataCenter);
        builder.addContactPoint(new InetSocketAddress(dbUrl, port));
        return builder.build();
    }

    private SimpleStatement buildInsertCommandStatement() {
        return QueryBuilder.insertInto(EVENTS_TABLE)
                .value(GROUP_ID, QueryBuilder.bindMarker())
                .value(USER_ID, QueryBuilder.bindMarker())
                .value(EVENT_ID, QueryBuilder.bindMarker())
                .value(OPERATION, QueryBuilder.bindMarker()).build();
    }

    private CreateTable buildCreateTableStatement() {
        return SchemaBuilder.createTable(EVENTS_TABLE)
                .withPartitionKey(GROUP_ID, DataTypes.TEXT)
                .withColumn(USER_ID, DataTypes.TEXT)
                .withColumn(EVENT_ID, DataTypes.BIGINT)
                .withColumn(OPERATION, DataTypes.TEXT);
    }

    private Command getCommandFromRow(Row row) {
        String groupId = row.getString(GROUP_ID);
        String userId = row.getString(USER_ID);
        String operation = row.getString(OPERATION);
        return new Command(Command.Operation.valueOf(operation), userId, groupId);
    }
}
