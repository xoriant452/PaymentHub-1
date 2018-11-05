package com.citi.paymenthub.kafka.costant;
/**
 * Constant Utils
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
public class ConstantUtils {

	public static final String CONFIG_FILE_PATH="/application.properties";
	public static final String BOOTSTRAP_SERVERS_CONFING="bootstrap-servers";
	public static final String GROUP_ID="group-id";
	public static final String GROUP_ID_1="group-id-1";
	public static final String LATEST="latest";
	
	public static final String AUTOOFFSETRESET = "earliest";
	public static final String ISOLATIONLEVEL = "read_committed";
	public static final String AUTOCOMMIT = "false";
	public static final Long FETCHMAXBYTESIZE = 2147483647l;
	
	public static final String MESSAGEKEY="messageKey";
	public static final String MESSAGE="message";
	public static final String PARTITIONNO="partition";
	public static final String OFFSET="offset";
	public static final String TIMESTAMP="timestamp";
	public static final String CONTENT="content";
	public static final String PAGESIZE="pageSize";
	public static final String PAGENUMBER="number";
	public static final String PAYPAGENUMBER="pageNumber";
	public static final String TOTALELEMENTS="totalElements";
	public static final String TOTALPAGES="totalPages";
	public static final String CONSUMER="consumer";
	public static final String UTER="UTER";
	public static final String ERRORMESSAGETOPIC="EU.TOPIC.ERROR.MESSAGES";
	
	public static final String _ID= "_id";
	public static final String STATUS= "status";
	public static final String STATUS_P= "P";
	public static final String STATUS_A= "A";
	public static final String STATUS_R= "R";
	public static final String BLANK= "";
	public static final String LIST= "list";
	public static final String APPROVED= "Approved";
	public static final String REJECTED= "Rejected";
	public static final String SYSTEM_ADMIN_MK="System Maker";
	public static final String SYSTEM_ADMIN_CK="System Checker";
	public static final String LASTOFFSET="lastOffset";
	public static final String STARTOFFSET="startOffset";
	
	public static final String MESSAGEUTER="uter";
	public static final String TOPICNAME="topicName";
	public static final String MSGTIMESTAMP="msgTimestamp";
	public static final String MSGDATA="msgData";
	
	public static final String TRANS="TRANS";
	public static final String MASTER="MASTER";
	
}
