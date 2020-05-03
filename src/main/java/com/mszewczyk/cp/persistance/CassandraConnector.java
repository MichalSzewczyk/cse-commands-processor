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

public class CassandraConnector implements DatabaseConnector<Command> {
    public static final String EVENTS_TABLE = "events_table";
    public static final String EVENT_ID = "event_id";
    public static final String USER_ID = "user_id";
    public static final String GROUP_ID = "group_id";
    private final CqlSession session;
    private final String keySpace;
    private final SimpleStatement insertCommandStatement;

    public CassandraConnector(String dbUrl, int port, String keySpace) {
        this.keySpace = keySpace;
        insertCommandStatement = buildInsertCommandStatement();
        session = buildCluster(dbUrl, port);
    }

    @Override
    public void initializeDataStructure() {
        CreateKeyspace createKeyspace = SchemaBuilder.createKeyspace(keySpace)
                .ifNotExists()
                .withSimpleStrategy(3);

        session.execute(createKeyspace.build());
        session.execute("USE " + CqlIdentifier.fromCql(keySpace));
        CreateTable createTable = buildCreateTableStatement();
        session.execute(createTable.build());
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
                .setLong(0, System.currentTimeMillis())
                .setString(1, value.getUser())
                .setString(2, value.getGroup())
                .setString(3, value.getOperation().toString());

        session.execute(statement);
    }

    @Override
    public void close() {
        session.close();
    }

    private CqlSession buildCluster(String dbUrl, int port) {
        CqlSessionBuilder builder = CqlSession.builder();
        builder.addContactPoint(new InetSocketAddress(dbUrl, port));
        return builder.build();
    }

    private SimpleStatement buildInsertCommandStatement() {
        return QueryBuilder.insertInto(EVENTS_TABLE)
                .value(EVENT_ID, QueryBuilder.bindMarker())
                .value(USER_ID, QueryBuilder.bindMarker())
                .value(GROUP_ID, QueryBuilder.bindMarker()).build();
    }

    private CreateTable buildCreateTableStatement() {
        return SchemaBuilder.createTable(EVENTS_TABLE)
                .withPartitionKey(EVENT_ID, DataTypes.TIMESTAMP)
                .withColumn(USER_ID, DataTypes.TEXT)
                .withColumn(GROUP_ID, DataTypes.TEXT);
    }

    private Command getCommandFromRow(Row row) {
        String userId = row.getString(1);
        String groupId = row.getString(2);
        String operation = row.getString(3);
        return new Command(Command.Operation.valueOf(operation), userId, groupId);
    }
}
