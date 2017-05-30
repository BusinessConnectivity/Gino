package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.DealCategoryModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import static com.bizconnectivity.gino.Common.*;
import static com.bizconnectivity.gino.webservices.ConstantWS.*;

public class RetrieveDealCategoryWS {

    public static List<DealCategoryModel> invokeRetrieveDealCategory() {

        List<DealCategoryModel> dealCategoryModelList = new ArrayList<>();

        //create request
        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_DEAL_CATEGORY);

        //create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //set output SOAP object
        envelope.setOutputSoapObject(request);
        //create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 2000);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_DEAL_CATEGORY, envelope);

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

                        DealCategoryModel dealCategoryModel = new DealCategoryModel();

                        if (isValidateProperty(table, "DealCategoryId")) {
                            dealCategoryModel.setDealCategoryID(Integer.parseInt(dataReturn(table, "DealCategoryId")));
                        }

                        if (isValidateProperty(table, "DealCategoryName")) {
                            dealCategoryModel.setDealCategoryName(dataReturn(table, "DealCategoryName"));
                        }

                        if (isValidateProperty(table, "ImageFile")) {
                            dealCategoryModel.setImageFile(dataReturn(table, "ImageFile"));
                        }

                        if (isValidateProperty(table, "ImageName")) {
                            dealCategoryModel.setImageName(dataReturn(table, "ImageName"));
                        }

                        if (isValidateProperty(table, "ImageExt")) {
                            dealCategoryModel.setImageExt(dataReturn(table, "ImageExt"));
                        }

                        dealCategoryModelList.add(dealCategoryModel);
                    }
                }
            }

        } catch (Exception e) {

            Log.d("TAG", "invokeRetrieveDealCategory: " + e.toString());
        }

        return dealCategoryModelList;
    }

}
