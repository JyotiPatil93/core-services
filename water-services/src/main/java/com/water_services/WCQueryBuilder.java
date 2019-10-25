package com.water_services;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WCQueryBuilder {
	@Autowired
    private JdbcTemplate jdbcTemplate;

    private WCConfiguration config;

    @Autowired
    public WCQueryBuilder(WCConfiguration config) {
        this.config = config;
    }

    private static final String INNER_JOIN_STRING = " INNER JOIN ";
    private static final String LEFT_OUTER_JOIN_STRING = " LEFT OUTER JOIN ";

    /*private static final String QUERY = "SELECT tl.*,tld.*,tlunit.*,tlacc.*,tlowner.*," +
            "tladdress.*,tlapldoc.*,tlverdoc.*,tlownerdoc.*,tlinsti.*,tl.id as tl_id,tl.tenantid as tl_tenantId,tl.lastModifiedTime as " +
            "tl_lastModifiedTime,tl.createdBy as tl_createdBy,tl.lastModifiedBy as tl_lastModifiedBy,tl.createdTime as " +
            "tl_createdTime,tld.id as tld_id,tladdress.id as tl_ad_id,tld.createdBy as tld_createdBy," +
            "tlowner.id as tlowner_uuid,tlowner.active as useractive," +
            "tld.createdTime as tld_createdTime,tld.lastModifiedBy as tld_lastModifiedBy,tld.createdTime as " +
            "tld_createdTime,tlunit.id as tl_un_id,tlunit.tradeType as tl_un_tradeType,tlunit.uom as tl_un_uom,tlunit.active as tl_un_active," +
            "tlunit.uomvalue as tl_un_uomvalue,tlacc.id as tl_acc_id,tlacc.uom as tl_acc_uom,tlacc.uomvalue as tl_acc_uomvalue,tlacc.active as tl_acc_active," +
            "tlapldoc.id as tl_ap_doc_id,tlapldoc.documenttype as tl_ap_doc_documenttype,tlapldoc.filestoreid as tl_ap_doc_filestoreid,tlapldoc.active as tl_ap_doc_active," +
            "tlverdoc.id as tl_ver_doc_id,tlverdoc.documenttype as tl_ver_doc_documenttype,tlverdoc.filestoreid as tl_ver_doc_filestoreid,tlverdoc.active as tl_ver_doc_active," +
            "tlownerdoc.userid as docuserid,tlownerdoc.tradeLicenseDetailId as doctradelicensedetailid,tlownerdoc.id as ownerdocid,"+
            "tlownerdoc.documenttype as ownerdocType,tlownerdoc.filestoreid as ownerfileStoreId,tlownerdoc.documentuid as ownerdocuid,tlownerdoc.active as ownerdocactive," +
            " tlinsti.id as instiid,tlinsti.name as institutionname,tlinsti.type as institutiontype,tlinsti.tenantid as institenantId,tlinsti.active as instiactive "+
            " FROM eg_tl_tradelicense tl"
            +INNER_JOIN_STRING
            +"eg_tl_tradelicensedetail tld ON tld.tradelicenseid = tl.id"
            +INNER_JOIN_STRING
            +"eg_tl_address tladdress ON tladdress.tradelicensedetailid = tld.id"
            +INNER_JOIN_STRING
            +"eg_tl_owner tlowner ON tlowner.tradelicensedetailid = tld.id"
            +INNER_JOIN_STRING
            +"eg_tl_tradeunit tlunit ON tlunit.tradelicensedetailid = tld.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_tl_accessory tlacc ON tlacc.tradelicensedetailid = tld.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_tl_document_owner tlownerdoc ON tlownerdoc.userid = tlowner.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_tl_applicationdocument tlapldoc ON tlapldoc.tradelicensedetailid = tld.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_tl_verificationdocument tlverdoc ON tlverdoc.tradelicensedetailid = tld.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_tl_institution tlinsti ON tlinsti.tradelicensedetailid = tld.id ";
*/
    private static final String QUERY="SELECT * FROM water_service_connection";
    
    private static final String INSERTQUERY= "INSERT INTO water_service_connection(id,property_id,applicationno,applicationstatus,status,connectionno,oldconnectionno,documents_id,connectioncategory,rainwaterharvesting,connectiontype,watersource,meterid,meterinstallationdate) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   //INSERT INTO address(id ,tenantid ,doorno ,plotno ,landmark ,city,district ,region,state , country ,pincode ,additiondetails ,buildingname ,locality_id ,geolocation_id) VALUES ";
    //INSERT INTO auditdetails(id,createdby,lastmodifiedby,createdtime,lastmodifiedtime) VALUES ;
    //INSERT INTO constructiondetail(id,totalunitarea,builtuparea,carpetarea,superbuiltuparea,constructiontype,constructiondate,dimensions,auditdetails_id,additionaldetails) VALUES ;
    //INSERT INTO documents(id,documenttype,filestore,documentuid,additionaldetails) VALUES ;
    //INSERT INTO geolocation(id,latitude,longitude,additionaldetails) VALUES ;
    //INSERT INTO institution(id,tenantid,type,designation,nameofauthorizedperson,additionaldetails) VALUES ;
    //INSERT INTO locality(id,code,label,latitude,longitude,children,materializedpath) VALUES ;
    //INSERT INTO owners(id,name,mobilenumber,gender,fatherorhusbandname,correspondenceaddress,isprimaryowner,ownershippercentage,ownertype,institutionid,document_id,relationship,additionaldetails) VALUES ;
    //INSERT INTO property(id,propertyid,linkpropertyid,tenantid,accountid,oldpropertyid,status,address_id,acknowldgementnumber,propertytype,owners_id,institution_id,creationreason,nooffloors,landarea,source,channel,documents_id,unit_id,additionaldetails) VALUES ;
    //INSERT INTO unit(id,tenantid,floorno,unittype,usagecategory,occupancytype,occupancydate,constructiondetail_id,additionaldetails) VALUES ;
      private final String paginationWrapper = "SELECT * FROM " +
              "(SELECT *, DENSE_RANK() OVER (ORDER BY tl_id) offset_ FROM " +
              "({})" +
              " result) result_offset " +
              "WHERE offset_ > ? AND offset_ <= ?";


      public String getWCSearchQuery(WaterConnectionSearchCriteria criteria, List<Object> preparedStmtList)
      {
     
        StringBuilder builder = new StringBuilder(QUERY);

//          if(criteria.getAccountId()!=null){
            addClauseIfRequired(preparedStmtList,builder);
//              builder.append(" accountid = ? ");
              preparedStmtList.add(criteria.getAccountId());

              /*List<String> ownerIds = criteria.getOwnerIds();
              if(!CollectionUtils.isEmpty(ownerIds)) {
                  builder.append(" OR (tlowner.id IN (").append(createQuery(ownerIds)).append(")");
                  addToPreparedStatement(preparedStmtList,ownerIds);
                  builder.append(" AND tlowner.active = ? )");
                  preparedStmtList.add(true);
              }*/

//              return builder.toString();
          
          
          return QUERY;
      }
      
      public String getWCInsertQuery(List<WaterConnection> waterConnectionList)
      {
        int lSize=waterConnectionList.size();
        
        StringBuilder builderWaterConnection = new StringBuilder(INSERTQUERY);
        
        
        
        for(int i=0;i<lSize;i++){
        	
        	WaterConnection w= waterConnectionList.get(i);
        	List<String> documentIds=new ArrayList<>();
        	
        	List<Document> listOfDocuments=w.getDocuments();
        	for(int j=0;j<listOfDocuments.size();j++){
        		documentIds.add(listOfDocuments.get(j).getId());
        	}
        	
        	jdbcTemplate.update(INSERTQUERY, w.getId(),w.getProperty().getPropertyId(),w.getApplicationNo(),w.getApplicationStatus(),w.getStatus(),
        			w.getConnectionNo(),
        			w.getOldConnectionNo(),documentIds.toString(),w.getConnectionCategory(),w.isRainWaterHarvesting(),w.getConnectionType(),
        			w.getWaterSource(),w.getMeterId(),w.getMeterInstallationDate());
        	
//        	builderWaterConnection.append("(");
//        	builderWaterConnection.append(w.getId()+","+w.getProperty().getId()+","+w.getApplicationNo()+","+w.getApplicationStatus()+","+w.getStatus()+","+w.getConnectionNo()+","+w.getOldConnectionNo()+",");
//        	builderWaterConnection.append(documentIds);
//        	builderWaterConnection.append(","+w.getConnectionCategory()+","+w.get);*/
        }

//          if(criteria.getAccountId()!=null){
//              addClauseIfRequired(preparedStmtList,builder);
//              builder.append(" accountid = ? ");
             

              /*List<String> ownerIds = criteria.getOwnerIds();
              if(!CollectionUtils.isEmpty(ownerIds)) {
                  builder.append(" OR (tlowner.id IN (").append(createQuery(ownerIds)).append(")");
                  addToPreparedStatement(preparedStmtList,ownerIds);
                  builder.append(" AND tlowner.active = ? )");
                  preparedStmtList.add(true);
              }*/

//              return builder.toString();
          
          
          return QUERY;
      }
         
          /*if(criteria.getAccountId()!=null){
              addClauseIfRequired(preparedStmtList,builder);
              builder.append(" tl.accountid = ? ");
              preparedStmtList.add(criteria.getAccountId());

              List<String> ownerIds = criteria.getOwnerIds();
              if(!CollectionUtils.isEmpty(ownerIds)) {
                  builder.append(" OR (tlowner.id IN (").append(createQuery(ownerIds)).append(")");
                  addToPreparedStatement(preparedStmtList,ownerIds);
                  builder.append(" AND tlowner.active = ? )");
                  preparedStmtList.add(true);
              }

              return builder.toString();
          }*/
      

    /*public String getWCSearchQuery(WaterConnectionSearchCriteria criteria, List<Object> preparedStmtList) {

        StringBuilder builder = new StringBuilder(QUERY);

        if(criteria.getAccountId()!=null){
            addClauseIfRequired(preparedStmtList,builder);
            builder.append(" tl.accountid = ? ");
            preparedStmtList.add(criteria.getAccountId());

            List<String> ownerIds = criteria.getOwnerIds();
            if(!CollectionUtils.isEmpty(ownerIds)) {
                builder.append(" OR (tlowner.id IN (").append(createQuery(ownerIds)).append(")");
                addToPreparedStatement(preparedStmtList,ownerIds);
                builder.append(" AND tlowner.active = ? )");
                preparedStmtList.add(true);
            }

            return builder.toString();
        }

        if(criteria.getTenantId()!=null){
            addClauseIfRequired(preparedStmtList,builder);
            builder.append(" tl.tenantid=? ");
            preparedStmtList.add(criteria.getTenantId());
        }


        List<String> ids = criteria.getIds();
        if(!CollectionUtils.isEmpty(ids)) {
            addClauseIfRequired(preparedStmtList,builder);
            builder.append(" tl.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList,ids);
        }

        List<String> ownerIds = criteria.getOwnerIds();
        if(!CollectionUtils.isEmpty(ownerIds)) {
            addClauseIfRequired(preparedStmtList,builder);
            builder.append(" (tlowner.id IN (").append(createQuery(ownerIds)).append(")");
            addToPreparedStatement(preparedStmtList,ownerIds);
            addClauseIfRequired(preparedStmtList,builder);
            builder.append(" tlowner.active = ? ) ");
            preparedStmtList.add(true);
        }

        if(criteria.getApplicationNumber()!=null){
            addClauseIfRequired(preparedStmtList,builder);
            builder.append("  tl.applicationnumber = ? ");
            preparedStmtList.add(criteria.getApplicationNumber());
        }

        if(criteria.getStatus()!=null){
            addClauseIfRequired(preparedStmtList,builder);
            builder.append("  tl.status = ? ");
            preparedStmtList.add(criteria.getStatus());
        }

        if(criteria.getLicenseNumber()!=null){
            addClauseIfRequired(preparedStmtList,builder);
            builder.append("  tl.licensenumber = ? ");
            preparedStmtList.add(criteria.getLicenseNumber());
        }

        if(criteria.getOldLicenseNumber()!=null){
            addClauseIfRequired(preparedStmtList,builder);
            builder.append("  tl.oldlicensenumber = ? ");
            preparedStmtList.add(criteria.getOldLicenseNumber());
        }

        if(criteria.getFromDate()!=null){
            addClauseIfRequired(preparedStmtList,builder);
            builder.append("  tl.applicationDate >= ? ");
            preparedStmtList.add(criteria.getFromDate());
        }

        if(criteria.getToDate()!=null){
            addClauseIfRequired(preparedStmtList,builder);
            builder.append("  tl.applicationDate <= ? ");
            preparedStmtList.add(criteria.getToDate());
        }



       // enrichCriteriaForUpdateSearch(builder,preparedStmtList,criteria);

        return addPaginationWrapper(builder.toString(),preparedStmtList,criteria);
    }




    private String createQuery(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for( int i = 0; i< length; i++){
            builder.append(" ?");
            if(i != length -1) builder.append(",");
        }
        return builder.toString();
    }

    private void addToPreparedStatement(List<Object> preparedStmtList,List<String> ids)
    {
        ids.forEach(id ->{ preparedStmtList.add(id);});
    }


    private String addPaginationWrapper(String query,List<Object> preparedStmtList,
                                      TradeLicenseSearchCriteria criteria){
        int limit = config.getDefaultLimit();
        int offset = config.getDefaultOffset();
        String finalQuery = paginationWrapper.replace("{}",query);

        if(criteria.getLimit()!=null && criteria.getLimit()<=config.getMaxSearchLimit())
            limit = criteria.getLimit();

        if(criteria.getLimit()!=null && criteria.getLimit()>config.getMaxSearchLimit())
            limit = config.getMaxSearchLimit();

        if(criteria.getOffset()!=null)
            offset = criteria.getOffset();

        preparedStmtList.add(offset);
        preparedStmtList.add(limit+offset);

       return finalQuery;
    }*/


    private static void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
        if (values.isEmpty())
            queryString.append(" WHERE ");
        else {
            queryString.append(" AND");
        }
    }


}
