package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.Merchant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
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
import static com.bizconnectivity.gino.ConstantWS.WS_RETRIEVE_MERCHANTS;

public class RetrieveMerchantWS {

    public static List<Merchant> invokeRetrieveMerchant() {

        List<Merchant> merchantList = new ArrayList<>();

        //create request
        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_MERCHANTS);

        //create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //set output SOAP object
        envelope.setOutputSoapObject(request);
        //create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_MERCHANTS, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeRetrieveMerchant: " + envelope.bodyIn.toString());

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

                        Merchant merchant = new Merchant();

                        if (isValidateProperty(table, "MerchantId")) {
                            merchant.setMerchantID(Integer.parseInt(dataReturn(table, "MerchantId")));
                        }

                        if (isValidateProperty(table, "MerchantName")) {
                            merchant.setMerchantName(dataReturn(table, "MerchantName"));
                        }

                        merchantList.add(merchant);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return merchantList;
    }
}
