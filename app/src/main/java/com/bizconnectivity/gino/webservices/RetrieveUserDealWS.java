package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.UserDealModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import static com.bizconnectivity.gino.Common.dataReturn;
import static com.bizconnectivity.gino.Common.isValidateProperty;
import static com.bizconnectivity.gino.webservices.ConstantWS.NAMESPACE;
import static com.bizconnectivity.gino.webservices.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.gino.webservices.ConstantWS.URL;
import static com.bizconnectivity.gino.webservices.ConstantWS.WS_RETRIEVE_USER_DEAL;

public class RetrieveUserDealWS {

    public static List<UserDealModel> invokeRetrieveUserDeal(int memberId) {

        List<UserDealModel> userDealModelList = new ArrayList<>();

        //create request
        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_USER_DEAL);

        //property which holds input parameters
        PropertyInfo memberIdPI = new PropertyInfo();
        //set name
        memberIdPI.setName("memberId");
        //set value
        memberIdPI.setValue(memberId);
        //set datatype
        memberIdPI.setType(int.class);
        //add the property to request object
        request.addProperty(memberIdPI);

        //create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //set output SOAP object
        envelope.setOutputSoapObject(request);
        //create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_USER_DEAL, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeRetrieveUserDeal: " + envelope.bodyIn.toString());

            } else {

                SoapObject response = (SoapObject) envelope.bodyIn;

                //get the desired property
                SoapObject soapObject = (SoapObject) response.getProperty(0);

                //retrieve diffGram from property
                SoapObject diffGram = (SoapObject) soapObject.getProperty("diffgram");

                if (!soapObject.getProperty("diffgram").toString().equals("anyType{}")) {

                    //retrieve document element from diffGram
                    SoapObject newDataSet = (SoapObject) diffGram.getProperty("DocumentElement");

                    for (int i=0; i<newDataSet.getPropertyCount(); i++) {

                        //get the number of dataSet
                        SoapObject table = (SoapObject) newDataSet.getProperty(i);

                        UserDealModel userDealModel = new UserDealModel();

                        if (isValidateProperty(table, "MemberDealId")) {
                            userDealModel.setUserDealID(Integer.parseInt(dataReturn(table, "MemberDealId")));
                        }

                        if (isValidateProperty(table, "MemberId")) {
                            userDealModel.setUserID(Integer.parseInt(dataReturn(table, "MemberId")));
                        }

                        if (isValidateProperty(table, "DealId")) {
                            userDealModel.setDealID(Integer.parseInt(dataReturn(table, "DealId")));
                        }

                        if (isValidateProperty(table, "Quantity")) {
                            userDealModel.setQuantity(Integer.parseInt(dataReturn(table, "Quantity")));
                        }

                        if (isValidateProperty(table, "RedeemedDate")) {
                            userDealModel.setRedeemedDate(dataReturn(table, "RedeemedDate"));
                        }

                        if (isValidateProperty(table, "IsRedeemed")) {
                            userDealModel.setRedeemed(Boolean.parseBoolean(dataReturn(table, "IsRedeemed")));
                        }

                        if (isValidateProperty(table, "IsExpired")) {
                            userDealModel.setExpired(Boolean.parseBoolean(dataReturn(table, "IsExpired")));
                        }

                        if (isValidateProperty(table, "CreatedDate")) {
                            userDealModel.setCreatedDate(dataReturn(table, "CreatedDate"));
                        }

                        userDealModelList.add(userDealModel);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userDealModelList;
    }
}
