package se.skl.components.pull.repository;

import se.skl.components.pull.domain.GetUpdatesStatus;

import java.util.List;

/**
 * Author: Henrik Rostam
 */

public interface GetUpdatesStatusRepository {

    List<GetUpdatesStatus> fetchAll();

    GetUpdatesStatus getStatusForLogicalAddressAndServiceContract(String logicalPullAddress, String pullServiceDomain);

    void save(GetUpdatesStatus status);

    void update(GetUpdatesStatus status);

    void delete(GetUpdatesStatus status);

}
