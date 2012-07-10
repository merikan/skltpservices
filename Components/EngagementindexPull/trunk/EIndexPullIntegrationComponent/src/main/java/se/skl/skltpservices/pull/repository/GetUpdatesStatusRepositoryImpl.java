package se.skl.skltpservices.pull.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import se.skl.skltpservices.pull.domain.GetUpdatesStatus;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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

    @Resource(name = "eiPullStatusDbName")
    private String tableName;

    private final ParameterizedRowMapper<GetUpdatesStatus> MAPPER = new ParameterizedRowMapper<GetUpdatesStatus>() {
        public GetUpdatesStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            GetUpdatesStatus status = new GetUpdatesStatus();
            status.setLogicalAddress(rs.getString("logicalpulladdress"));
            status.setServiceDomain(rs.getString("pullservicedomain"));
            try {
                status.setLastSuccess(simpleDateFormat.parse(rs.getString("lastsuccess")));
            } catch (Exception e) {
                // If date was not found or not set, it should be set upon next success
                status.setLastSuccess(null);
            }
            status.setAmountOfErrorsSinceLastSuccess(rs.getInt("errorssincelastsuccess"));
            return status;
        }
    };

    public List<GetUpdatesStatus> fetchAll() {
        return this.getJdbcTemplate().query("SELECT * FROM " + tableName, MAPPER);
    }

    public GetUpdatesStatus getStatusForLogicalAddressAndServiceContract(String logicalPullAddress, String pullServiceDomain) {
        try {
            return this.getJdbcTemplate().queryForObject("SELECT * FROM " + tableName + " WHERE logicalpulladdress = ? AND pullservicedomain = ?", MAPPER, logicalPullAddress, pullServiceDomain);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void save(GetUpdatesStatus status) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String sqlInsert = "INSERT INTO " + tableName + " (logicalpulladdress, pullservicedomain, lastsuccess, errorssincelastsuccess) VALUES (?, ?, ?, ?)";
        String logicalAddress = status.getLogicalAddress();
        String serviceDomain = status.getServiceDomain();
        
        String formattedDate = null;
        if(status.getLastSuccess() != null){
        	formattedDate = simpleDateFormat.format(status.getLastSuccess());
        }
        
        int amountOfErrorsSinceLastSuccess = status.getAmountOfErrorsSinceLastSuccess();
        this.getJdbcTemplate().update(sqlInsert, logicalAddress, serviceDomain, formattedDate, amountOfErrorsSinceLastSuccess);
    }

    public void update(GetUpdatesStatus status) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String sqlUpdate = "UPDATE " + tableName + " SET lastsuccess = ?, errorssincelastsuccess = ? WHERE logicalpulladdress = ? AND pullservicedomain = ?";
       
        String formattedDate = null;
        if(status.getLastSuccess() != null){
        	formattedDate = simpleDateFormat.format(status.getLastSuccess());
        }
        
        int amountOfErrorsSinceLastSuccess = status.getAmountOfErrorsSinceLastSuccess();
        String logicalAddress = status.getLogicalAddress();
        String serviceDomain = status.getServiceDomain();
        this.getJdbcTemplate().update(sqlUpdate, formattedDate, amountOfErrorsSinceLastSuccess, logicalAddress, serviceDomain);
    }

    public void delete(GetUpdatesStatus status) {
        String sqlDelete = "DELETE FROM " + tableName + " WHERE logicalpulladdress = ? AND pullservicedomain = ?";
        this.getJdbcTemplate().update(sqlDelete, status.getLogicalAddress(), status.getServiceDomain());
    }

    @PostConstruct
    private void initDb() throws SQLException {
        if (!tableExists(tableName)) {
            String createTableSql =
                    "CREATE TABLE " + tableName + " " +
                    "(" +
                         "logicalpulladdress VARCHAR(255) NOT NULL, " +
                         "pullservicedomain VARCHAR(255) NOT NULL, " +
                         "lastsuccess CHAR(14), " +
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
