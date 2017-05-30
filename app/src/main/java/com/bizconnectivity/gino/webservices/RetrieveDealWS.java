package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.DealModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bizconnectivity.gino.Common.dataReturn;
import static com.bizconnectivity.gino.Common.isValidateProperty;
import static com.bizconnectivity.gino.Constant.format1;
import static com.bizconnectivity.gino.Constant.format2;
import static com.bizconnectivity.gino.webservices.ConstantWS.NAMESPACE;
import static com.bizconnectivity.gino.webservices.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.gino.webservices.ConstantWS.URL;
import static com.bizconnectivity.gino.webservices.ConstantWS.WS_RETRIEVE_DEAL;

public class RetrieveDealWS {

    public static List<DealModel> invokeRetrieveDeal() {

        List<DealModel> dealModelList = new ArrayList<>();

        //create request
        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_DEAL);

        //create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //set output SOAP object
        envelope.setOutputSoapObject(request);
        //create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_DEAL, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeRetrieveDeal: " + envelope.bodyIn.toString());

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

                        DealModel dealModel = new DealModel();

                        if (isValidateProperty(table, "DealId")) {
                            dealModel.setDealID(Integer.parseInt(dataReturn(table, "DealId")));
                        }

                        if (isValidateProperty(table, "DealName")) {
                            dealModel.setDealName(dataReturn(table, "DealName"));
                        }

                        if (isValidateProperty(table, "DealDescription")) {
                            dealModel.setDealDescription(dataReturn(table, "DealDescription"));
                        }

                        if (isValidateProperty(table, "DealPromoStartDate")) {
                            dealModel.setDealPromoStartDate(dataReturn(table, "DealPromoStartDate"));
                        }

                        if (isValidateProperty(table, "DealPromoEndDate")) {
                            dealModel.setDealPromoEndDate(dataReturn(table, "DealPromoEndDate"));
                        }

                        if (isValidateProperty(table, "DealRedeemStartDate")) {
                            dealModel.setDealRedeemStartDate(dataReturn(table, "DealRedeemStartDate"));
                        }

                        if (isValidateProperty(table, "DealRedeemEndDate")) {
                            dealModel.setDealRedeemEndDate(dataReturn(table, "DealRedeemEndDate"));
                        }

                        if (isValidateProperty(table, "DealUsualPrice")) {
                            dealModel.setDealUsualPrice(dataReturn(table, "DealUsualPrice"));
                        }

                        if (isValidateProperty(table, "DealPromoPrice")) {
                            dealModel.setDealPromoPrice(dataReturn(table, "DealPromoPrice"));
                        }

                        if (isValidateProperty(table, "DealLocation")) {
                            dealModel.setDealLocation(dataReturn(table, "DealLocation"));
                        }

                        if (isValidateProperty(table, "ImageFile")) {
                            dealModel.setDealImageFile(dataReturn(table, "ImageFile"));
                        }

                        if (isValidateProperty(table, "ImageName")) {
                            dealModel.setDealImageName(dataReturn(table, "ImageName"));
                        }

                        if (isValidateProperty(table, "ImageExt")) {
                            dealModel.setDealImageExt(dataReturn(table, "ImageExt"));
                        }

                        if (isValidateProperty(table, "DealCategoryId")) {
                            dealModel.setDealCategoryID(Integer.parseInt(dataReturn(table, "DealCategoryId")));
                        }

                        if (isValidateProperty(table, "MerchantId")) {
                            dealModel.setMerchantID(Integer.parseInt(dataReturn(table, "MerchantId")));
                        }

                        if (isValidateProperty(table, "DealNoOfView")) {
                            dealModel.setDealNoOfView(Integer.parseInt(dataReturn(table, "DealNoOfView")));
                        }

                        if (isValidateProperty(table, "CreatedDate")) {

                            String formattedDate = format2.format(format1.parse(dataReturn(table, "CreatedDate")));
                            Date date = format2.parse(formattedDate);
                            dealModel.setCreatedDate(date);
                        }

                        dealModelList.add(dealModel);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dealModelList;
    }
}
