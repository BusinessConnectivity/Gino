package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.UserModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.gino.Common.*;
import static com.bizconnectivity.gino.webservices.ConstantWS.*;

public class RetrieveUserWS {

    public static UserModel invokeRetrieveUser(String email) {

        UserModel userModel = new UserModel();

        //create request
        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_USER);

        //property which holds input parameters
        PropertyInfo emailPI = new PropertyInfo();
        //set name
        emailPI.setName("email");
        //set value
        emailPI.setValue(email);
        //set datatype
        emailPI.setType(String.class);
        //add the property to request object
        request.addProperty(emailPI);

        //create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //set output SOAP object
        envelope.setOutputSoapObject(request);
        //create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_USER, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeCheckUser: " + envelope.bodyIn.toString());

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

                    if (isValidateProperty(table, "MemberId")) {
                        userModel.setUserID(Integer.parseInt(dataReturn(table, "MemberId")));
                    }

                    if (isValidateProperty(table, "MemberName")) {
                        userModel.setUserName(dataReturn(table, "MemberName"));
                    }

                    if (isValidateProperty(table, "MemberEmail")) {
                        userModel.setUserEmail(dataReturn(table, "MemberEmail"));
                    }

                    if (isValidateProperty(table, "MemberGender")) {
                        userModel.setUserGender(dataReturn(table, "MemberGender"));
                    }

                    if (isValidateProperty(table, "MemberDOB")) {
                        userModel.setUserDOB(dataReturn(table, "MemberDOB"));
                    }

                    if (isValidateProperty(table, "FacebookId")) {
                        userModel.setFacebookID(dataReturn(table, "FacebookId"));
                    }

                    if (isValidateProperty(table, "GoogleId")) {
                        userModel.setGoogleID(dataReturn(table, "GoogleId"));
                    }

                    if (isValidateProperty(table, "PhotoFile")) {
                        userModel.setPhotoFile(dataReturn(table, "PhotoFile"));
                    }

                    if (isValidateProperty(table, "PhotoName")) {
                        userModel.setPhotoName(dataReturn(table, "PhotoName"));
                    }

                    if (isValidateProperty(table, "PhotoExt")) {
                        userModel.setPhotoExt(dataReturn(table, "PhotoExt"));
                    }

                    if (isValidateProperty(table, "PhotoUrl")) {
                        userModel.setPhotoUrl(dataReturn(table, "PhotoUrl"));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userModel;
    }
}
