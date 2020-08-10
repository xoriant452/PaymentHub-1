package com.xoriant.poc.errordashboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xoriant.poc.errordashboard.dao.DashboardInfo;

@Repository
public interface DashboardInfoRepository extends JpaRepository<DashboardInfo, Long>{

	@Query(value="SELECT * FROM errordetails e WHERE e.target_system = :targetSystem",nativeQuery = true)
	public Page<DashboardInfo> findErrorByTargetSystem(@Param("targetSystem") String targetSystem,Pageable pageable);
	
	@Query(value="SELECT * FROM errordetails e WHERE e.transaction_name = :transactionName",nativeQuery = true)
	public Page<DashboardInfo> findErrorByTransactionName(@Param("transactionName") String transactionName,Pageable pageable);
	
	@Query(value="SELECT * FROM errordetails e WHERE e.application_name = :applicationName",nativeQuery = true)
	public Page<DashboardInfo> findErrorByApplicationName(@Param("applicationName") String applicationName,Pageable pageable);
	
	@Query(value="SELECT * FROM errordetails e WHERE e.error_timestamp <= :toDate AND e.error_timestamp >= :fromDate",nativeQuery = true)
	public Page<DashboardInfo> findErrorByDateRange(@Param("fromDate") String fromDate,@Param("toDate") String toDate ,Pageable pageable);
	
	@Query(value="SELECT * FROM errordetails e WHERE e.application_name = :applicationName and e.transaction_name = :transactionName",nativeQuery = true)
	public Page<DashboardInfo> findErrorByAggregatedFilters(@Param("applicationName") String applicationName,@Param("transactionName") String transactionName,Pageable pageable);
	
	@Query(value="SELECT * FROM errordetails e WHERE e.application_name = :applicationName and e.transaction_name = :transactionName and e.error_timestamp <= :toDate and e.error_timestamp >= :fromDate",nativeQuery = true)
	public Page<DashboardInfo> findErrorByAggregatedFiltersbyDate(@Param("applicationName") String applicationName,@Param("transactionName") String transactionName,@Param("fromDate") String fromDate,@Param("toDate") String toDate,Pageable pageable);
	
	@Query(value="SELECT * FROM errordetails e WHERE e.application_name = :applicationName and e.error_timestamp <= :toDate and e.error_timestamp >= :fromDate",nativeQuery = true)
	public Page<DashboardInfo> findErrorByApplicationNameFiltersbyDate(@Param("applicationName") String applicationName,@Param("fromDate") String fromDate,@Param("toDate") String toDate,Pageable pageable);
	
	@Query(value="SELECT * FROM errordetails e WHERE e.transaction_name = :transactionName and e.error_timestamp <= :toDate and e.error_timestamp >= :fromDate",nativeQuery = true)
	public Page<DashboardInfo> findErrorByTransactiionNameFiltersbyDate(@Param("transactionName") String transactionName,@Param("fromDate") String fromDate,@Param("toDate") String toDate,Pageable pageable);
	
}
