package com.mogujie.jarvis.server.actor.base;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.*;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSetWriter;
import org.junit.Assert;
import org.mybatis.guice.transactional.Transactional;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Location www.mogujie.com
 * Created by qinghuo on 16/1/18.
 * used by jarvis-parent
 */
@Transactional
public abstract class DBTestBased {

    protected static Properties properties = new Properties();

    static {
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This abstract is used for prepare data before do the real method call.
     *
     * @param iconn
     * @throws Exception
     */
    protected abstract void prepareData(IDatabaseConnection iconn, String tableName) throws Exception;

    /**
     * Execute one sql
     *
     * @param iconn
     * @param sql
     * @throws Exception
     */
    protected void execSql(IDatabaseConnection iconn, String sql) throws Exception {
        Connection con = iconn.getConnection();
        Statement stmt = con.createStatement();
        try {
            stmt.execute(sql);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    /**
     * 导出数据到指定文件
     * @param file 一个标准的java.io.File
     * @param connection  一个标准的java.sql.Connection
     * @throws org.dbunit.DatabaseUnitException
     */
    protected void exportTable(File file, Connection connection,String tableName) throws DatabaseUnitException, IOException {
        IDatabaseConnection databaseConnection = new DatabaseConnection(connection);
        QueryDataSet dataSet = new QueryDataSet(databaseConnection);
        try {
            dataSet.addTable(tableName);
            Writer writer = new FileWriter(file);
            XmlDataSetWriter w = new XmlDataSetWriter(writer);
            w.write(dataSet);
            writer.flush();
            writer.close();
        } catch (AmbiguousTableNameException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataSetException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get IDatabaseConnection connection
     *
     * @return
     * @throws Exception
     */
    protected IDatabaseConnection getIDatabaseConnection() throws Exception {

        String db_inst = "", db_user = "", db_pwd = "", driverClass = "";
        //The default is commit the record

        db_user = properties.getProperty("dataSource.user");
        db_inst = properties.getProperty("dataSource.url");
        db_pwd = properties.getProperty("dataSource.password");
        driverClass = properties.getProperty("dataSourceClassName");

        IDatabaseConnection iconn = null;
        IDatabaseTester databaseTester;
        databaseTester = new JdbcDatabaseTester(driverClass, db_inst, db_user, db_pwd);
        iconn = databaseTester.getConnection();
        return iconn;
    }

    /**
     * This is used to assert the data from table and the expected data set. If all of the them has the same records, then the assert is true.
     *
     * @param tableName
     * @param sql
     * @param expectedDataSet
     * @param iconn
     * @throws Exception
     */
    protected void assertDataSet(String tableName, String sql, IDataSet expectedDataSet, IDatabaseConnection iconn) throws Exception {
        printDataAsXml(iconn, tableName, sql);
        QueryDataSet loadedDataSet = new QueryDataSet(iconn);
        loadedDataSet.addTable(tableName, sql);
        ITable table1 = loadedDataSet.getTable(tableName);
        ITable table2 = expectedDataSet.getTable(tableName);
        Assert.assertEquals(table2.getRowCount(), table1.getRowCount());

        DefaultColumnFilter.includedColumnsTable(table1, table2.getTableMetaData().getColumns());
        Assertion.assertEquals(table2, table1);

    }

    /**
     * Create the data set by input stream which read from the dbunit xml data file.
     *
     * @param is
     * @return
     * @throws Exception
     */
    protected ReplacementDataSet createDataSet(InputStream is) throws Exception {
        return new ReplacementDataSet(new FlatXmlDataSetBuilder().build(is));
    }

    /**
     * Convert the data in the ITable to List
     *
     * @param table
     * @return
     * @throws Exception
     */
    private List<Map<?, ?>> getDataFromTable(ITable table) throws Exception {
        List<Map<?, ?>> ret = new ArrayList<Map<?, ?>>();
        int count_table = table.getRowCount();
        if (count_table > 0) {
            Column[] columns = table.getTableMetaData().getColumns();
            for (int i = 0; i < count_table; i++) {
                Map<String, Object> map = new TreeMap<String, Object>();
                for (Column column : columns) {
                    map.put(column.getColumnName().toUpperCase(), table.getValue(i, column.getColumnName()));
                }
                ret.add(map);
            }
        }
        return ret;
    }

    /**
     * Get data by the SQL and table name, then convert the data in the ITable to List
     *
     * @param iconn
     * @param tableName
     * @param sql
     * @return
     * @throws Exception
     */
    protected List<Map<?, ?>> getTableDataFromSql(IDatabaseConnection iconn, String tableName, String sql) throws Exception {
        ITable table = iconn.createQueryTable(tableName, sql);
        return getDataFromTable(table);
    }

    /**
     * Get data by the SQL and table name, then convert the data in the ITable to List. And the print the data as xml data format.
     *
     * @param iconn
     * @param tableName
     * @param sql
     * @throws Exception
     */
    protected void printDataAsXml(IDatabaseConnection iconn, String tableName, String sql) throws Exception {
        List<Map<?, ?>> datas = getTableDataFromSql(iconn, tableName, sql);
        StringBuffer sb;
        for (Map<?, ?> data : datas) {
            sb = new StringBuffer();
            sb.append("<" + tableName.toUpperCase() + " ");
            for (Object o : data.keySet()) {
                sb.append(o + "=\"" + data.get(o) + "\" ");
            }
            sb.append("/>");
            System.out.println(sb.toString());
        }
    }

    /**
     * Export data for the table names by the given Connection into the resultFile.<br>
     * The export data will be DBUnit format.
     *
     * @param conn
     * @param tableNameList
     * @param resultFile
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void exportData(Connection conn, List<String> tableNameList, String resultFile) throws SQLException, DatabaseUnitException, FileNotFoundException, IOException {
        if (conn == null) {
            return;
        }
        IDatabaseConnection iconn = new DatabaseConnection(conn);
        exportData(iconn, tableNameList, resultFile);
    }


    /**
     * Export data for the table names by the given IDatabaseConnection into the resultFile.<br>
     * The export data will be DBUnit format.
     *
     * @param iconn
     * @param tableNameList
     * @param resultFile
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void exportData(IDatabaseConnection iconn, List<String> tableNameList, String resultFile) throws SQLException, DatabaseUnitException, FileNotFoundException, IOException {
        QueryDataSet dataSet = null;
        if (iconn == null) {
            return;
        }
        if (tableNameList == null || tableNameList.size() == 0) {
            return;
        }
        try {
            dataSet = new QueryDataSet(iconn);
            for (String tableName : tableNameList) {
                dataSet.addTable(tableName);
            }
        } finally {
            if (dataSet != null) {
                FlatXmlDataSet.write(dataSet, new FileOutputStream(resultFile));
            }
        }

    }
}
