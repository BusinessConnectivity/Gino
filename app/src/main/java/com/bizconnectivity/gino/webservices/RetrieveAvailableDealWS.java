package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.PurchasedDeal;

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
import static com.bizconnectivity.gino.ConstantWS.NAMESPACE;
import static com.bizconnectivity.gino.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.gino.ConstantWS.URL;
import static com.bizconnectivity.gino.ConstantWS.WS_RETRIEVE_AVAILABLE_DEAL;

public class RetrieveAvailableDealWS {

    public static List<PurchasedDeal> invokeRetrieveAvailableDeal(int memberId) {

        List<PurchasedDeal> purchasedDealList = new ArrayList<>();

        //create request
        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_AVAILABLE_DEAL);

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
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_AVAILABLE_DEAL, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeRetrieveHistoryDeal: " + envelope.bodyIn.toString());

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

                        PurchasedDeal purchasedDeal = new PurchasedDeal();

                        if (isValidateProperty(table, "MemberDealId")) {
                            purchasedDeal.setUserDealId(Integer.parseInt(dataReturn(table, "MemberDealId")));
                        }

                        if (isValidateProperty(table, "DealId")) {
                            purchasedDeal.setDealId(Integer.parseInt(dataReturn(table, "DealId")));
                        }

                        if (isValidateProperty(table, "DealName")) {
                            purchasedDeal.setDealName(dataReturn(table, "DealName"));
                        }

                        if (isValidateProperty(table, "ImageFile")) {
                            purchasedDeal.setImageFile(dataReturn(table, "ImageFile"));
                        }

                        if (isValidateProperty(table, "DealRedeemEndDate")) {

                            purchasedDeal.setDealRedeemEndDate(dataReturn(table, "DealRedeemEndDate"));
                        }

                        purchasedDealList.add(purchasedDeal);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return purchasedDealList;
    }
}
