package com.bizconnectivity.gino.webservices;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.gino.webservices.ConstantWS.NAMESPACE;
import static com.bizconnectivity.gino.webservices.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.gino.webservices.ConstantWS.URL;
import static com.bizconnectivity.gino.webservices.ConstantWS.WS_UPDATE_DEAL_NO_OF_VIEW;

public class UpdateDealNoOfViewWS {

    public static boolean invokeUpdateDealNoOfView(int dealId) {

        boolean returnResult = false;

        SoapObject request = new SoapObject(NAMESPACE, WS_UPDATE_DEAL_NO_OF_VIEW);

        PropertyInfo dealIdPI = new PropertyInfo();
        // Set Name
        dealIdPI.setName("dealId");
        // Set Value
        dealIdPI.setValue(dealId);
        // Set dataType
        dealIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(dealIdPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_UPDATE_DEAL_NO_OF_VIEW, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeUpdateDealNoOfView: " + envelope.bodyIn.toString());

            } else {

                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

                if (Boolean.parseBoolean(responseProperty.toString())) {

                    returnResult = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnResult;
    }
}
