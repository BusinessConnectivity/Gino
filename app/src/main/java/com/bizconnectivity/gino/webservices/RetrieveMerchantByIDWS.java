package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.MerchantModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.gino.Common.dataReturn;
import static com.bizconnectivity.gino.Common.isValidateProperty;
import static com.bizconnectivity.gino.webservices.ConstantWS.NAMESPACE;
import static com.bizconnectivity.gino.webservices.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.gino.webservices.ConstantWS.URL;
import static com.bizconnectivity.gino.webservices.ConstantWS.WS_RETRIEVE_MERCHANT_BY_ID;

public class RetrieveMerchantByIDWS {

    public static MerchantModel invokeRetrieveMerchantByID(int merchantId) {

        MerchantModel merchantModel = new MerchantModel();

        //create request
        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_MERCHANT_BY_ID);

        //property which holds input parameters
        PropertyInfo merchantIdPI = new PropertyInfo();
        //set name
        merchantIdPI.setName("merchantId");
        //set value
        merchantIdPI.setValue(merchantId);
        //set datatype
        merchantIdPI.setType(int.class);
        //add the property to request object
        request.addProperty(merchantIdPI);

        //create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //set output SOAP object
        envelope.setOutputSoapObject(request);
        //create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_MERCHANT_BY_ID, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeRetrieveMerchantByID: " + envelope.bodyIn.toString());

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

                    if (isValidateProperty(table, "MerchantId")) {
                        merchantModel.setMerchantID(Integer.parseInt(dataReturn(table, "MerchantId")));
                    }

                    if (isValidateProperty(table, "MerchantName")) {
                        merchantModel.setMerchantName(dataReturn(table, "MerchantName"));
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return merchantModel;
    }
}
