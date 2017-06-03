package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.Deal;

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
import static com.bizconnectivity.gino.ConstantWS.WS_RETRIEVE_DEAL_BY_ID;

public class RetrieveDealByIdWS {

    public static Deal invokeRetrieveDealById(int dealId) {

        Deal deal = new Deal();

        //create request
        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_DEAL_BY_ID);

        //property which holds input parameters
        PropertyInfo dealIdPI = new PropertyInfo();
        //set name
        dealIdPI.setName("dealId");
        //set value
        dealIdPI.setValue(dealId);
        //set datatype
        dealIdPI.setType(int.class);
        //add the property to request object
        request.addProperty(dealIdPI);

        //create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //set output SOAP object
        envelope.setOutputSoapObject(request);
        //create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_DEAL_BY_ID, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeRetrieveDealById: " + envelope.bodyIn.toString());

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

                    if (isValidateProperty(table, "DealId")) {
                        deal.setDealID(Integer.parseInt(dataReturn(table, "DealId")));
                    }

                    if (isValidateProperty(table, "DealName")) {
                        deal.setDealName(dataReturn(table, "DealName"));
                    }

                    if (isValidateProperty(table, "DealDescription")) {
                        deal.setDealDescription(dataReturn(table, "DealDescription"));
                    }

                    if (isValidateProperty(table, "DealPromoStartDate")) {
                        deal.setDealPromoStartDate(dataReturn(table, "DealPromoStartDate"));
                    }

                    if (isValidateProperty(table, "DealPromoEndDate")) {
                        deal.setDealPromoEndDate(dataReturn(table, "DealPromoEndDate"));
                    }

                    if (isValidateProperty(table, "DealRedeemStartDate")) {
                        deal.setDealRedeemStartDate(dataReturn(table, "DealRedeemStartDate"));
                    }

                    if (isValidateProperty(table, "DealRedeemEndDate")) {
                        deal.setDealRedeemEndDate(dataReturn(table, "DealRedeemEndDate"));
                    }

                    if (isValidateProperty(table, "DealUsualPrice")) {
                        deal.setDealUsualPrice(dataReturn(table, "DealUsualPrice"));
                    }

                    if (isValidateProperty(table, "DealPromoPrice")) {
                        deal.setDealPromoPrice(dataReturn(table, "DealPromoPrice"));
                    }

                    if (isValidateProperty(table, "DealLocation")) {
                        deal.setDealLocation(dataReturn(table, "DealLocation"));
                    }

                    if (isValidateProperty(table, "ImageFile")) {
                        deal.setDealImageFile(dataReturn(table, "ImageFile"));
                    }

                    if (isValidateProperty(table, "ImageName")) {
                        deal.setDealImageName(dataReturn(table, "ImageName"));
                    }

                    if (isValidateProperty(table, "ImageExt")) {
                        deal.setDealImageExt(dataReturn(table, "ImageExt"));
                    }

                    if (isValidateProperty(table, "DealCategoryId")) {
                        deal.setDealCategoryID(Integer.parseInt(dataReturn(table, "DealCategoryId")));
                    }

                    if (isValidateProperty(table, "MerchantId")) {
                        deal.setMerchantID(Integer.parseInt(dataReturn(table, "MerchantId")));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return deal;
    }
}
