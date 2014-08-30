
/**
 * INTSIIE004ReportManagementImplServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.sigaim.siie.ws2;

    /**
     *  INTSIIE004ReportManagementImplServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class INTSIIE004ReportManagementImplServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public INTSIIE004ReportManagementImplServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public INTSIIE004ReportManagementImplServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for createPerformer method
            * override this method for handling normal response from createPerformer operation
            */
           public void receiveResultcreatePerformer(
                    org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.CreatePerformerResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createPerformer operation
           */
            public void receiveErrorcreatePerformer(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createReport method
            * override this method for handling normal response from createReport operation
            */
           public void receiveResultcreateReport(
                    org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.CreateReportResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createReport operation
           */
            public void receiveErrorcreateReport(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateReport method
            * override this method for handling normal response from updateReport operation
            */
           public void receiveResultupdateReport(
                    org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.UpdateReportResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateReport operation
           */
            public void receiveErrorupdateReport(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createHealthcareFacility method
            * override this method for handling normal response from createHealthcareFacility operation
            */
           public void receiveResultcreateHealthcareFacility(
                    org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.CreateHealthcareFacilityResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createHealthcareFacility operation
           */
            public void receiveErrorcreateHealthcareFacility(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createSubjectOfCare method
            * override this method for handling normal response from createSubjectOfCare operation
            */
           public void receiveResultcreateSubjectOfCare(
                    org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.CreateSubjectOfCareResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createSubjectOfCare operation
           */
            public void receiveErrorcreateSubjectOfCare(java.lang.Exception e) {
            }
                


    }
    