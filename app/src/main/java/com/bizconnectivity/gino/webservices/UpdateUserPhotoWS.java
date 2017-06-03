package com.bizconnectivity.gino.webservices;

import android.util.Base64;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.gino.ConstantWS.NAMESPACE;
import static com.bizconnectivity.gino.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.gino.ConstantWS.URL;
import static com.bizconnectivity.gino.ConstantWS.WS_UPDATE_USER_PHOTO;

public class UpdateUserPhotoWS {

    public static boolean invokeUpdateUserPhoto(int memberId, byte[] photoFile, String photoName, String photoExt) {

        boolean returnResult = false;

        SoapObject request = new SoapObject(NAMESPACE, WS_UPDATE_USER_PHOTO);

        PropertyInfo memberIdPI = new PropertyInfo();
        // Set Name
        memberIdPI.setName("memberId");
        // Set Value
        memberIdPI.setValue(memberId);
        // Set dataType
        memberIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(memberIdPI);

        PropertyInfo photoFilePI = new PropertyInfo();
        // Set Name
        photoFilePI.setName("photoFile");
        // Set Value
        photoFilePI.setValue(photoFile);
        // Set dataType
        photoFilePI.setType(Base64.class);
        // Add the property to request object
        request.addProperty(photoFilePI);

        PropertyInfo photoNamePI = new PropertyInfo();
        // Set Name
        photoNamePI.setName("photoName");
        // Set Value
        photoNamePI.setValue(photoName);
        // Set dataType
        photoNamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(photoNamePI);

        PropertyInfo photoExtPI = new PropertyInfo();
        // Set Name
        photoExtPI.setName("photoExt");
        // Set Value
        photoExtPI.setValue(photoExt);
        // Set dataType
        photoExtPI.setType(String.class);
        // Add the property to request object
        request.addProperty(photoExtPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        MarshalBase64 marshal = new MarshalBase64();
        marshal.register(envelope);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_UPDATE_USER_PHOTO, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeUpdateUserPhoto: " + envelope.bodyIn.toString());

            } else {

                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

                if (responseProperty.toString().equals("true")) {

                    returnResult = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnResult;
    }
}
