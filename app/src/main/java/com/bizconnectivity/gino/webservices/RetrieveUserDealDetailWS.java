package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.UserDeal;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.gino.Common.dataReturn;
import static com.bizconnectivity.gino.Common.isValidateProperty;
import static com.bizconnectivity.gino.ConstantWS.NAMESPACE;
import static com.bizconnectivity.gino.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.gino.ConstantWS.URL;
import static com.bizconnectivity.gino.ConstantWS.WS_RETRIEVE_USER_DEAL_DETAIL;

public class RetrieveUserDealDetailWS {

    public static UserDeal invokeRetrieveUserDealDetail(int memberDealId) {

        UserDeal userDeal = new UserDeal();

        //create request
        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_USER_DEAL_DETAIL);

        //property which holds input parameters
        PropertyInfo memberDealIdPI = new PropertyInfo();
        //set name
        memberDealIdPI.setName("memberDealId");
        //set value
        memberDealIdPI.setValue(memberDealId);
        //set datatype
        memberDealIdPI.setType(int.class);
        //add the property to request object
        request.addProperty(memberDealIdPI);

        //create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //set output SOAP object
        envelope.setOutputSoapObject(request);
        //create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_USER_DEAL_DETAIL, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeRetrieveUserDealDetail: " + envelope.bodyIn.toString());

            } else {

                SoapObject response = (SoapObject) envelope.bodyIn;

                //get the desired property
                SoapObject soapObject = (SoapObject) response.getProperty(0);

                //retrieve diffGram from property
                SoapObject diffGram = (SoapObject) soapObject.getProperty("diffgram");

                if (!soapObject.getProperty("diffgram").toString().equals("anyType{}")) {

                    //retrieve document element from diffGram
                    SoapObject newDataSet = (SoapObject) diffGram.getProperty("DocumentElement");

                    //get the number of dataSet
                    SoapObject table = (SoapObject) newDataSet.getProperty(0);

                    if (isValidateProperty(table, "MemberDealId")) {
                        userDeal.setUserDealId(Integer.parseInt(dataReturn(table, "MemberDealId")));
                    }

                    if (isValidateProperty(table, "MemberId")) {
                        userDeal.setUserId(Integer.parseInt(dataReturn(table, "MemberId")));
                    }

                    if (isValidateProperty(table, "DealId")) {
                        userDeal.setDealId(Integer.parseInt(dataReturn(table, "DealId")));
                    }

                    if (isValidateProperty(table, "Quantity")) {
                        userDeal.setQuantity(Integer.parseInt(dataReturn(table, "Quantity")));
                    }

                    if (isValidateProperty(table, "TotalPrice")) {
                        userDeal.setTotalPrice(dataReturn(table, "TotalPrice"));
                    }

                    if (isValidateProperty(table, "SubTotalPrice")) {
                        userDeal.setSubtotalPrice(dataReturn(table, "SubTotalPrice"));
                    }

                    if (isValidateProperty(table, "CreatedDate2")) {
                        userDeal.setCreatedDate(dataReturn(table, "CreatedDate"));
                    }

                    if (isValidateProperty(table, "DealName")) {
                        userDeal.setDealName(dataReturn(table, "DealName"));
                    }

                    if (isValidateProperty(table, "ImageFile")) {
                        userDeal.setImageFile(dataReturn(table, "ImageFile"));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userDeal;
    }
}
