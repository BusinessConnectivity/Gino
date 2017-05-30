package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.FavDealModel;

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
import static com.bizconnectivity.gino.webservices.ConstantWS.WS_RETRIEVE_FAVOURITE_DEAL;

public class RetrieveFavouriteDealWS {

    public static List<FavDealModel> invokeRetrieveFavouriteDeal(int memberId) {

        List<FavDealModel> favDealList = new ArrayList<>();

        //create request
        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_FAVOURITE_DEAL);

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
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_FAVOURITE_DEAL, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeRetrieveFavouriteDeal: " + envelope.bodyIn.toString());

            } else {

                SoapObject response = (SoapObject) envelope.bodyIn;

                //get the desired property
                SoapObject soapObject = (SoapObject) response.getProperty(0);

                //retrieve diffGram from property
                SoapObject diffGram = (SoapObject) soapObject.getProperty("diffgram");

                if (!soapObject.getProperty("diffgram").toString().equals("anyType{}")) {

                    //retrieve document element from diffGram
                    SoapObject newDataSet = (SoapObject) diffGram.getProperty("DocumentElement");

                    for (int i = 0; i < newDataSet.getPropertyCount(); i++) {

                        //get the number of dataSet
                        SoapObject table = (SoapObject) newDataSet.getProperty(i);

                        FavDealModel favDeal = new FavDealModel();

                        if (isValidateProperty(table, "MemberFavDealId")) {
                            favDeal.setUserFavDealID(Integer.parseInt(dataReturn(table, "MemberFavDealId")));
                        }

                        if (isValidateProperty(table, "MemberId")) {
                            favDeal.setUserID(Integer.parseInt(dataReturn(table, "MemberId")));
                        }

                        if (isValidateProperty(table, "DealId")) {
                            favDeal.setDealID(Integer.parseInt(dataReturn(table, "DealId")));
                        }

                        favDealList.add(favDeal);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return favDealList;
    }
}
