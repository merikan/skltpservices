package se.skl.components.pull.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import se.skl.components.pull.domain.GetUpdatesStatus;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Author: Henrik Rostam
 */

@Repository
public class GetUpdatesStatusRepositoryImpl extends JdbcDaoSupport implements GetUpdatesStatusRepository {

    @Autowired
    public void setAutowiredDataSource(@Qualifier("eiPullDataSource") final DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    private final String TABLENAME = "pulldata";

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private final ParameterizedRowMapper<GetUpdatesStatus> MAPPER = new ParameterizedRowMapper<GetUpdatesStatus>() {
        public GetUpdatesStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            GetUpdatesStatus status = new GetUpdatesStatus();
            status.setLogicalAddress(rs.getString("logicalpulladdress"));
            status.setServiceDomain(rs.getString("pullservicedomain"));
            try {
                status.setLastSuccess(simpleDateFormat.parse(rs.getString("lastsuccess")));
            } catch (Exception e) {
                status.setLastSuccess(null);
            }
            status.setAmountOfErrorsSinceLastSuccess(rs.getInt("errorssincelastsuccess"));
            return status;
        }
    };

    public List<GetUpdatesStatus> fetchAll() {
        return this.getJdbcTemplate().query("SELECT * FROM " + TABLENAME, MAPPER);
    }

    public GetUpdatesStatus getStatusForLogicalAddressAndServiceContract(String logicalPullAddress, String pullServiceDomain) {
        try {
            return this.getJdbcTemplate().queryForObject("SELECT * FROM " + TABLENAME + " WHERE logicalpulladdress = ? AND pullservicedomain = ?", MAPPER, logicalPullAddress, pullServiceDomain);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void save(GetUpdatesStatus status) {
        String sqlInsert = "INSERT INTO " + TABLENAME + " (logicalpulladdress, pullservicedomain, lastsuccess, errorssincelastsuccess) VALUES (?, ?, ?, ?)";
        this.getJdbcTemplate().update(sqlInsert, status.getLogicalAddress(), status.getServiceDomain(), simpleDateFormat.format(status.getLastSuccess()), status.getAmountOfErrorsSinceLastSuccess());
    }

    public void update(GetUpdatesStatus status) {
        String sqlUpdate = "UPDATE " + TABLENAME + " SET lastsuccess = ?, errorssincelastsuccess = ? WHERE logicalpulladdress = ? AND pullservicedomain = ?";
        this.getJdbcTemplate().update(sqlUpdate, simpleDateFormat.format(status.getLastSuccess()), status.getAmountOfErrorsSinceLastSuccess(), status.getLogicalAddress(), status.getServiceDomain());
    }

    public void delete(GetUpdatesStatus status) {
        String sqlDelete = "DELETE FROM " + TABLENAME + " WHERE logicalpulladdress = ? AND pullservicedomain = ?";
        this.getJdbcTemplate().update(sqlDelete, status.getLogicalAddress(), status.getServiceDomain());
    }

    @PostConstruct
    private void initDb() throws SQLException {
        if (!tableExists(TABLENAME)) {
            String createTableSql =
                    "CREATE TABLE " + TABLENAME + " " +
                    "(" +
                         "logicalpulladdress VARCHAR(255) NOT NULL, " +
                         "pullservicedomain VARCHAR(255) NOT NULL, " +
                         "lastsuccess CHAR(14) NOT NULL, " +
                         "errorssincelastsuccess INT, " +
                         "PRIMARY KEY (logicalpulladdress, pullservicedomain)" +
                    ")";
            this.getJdbcTemplate().update(createTableSql);
        }
        // Do nothing, table already exists
    }

    private boolean tableExists(String tableName) throws SQLException {
        // Statements such as "CREATE IF NOT EXIST" doesn't exist in Derby, checking by selecting count from table
        try {
            this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM " + tableName);
        } catch (BadSqlGrammarException e) {
            // This exception is thrown when a query was performed on a non-existing table.
            return false;
        }
        // No Exception, so the table does exist.
        return true;
    }

}
